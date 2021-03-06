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
package com.hp.mwtests.ts.jta.recovery;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class TestXAResource implements XAResource {
    private Xid xid;
    private int commitCount;
    private int rollbackCount;
    private int recoveryCount;

    public TestXAResource(Xid xid) {
        this.xid = xid;
    }

    public TestXAResource() {
    }

    public Xid[] recover(int flag) throws XAException {
        recoveryCount++;
        return new Xid[] { xid };
    }

    public boolean isSameRM(XAResource xares) throws XAException {
        return false;
    }

    public int prepare(Xid xid) throws XAException {
        this.xid = xid;
        return XA_OK;
    }

    public void commit(Xid id, boolean onePhase) throws XAException {
        System.out.println("committed");
        xid = null;
        commitCount++;
    }

    public void rollback(Xid xid) throws XAException {
        System.out.println("rolled back");
        xid = null;
        rollbackCount++;
    }

    public void forget(Xid xid) throws XAException {
    }

    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    public boolean setTransactionTimeout(int seconds) throws XAException {
        return true;
    }

    public void start(Xid xid, int flags) throws XAException {
    }

    public void end(Xid xid, int flags) throws XAException {
    }

    public Xid getXid() {
        return xid;
    }

    public int commitCount() {
        return commitCount;
    }

    public int rollbackCount() {
        return rollbackCount;
    }

    public int recoveryCount() {
        return recoveryCount;
    }

}
