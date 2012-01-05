/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package com.jboss.transaction.txinterop.webservices.bainterop.participant;

import java.util.TimerTask;

import com.arjuna.webservices.util.TransportTimer;
import com.arjuna.wst11.messaging.engines.ParticipantCompletionParticipantEngine;

public class CompletedParticipant extends ParticipantCompletionParticipantAdapter
{
    private ParticipantCompletionParticipantEngine engine ;
    
    public void setEngine(final ParticipantCompletionParticipantEngine engine)
    {
	this.engine = engine ;
    }
    
    public void initialiseTimeout()
    {
        final TimerTask timerTask = new TimerTask() {
            public void run() {
                engine.completed() ;
            }
        } ;
        TransportTimer.getTimer().schedule(timerTask, 2000) ;
    }
}
