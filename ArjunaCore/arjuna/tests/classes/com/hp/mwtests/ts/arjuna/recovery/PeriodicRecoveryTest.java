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
 * (C) 2016,
 * @author JBoss Inc.
 */
package com.hp.mwtests.ts.arjuna.recovery;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.arjuna.ats.arjuna.common.RecoveryEnvironmentBean;
import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.arjuna.ats.internal.arjuna.recovery.PeriodicRecovery;

public class PeriodicRecoveryTest {
    @Test
    public void testInitialDelay() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RecoveryEnvironmentBean bean = recoveryPropertyManager.getRecoveryEnvironmentBean()
                .setPeriodicRecoveryInitilizationOffset(1);
        PeriodicRecovery periodicRecovery = new PeriodicRecovery(false, false);
        Method doInitialWait = periodicRecovery.getClass().getDeclaredMethod("doInitialWait");
        doInitialWait.setAccessible(true);
        long l = System.currentTimeMillis();
        try {
            doInitialWait.invoke(periodicRecovery, null);
        } finally {
            doInitialWait.setAccessible(false);
        }
        assertTrue(System.currentTimeMillis() - l > 500);
    }
}
