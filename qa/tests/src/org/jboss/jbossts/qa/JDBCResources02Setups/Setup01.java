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
//
// Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003
//
// Arjuna Technologies Ltd.,
// Newcastle upon Tyne,
// Tyne and Wear,
// UK.
//
// $Id: Setup01.java,v 1.6 2004/04/21 08:30:54 jcoleman Exp $
//

package org.jboss.jbossts.qa.JDBCResources02Setups;

import org.jboss.jbossts.qa.JDBCResources02.*;
import org.jboss.jbossts.qa.Utils.JDBCProfileStore;
import org.jboss.jbossts.qa.Utils.OAInterface;
import org.jboss.jbossts.qa.Utils.ORBInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class Setup01
{
	public static void main(String[] args)
	{
		boolean passed = true;

		try
		{
			ORBInterface.initORB(args, null);
			OAInterface.initOA();

			String profileName = args[args.length - 1];

			int numberOfDrivers = JDBCProfileStore.numberOfDrivers(profileName);
			for (int index = 0; index < numberOfDrivers; index++)
			{
				String driver = JDBCProfileStore.driver(profileName, index);

				Class.forName(driver);
			}

			String databaseURL = JDBCProfileStore.databaseURL(profileName);
			String databaseUser = JDBCProfileStore.databaseUser(profileName);
			String databasePassword = JDBCProfileStore.databasePassword(profileName);
			String databaseDynamicClass = JDBCProfileStore.databaseDynamicClass(profileName);

			Connection connection;
			if (databaseDynamicClass != null)
			{
				Properties databaseProperties = new Properties();

				databaseProperties.put(com.arjuna.ats.jdbc.TransactionalDriver.userName, databaseUser);
				databaseProperties.put(com.arjuna.ats.jdbc.TransactionalDriver.password, databasePassword);
				databaseProperties.put(com.arjuna.ats.jdbc.TransactionalDriver.dynamicClass, databaseDynamicClass);

				connection = DriverManager.getConnection(databaseURL, databaseProperties);
			}
			else
			{
				connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
			}

			Statement statement = connection.createStatement();

            String tableName = JDBCProfileStore.getTableName(databaseUser, "Infotable");

			try
			{
				System.err.println("DROP TABLE " + tableName);
				statement.executeUpdate("DROP TABLE " + tableName);
			}
			catch (java.sql.SQLException s)
			{
				if(!(s.getSQLState().startsWith("42") // old ms sql 2000 drivers
						|| s.getSQLState().equals("S0005") // ms sql 2005 drivers
						|| s.getSQLState().equals("ZZZZZ"))) // sybase jConnect drivers
				{
					System.err.println("Setup01.main: " + s);
					System.err.println("SQL state is: <" + s.getSQLState() + ">");
				}
			}
			System.err.println("CREATE TABLE " + tableName + " (Name VARCHAR(64), Value VARCHAR(64))");
			statement.executeUpdate("CREATE TABLE " + tableName + " (Name VARCHAR(64), Value VARCHAR(64))");

			statement.close();
			connection.close();
		}
		catch (Exception exception)
		{
			System.err.println("Setup01.main: " + exception);
			exception.printStackTrace(System.err);
			System.out.println("Failed");
			passed = false;
		}

		try
		{
			OAInterface.shutdownOA();
			ORBInterface.shutdownORB();
		}
		catch (Exception exception)
		{
			System.err.println("Setup01.main: " + exception);
			exception.printStackTrace(System.err);
			System.out.println("Failed");
			passed = false;
		}

		if (passed)
		{
			System.out.println("Passed");
		}
		else
		{
			System.out.println("Failed");
		}

	}
}
