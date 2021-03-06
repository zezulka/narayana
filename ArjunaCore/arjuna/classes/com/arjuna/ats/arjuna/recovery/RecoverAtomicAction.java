/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
/*
 * Copyright (C) 1999-2001 by HP Bluestone Software, Inc. All rights Reserved.
 *
 * HP Arjuna Labs,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: RecoverAtomicAction.java 2342 2006-03-30 13:06:17Z  $
 */

package com.arjuna.ats.arjuna.recovery;

import com.arjuna.ats.arjuna.AtomicAction;
import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.coordinator.ActionStatus;
import com.arjuna.ats.arjuna.logging.tsLogger;
import com.arjuna.ats.internal.arjuna.recovery.AtomicActionExpiryScanner;

import io.narayana.tracing.NarayanaSpanBuilder;
import io.narayana.tracing.TracingUtils;
import io.narayana.tracing.names.SpanName;
import io.narayana.tracing.names.TagName;
import io.opentracing.Scope;
import io.opentracing.Span;

public class RecoverAtomicAction extends AtomicAction {
    /**
     * Re-creates/activates an AtomicAction for the specified transaction Uid.
     */
    public RecoverAtomicAction(Uid rcvUid, int theStatus) {
        super(rcvUid);
        _theStatus = theStatus;
        _activated = activate();
    }

    /**
     * Replays phase 2 of the commit protocol.
     */
    public void replayPhase2() {
        if (tsLogger.logger.isDebugEnabled()) {
            tsLogger.logger.debug("RecoverAtomicAction.replayPhase2 recovering " + get_uid() + " ActionStatus is "
                    + ActionStatus.stringForm(_theStatus));
        }

        if (_activated) {
            Span s = new NarayanaSpanBuilder(SpanName.BRANCH_RECOVERY)
                    .tag(TagName.UID, get_uid().toString())
                    .tag(TagName.STATUS, ActionStatus.stringForm(_theStatus))
                    .build();
            try(Scope _s = TracingUtils.activateSpan(s)) {
                if ((_theStatus == ActionStatus.PREPARED) || (_theStatus == ActionStatus.COMMITTING)
                        || (_theStatus == ActionStatus.COMMITTED) || (_theStatus == ActionStatus.H_COMMIT)
                        || (_theStatus == ActionStatus.H_MIXED) || (_theStatus == ActionStatus.H_HAZARD)) {
                    super.phase2Commit(_reportHeuristics);
                } else if ((_theStatus == ActionStatus.ABORTED) || (_theStatus == ActionStatus.H_ROLLBACK)
                        || (_theStatus == ActionStatus.ABORTING) || (_theStatus == ActionStatus.ABORT_ONLY)) {
                    super.phase2Abort(_reportHeuristics);
                } else {
                    tsLogger.i18NLogger.warn_recovery_RecoverAtomicAction_2(ActionStatus.stringForm(_theStatus));
                }
            } finally {
                s.finish();
            }


            if (tsLogger.logger.isDebugEnabled()) {
                tsLogger.logger.debug("RecoverAtomicAction.replayPhase2( " + get_uid() + " )  finished");
            }
        } else {
            tsLogger.i18NLogger.warn_recovery_RecoverAtomicAction_4(get_uid());

            /*
             * Failure to activate so move the log. Unlikely to get better automatically!
             */

            AtomicActionExpiryScanner scanner = new AtomicActionExpiryScanner();

            try {
                scanner.moveEntry(get_uid());
            } catch (final Exception ex) {
                tsLogger.i18NLogger.warn_recovery_RecoverAtomicAction_5(get_uid());
            }
        }
    }

    // Current transaction status
    // (retrieved from the TransactionStatusManager)
    private int _theStatus;

    // Flag to indicate that this transaction has been re-activated
    // successfully.
    private boolean _activated = false;

    // whether heuristic reporting on phase 2 commit is enabled.
    private boolean _reportHeuristics = true;

}
