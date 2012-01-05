/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
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
 * Copyright (C) 2003, 2004
 * Arjuna Technologies Limited
 * Newcastle upon Tyne, UK
 *
 * $Id: HelloServer.java 2342 2006-03-30 13:06:17Z  $
 */
package com.arjuna.demo.simple;

import com.arjuna.orbportability.OA;
import com.arjuna.orbportability.ORB;
import com.arjuna.orbportability.RootOA;
import org.omg.CORBA.SystemException;

/**
 * This is the entry point into a simple server application that is used to expose a transaction-aware object, the
 * <CODE>HelloImpl</CODE>. All that this application does is to create the <CODE>HelloImpl</CODE> server object and
 * bind it as a service.
 */
public class HelloServer
{
    /**
     * The application entry point.
     *
     * @param args  Not used by this application.
     */
    public static void main(String[] args)
    {
        // 0. Define and create the ORB
        // Define an ORB suitable for use by the JBoss Transactions product ORB portability layer.
        ORB myORB = null;
        // Define an object adapter suitable for use by the JBoss Transactions product ORB portability layer.
        RootOA myOA = null;
        try
        {
            // Initialize the ORB reference using the JBoss Transactions product ORB portability layer.
            myORB = ORB.getInstance("ServerSide");
            // Initialize the object adapter reference using the JBoss Transactions product ORB portability layer.
            myOA = OA.getRootOA(myORB);
            // Initialize the ORB using the JBoss Transactions product ORB portability layer.
            myORB.initORB(args, null);
            // Initialize the object adapter reference using the JBoss Transactions product ORB portability layer.
            myOA.initOA();
        }
        catch (Exception e)
        {
            // The ORB has not been correctly configured!
            // Display as much help as possible to the user track down the configuration problem
            System.err.println("Trailmap Error: ORB Initialisation failed: " + e);
            e.printStackTrace();
            System.exit(0);
        }

        // 1. Create the workhorse of this section of the trailmap, the HelloImpl which responds to calls under
        // transactional control
        HelloImpl hello = new HelloImpl();

        // 2. Create an IOR reference to the <CODE>HelloImpl</CODE> that can be used by the <CODE>HelloClient</CODE>
        // to locate the server
        // Create the reference
        String reference = myORB.orb().object_to_string(myOA.corbaReference(hello));
        try
        {
            // Write the reference to disk for the client to read
            java.io.FileOutputStream file = new java.io.FileOutputStream("ObjectId");
            java.io.PrintStream pfile = new java.io.PrintStream(file);
            pfile.println(reference);
            file.close();
        }
        catch (java.io.IOException ioe)
        {
            // The IOR could not be persisted
            // Display as much help as possible to the user track down the configuration problem
            System.out.println("Trailmap Error: Could not persist the IOR of the HelloImpl: " + ioe);
            ioe.printStackTrace();
            System.exit(0);
        }

        // 3. Start the object adapter listening for requests from the client
        try
        {
            // Display information to indicate that the client application may now be ran
            System.out.println("The Hello server is now ready...");
            myOA.run();
        }
        catch (SystemException ex)
        {
            // The OA could not be ran
            // Display as much help as possible to the user track down the configuration problem
            System.out.println("Trailmap Error: The ORB object adapter could not ran: " + ex);
            ex.printStackTrace();
        }
    }
}
