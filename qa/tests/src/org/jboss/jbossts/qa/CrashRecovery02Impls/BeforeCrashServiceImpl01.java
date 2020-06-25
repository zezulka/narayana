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
// $Id: BeforeCrashServiceImpl01.java,v 1.5 2003/07/17 11:52:48 jcoleman Exp $
//

package org.jboss.jbossts.qa.CrashRecovery02Impls;

/*
 * Copyright (C) 1999-2001 by HP Bluestone Software, Inc. All rights Reserved.
 *
 * HP Arjuna Labs,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: BeforeCrashServiceImpl01.java,v 1.5 2003/07/17 11:52:48 jcoleman Exp $
 */

/*
 * Try to get around the differences between Ansi CPP and
 * K&R cpp with concatenation.
 */

/*
 * Copyright (C) 1999-2001 by HP Bluestone Software, Inc. All rights Reserved.
 *
 * HP Arjuna Labs,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: BeforeCrashServiceImpl01.java,v 1.5 2003/07/17 11:52:48 jcoleman Exp $
 */


import org.jboss.jbossts.qa.CrashRecovery02.*;
import org.jboss.jbossts.qa.Utils.OAInterface;
import org.jboss.jbossts.qa.Utils.ORBInterface;
import org.jboss.jbossts.qa.Utils.OTS;
import org.jboss.jbossts.qa.Utils.ServerIORStore;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.ResourceHelper;
import org.omg.CosTransactions.ResourcePOATie;

public class BeforeCrashServiceImpl01 implements BeforeCrashServiceOperations
{
	public BeforeCrashServiceImpl01(int serviceNumber, int objectNumber)
	{
		System.out.println("BeforeCrashServiceImpl01(" + serviceNumber + ", " + objectNumber + ")");
		_serviceNumber = serviceNumber;
		_objectNumber = objectNumber;
	}

	public void setup_oper(ResourceBehavior[] resource_behaviors)
	{
		ResourceImpl01[] resourceImpl = new ResourceImpl01[resource_behaviors.length];
		Resource[] resource = new Resource[resource_behaviors.length];
		RecoveryCoordinator[] recoveryCoordinator = new RecoveryCoordinator[resource_behaviors.length];

		for (int index = 0; index < resource_behaviors.length; index++)
		{
			try
			{
				resourceImpl[index] = new ResourceImpl01(_serviceNumber, _objectNumber, index, resource_behaviors[index]);
				ResourcePOATie servant = new ResourcePOATie(resourceImpl[index]);

				OAInterface.objectIsReady(servant);
				resource[index] = ResourceHelper.narrow(OAInterface.corbaReference(servant));

				recoveryCoordinator[index] = OTS.current().get_control().get_coordinator().register_resource(resource[index]);

				System.out.println("BeforeCrashServiceImpl01: storing IOR \"RecoveryCoordinator_" + _serviceNumber + "_" + _objectNumber + "_" + index + "\"");
				ServerIORStore.storeIOR("RecoveryCoordinator_" + _serviceNumber + "_" + _objectNumber + "_" + index, ORBInterface.orb().object_to_string(recoveryCoordinator[index]));
			}
			catch (Exception exception)
			{
				System.err.println("BeforeCrashServiceImpl01.setup_oper: " + exception);
				exception.printStackTrace(System.err);
				_isCorrect = false;
			}
		}
	}

	public boolean is_correct()
	{
		System.err.println("BeforeCrashServiceImpl01.is_correct: " + _isCorrect);

		return _isCorrect;
	}

	private int _serviceNumber;
	private int _objectNumber;
	private boolean _isCorrect = true;
}
