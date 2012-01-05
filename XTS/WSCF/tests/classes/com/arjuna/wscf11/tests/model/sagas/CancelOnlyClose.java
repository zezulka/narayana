package com.arjuna.wscf11.tests.model.sagas;

import com.arjuna.mw.wscf.model.sagas.api.UserCoordinator;
import com.arjuna.mw.wscf.model.sagas.exceptions.CoordinatorCancelledException;
import com.arjuna.mw.wscf11.model.sagas.UserCoordinatorFactory;
import com.arjuna.wscf11.tests.WSCF11TestUtils;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 */

public class CancelOnlyClose
{
    @Test
    public void testCancelOnlyClose()
            throws Exception
    {
        System.out.println("Running test : " + this.getClass().getName());

        UserCoordinator ua = UserCoordinatorFactory.userCoordinator();

	try
	{
	    ua.begin("Sagas11HLS");

	    System.out.println("Started: "+ua.identifier()+"\n");

	    ua.setCancelOnly();

	    ua.close();

        fail("Close succeeded after setCancelOnly");
    }
	catch (CoordinatorCancelledException ex)
	{
	    // we should get here
        WSCF11TestUtils.cleanup(ua);
    }
	catch (Exception ex)
	{
	    WSCF11TestUtils.cleanup(ua);
        throw ex;
    }
    }
}