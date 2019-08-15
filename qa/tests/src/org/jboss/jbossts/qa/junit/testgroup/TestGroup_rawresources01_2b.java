/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
 * (C) 2009,
 * @author JBoss Inc.
 */
package org.jboss.jbossts.qa.junit.testgroup;

import org.jboss.jbossts.qa.junit.*;
import org.junit.*;

// Automatically generated by XML2JUnit
public class TestGroup_rawresources01_2b extends TestGroupBase
{
	public String getTestGroupName()
	{
		return "rawresources01_2b";
	}

	protected Task server0 = null;

	@Before public void setUp()
	{
		super.setUp();
		server0 = createTask("server0", com.arjuna.ats.arjuna.recovery.RecoveryManager.class, Task.TaskType.EXPECT_READY, 480);
		server0.start("-test");
	}

	@After public void tearDown()
	{
		try {
			server0.terminate();
            removeServerIORStore("task0", "$(1)", "$(2)");
		} finally {
			super.tearDown();
		}
	}

    private void runOneServerOneClient(Class clientClass) {

        Task server1 = createTask("server1", org.jboss.jbossts.qa.RawResources01Servers.Server02.class, Task.TaskType.EXPECT_READY, 480);
		server1.start("$(1)", "$(2)");
		Task client0 = createTask("client0", clientClass, Task.TaskType.EXPECT_PASS_FAIL, 480);
		client0.start("$(1)", "$(2)");
		client0.waitFor();
		server1.terminate();
    }

	@Test public void RawResources01_2b_Test001()
	{
		setTestName("Test001");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client001.class);
	}

	@Test public void RawResources01_2b_Test002()
	{
		setTestName("Test002");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client002.class);
	}

	@Test public void RawResources01_2b_Test003()
	{
		setTestName("Test003");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client003.class);
	}

	@Test public void RawResources01_2b_Test004()
	{
		setTestName("Test004");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client004.class);
	}

	@Test public void RawResources01_2b_Test005()
	{
		setTestName("Test005");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client005.class);
	}

	@Test public void RawResources01_2b_Test006()
	{
		setTestName("Test006");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client006.class);
	}

	@Test public void RawResources01_2b_Test007()
	{
		setTestName("Test007");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client007.class);
	}

	@Test public void RawResources01_2b_Test008()
	{
		setTestName("Test008");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client008.class);
	}

	@Test public void RawResources01_2b_Test009()
	{
		setTestName("Test009");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client009.class);
	}

	@Test public void RawResources01_2b_Test010()
	{
		setTestName("Test010");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client010.class);
	}

	@Test public void RawResources01_2b_Test011()
	{
		setTestName("Test011");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client011.class);
	}

	@Test public void RawResources01_2b_Test012()
	{
		setTestName("Test012");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client012.class);
	}

	@Test public void RawResources01_2b_Test013()
	{
		setTestName("Test013");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client013.class);
	}

	@Test public void RawResources01_2b_Test014()
	{
		setTestName("Test014");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client014.class);
	}

	@Test public void RawResources01_2b_Test015()
	{
		setTestName("Test015");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client015.class);
	}

	@Test public void RawResources01_2b_Test016()
	{
		setTestName("Test016");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client016.class);
	}

	@Test public void RawResources01_2b_Test017()
	{
		setTestName("Test017");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client017.class);
	}

	@Test public void RawResources01_2b_Test018()
	{
		setTestName("Test018");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client018.class);
	}

	@Test public void RawResources01_2b_Test019()
	{
		setTestName("Test019");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client019.class);
	}

	@Test public void RawResources01_2b_Test020()
	{
		setTestName("Test020");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client020.class);
	}

	@Test public void RawResources01_2b_Test021()
	{
		setTestName("Test021");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client021.class);
	}

	@Test public void RawResources01_2b_Test022()
	{
		setTestName("Test022");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client022.class);
	}

	@Test public void RawResources01_2b_Test023()
	{
		setTestName("Test023");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client023.class);
	}

	@Test public void RawResources01_2b_Test024()
	{
		setTestName("Test024");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client024.class);
	}

	@Test public void RawResources01_2b_Test025()
	{
		setTestName("Test025");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client025.class);
	}

	@Test public void RawResources01_2b_Test026()
	{
		setTestName("Test026");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client026.class);
	}

	@Test public void RawResources01_2b_Test027()
	{
		setTestName("Test027");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client027.class);
	}

	@Test public void RawResources01_2b_Test028()
	{
		setTestName("Test028");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client028.class);
	}

	@Test public void RawResources01_2b_Test029()
	{
		setTestName("Test029");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client029.class);
	}

	@Test public void RawResources01_2b_Test030()
	{
		setTestName("Test030");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client030.class);
	}

	@Test public void RawResources01_2b_Test031()
	{
		setTestName("Test031");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client031.class);
	}

	@Test public void RawResources01_2b_Test032()
	{
		setTestName("Test032");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client032.class);
	}

	@Test public void RawResources01_2b_Test033()
	{
		setTestName("Test033");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client033.class);
	}

	@Test public void RawResources01_2b_Test034()
	{
		setTestName("Test034");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client034.class);
	}

	@Test public void RawResources01_2b_Test035()
	{
		setTestName("Test035");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client035.class);
	}

	@Test public void RawResources01_2b_Test036()
	{
		setTestName("Test036");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client036.class);
	}

	@Test public void RawResources01_2b_Test037()
	{
		setTestName("Test037");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client037.class);
	}

	@Test public void RawResources01_2b_Test038()
	{
		setTestName("Test038");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client038.class);
	}

	@Test public void RawResources01_2b_Test039()
	{
		setTestName("Test039");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client039.class);
	}

	@Test public void RawResources01_2b_Test040()
	{
		setTestName("Test040");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client040.class);
	}

	@Test public void RawResources01_2b_Test041()
	{
		setTestName("Test041");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client041.class);
	}

	@Test public void RawResources01_2b_Test042()
	{
		setTestName("Test042");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client042.class);
	}

	@Test public void RawResources01_2b_Test043()
	{
		setTestName("Test043");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client043.class);
	}

	@Test public void RawResources01_2b_Test044()
	{
		setTestName("Test044");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client044.class);
	}

	@Test public void RawResources01_2b_Test045()
	{
		setTestName("Test045");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client045.class);
	}

	@Test public void RawResources01_2b_Test046()
	{
		setTestName("Test046");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client046.class);
	}

	@Test public void RawResources01_2b_Test047()
	{
		setTestName("Test047");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client047.class);
	}

	@Test public void RawResources01_2b_Test048()
	{
		setTestName("Test048");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client048.class);
	}

	@Test public void RawResources01_2b_Test049()
	{
		setTestName("Test049");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client049.class);
	}

	@Test public void RawResources01_2b_Test050()
	{
		setTestName("Test050");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client050.class);
	}

	@Test public void RawResources01_2b_Test051()
	{
		setTestName("Test051");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client051.class);
	}

	@Test public void RawResources01_2b_Test052()
	{
		setTestName("Test052");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client052.class);
	}

	@Test public void RawResources01_2b_Test053()
	{
		setTestName("Test053");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client053.class);
	}

	@Test public void RawResources01_2b_Test054()
	{
		setTestName("Test054");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client054.class);
	}

	@Test public void RawResources01_2b_Test055()
	{
		setTestName("Test055");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client055.class);
	}

	@Test public void RawResources01_2b_Test056()
	{
		setTestName("Test056");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client056.class);
	}

	@Test public void RawResources01_2b_Test057()
	{
		setTestName("Test057");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client057.class);
	}

	@Test public void RawResources01_2b_Test058()
	{
		setTestName("Test058");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client058.class);
	}

	@Test public void RawResources01_2b_Test059()
	{
		setTestName("Test059");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client059.class);
	}

	@Test public void RawResources01_2b_Test060()
	{
		setTestName("Test060");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client060.class);
	}

	@Test public void RawResources01_2b_Test061()
	{
		setTestName("Test061");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client061.class);
	}

	@Test public void RawResources01_2b_Test062()
	{
		setTestName("Test062");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client062.class);
	}

	@Test public void RawResources01_2b_Test063()
	{
		setTestName("Test063");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client063.class);
	}

	@Test public void RawResources01_2b_Test064()
	{
		setTestName("Test064");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client064.class);
	}

	@Test public void RawResources01_2b_Test065()
	{
		setTestName("Test065");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client065.class);
	}

	@Test public void RawResources01_2b_Test066()
	{
		setTestName("Test066");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client066.class);
	}

	@Test public void RawResources01_2b_Test067()
	{
		setTestName("Test067");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client067.class);
	}

	@Test public void RawResources01_2b_Test068()
	{
		setTestName("Test068");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client068.class);
	}

	@Test public void RawResources01_2b_Test069()
	{
		setTestName("Test069");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client069.class);
	}

	@Test public void RawResources01_2b_Test070()
	{
		setTestName("Test070");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client070.class);
	}

	@Test public void RawResources01_2b_Test071()
	{
		setTestName("Test071");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client071.class);
	}

	@Test public void RawResources01_2b_Test072()
	{
		setTestName("Test072");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client072.class);
	}

	@Test public void RawResources01_2b_Test073()
	{
		setTestName("Test073");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client073.class);
	}

	@Test public void RawResources01_2b_Test074()
	{
		setTestName("Test074");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client074.class);
	}

	@Test public void RawResources01_2b_Test075()
	{
		setTestName("Test075");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client075.class);
	}

	@Test public void RawResources01_2b_Test076()
	{
		setTestName("Test076");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client076.class);
	}

	@Test public void RawResources01_2b_Test077()
	{
		setTestName("Test077");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client077.class);
	}

	@Test public void RawResources01_2b_Test078()
	{
		setTestName("Test078");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client078.class);
	}

	@Test public void RawResources01_2b_Test079()
	{
		setTestName("Test079");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client079.class);
	}

	@Test public void RawResources01_2b_Test080()
	{
		setTestName("Test080");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client080.class);
	}

	@Test public void RawResources01_2b_Test081()
	{
		setTestName("Test081");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client081.class);
	}

	@Test public void RawResources01_2b_Test082()
	{
		setTestName("Test082");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client082.class);
	}

	@Test public void RawResources01_2b_Test083()
	{
		setTestName("Test083");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client083.class);
	}

	@Test public void RawResources01_2b_Test084()
	{
		setTestName("Test084");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client084.class);
	}

	@Test public void RawResources01_2b_Test085()
	{
		setTestName("Test085");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client085.class);
	}

	@Test public void RawResources01_2b_Test086()
	{
		setTestName("Test086");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client086.class);
	}

	@Test public void RawResources01_2b_Test087()
	{
		setTestName("Test087");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client087.class);
	}

	@Test public void RawResources01_2b_Test088()
	{
		setTestName("Test088");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client088.class);
	}

	@Test public void RawResources01_2b_Test089()
	{
		setTestName("Test089");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client089.class);
	}

	@Test public void RawResources01_2b_Test090()
	{
		setTestName("Test090");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client090.class);
	}

	@Test public void RawResources01_2b_Test091()
	{
		setTestName("Test091");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client091.class);
	}

	@Test public void RawResources01_2b_Test092()
	{
		setTestName("Test092");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client092.class);
	}

	@Test public void RawResources01_2b_Test093()
	{
		setTestName("Test093");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client093.class);
	}

	@Test public void RawResources01_2b_Test094()
	{
		setTestName("Test094");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client094.class);
	}

	@Test public void RawResources01_2b_Test095()
	{
		setTestName("Test095");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client095.class);
	}

	@Test public void RawResources01_2b_Test096()
	{
		setTestName("Test096");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client096.class);
	}

	@Test public void RawResources01_2b_Test097()
	{
		setTestName("Test097");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client097.class);
	}

	@Test public void RawResources01_2b_Test098()
	{
		setTestName("Test098");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client098.class);
	}

	@Test public void RawResources01_2b_Test099()
	{
		setTestName("Test099");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client099.class);
	}

	@Test public void RawResources01_2b_Test100()
	{
		setTestName("Test100");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client100.class);
	}

	@Test public void RawResources01_2b_Test101()
	{
		setTestName("Test101");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client101.class);
	}

	@Test public void RawResources01_2b_Test102()
	{
		setTestName("Test102");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client102.class);
	}

	@Test public void RawResources01_2b_Test103()
	{
		setTestName("Test103");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client103.class);
	}

	@Test public void RawResources01_2b_Test104()
	{
		setTestName("Test104");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client104.class);
	}

	@Test public void RawResources01_2b_Test105()
	{
		setTestName("Test105");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client105.class);
	}

	@Test public void RawResources01_2b_Test106()
	{
		setTestName("Test106");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client106.class);
	}

	@Test public void RawResources01_2b_Test107()
	{
		setTestName("Test107");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client107.class);
	}

	@Test public void RawResources01_2b_Test108()
	{
		setTestName("Test108");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client108.class);
	}

	@Test public void RawResources01_2b_Test109()
	{
		setTestName("Test109");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client109.class);
	}

	@Test public void RawResources01_2b_Test110()
	{
		setTestName("Test110");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client110.class);
	}

	@Test public void RawResources01_2b_Test111()
	{
		setTestName("Test111");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client111.class);
	}

	@Test public void RawResources01_2b_Test112()
	{
		setTestName("Test112");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client112.class);
	}

	@Test public void RawResources01_2b_Test113()
	{
		setTestName("Test113");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client113.class);
	}

	@Test public void RawResources01_2b_Test114()
	{
		setTestName("Test114");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client114.class);
	}

	@Test public void RawResources01_2b_Test115()
	{
		setTestName("Test115");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client115.class);
	}

	@Test public void RawResources01_2b_Test116()
	{
		setTestName("Test116");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client116.class);
	}

	@Test public void RawResources01_2b_Test117()
	{
		setTestName("Test117");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client117.class);
	}

	@Test public void RawResources01_2b_Test118()
	{
		setTestName("Test118");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client118.class);
	}

	@Test public void RawResources01_2b_Test119()
	{
		setTestName("Test119");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client119.class);
	}

	@Test public void RawResources01_2b_Test120()
	{
		setTestName("Test120");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client120.class);
	}

	@Test public void RawResources01_2b_Test121()
	{
		setTestName("Test121");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client121.class);
	}

	@Test public void RawResources01_2b_Test122()
	{
		setTestName("Test122");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client122.class);
	}

	@Test public void RawResources01_2b_Test123()
	{
		setTestName("Test123");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client123.class);
	}

	@Test public void RawResources01_2b_Test124()
	{
		setTestName("Test124");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client124.class);
	}

	@Test public void RawResources01_2b_Test125()
	{
		setTestName("Test125");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client125.class);
	}

	@Test public void RawResources01_2b_Test126()
	{
		setTestName("Test126");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client126.class);
	}

	@Test public void RawResources01_2b_Test127()
	{
		setTestName("Test127");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client127.class);
	}

	@Test public void RawResources01_2b_Test128()
	{
		setTestName("Test128");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client128.class);
	}

	@Test public void RawResources01_2b_Test129()
	{
		setTestName("Test129");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client129.class);
	}

	@Test public void RawResources01_2b_Test130()
	{
		setTestName("Test130");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client130.class);
	}

	@Test public void RawResources01_2b_Test131()
	{
		setTestName("Test131");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client131.class);
	}

	@Test public void RawResources01_2b_Test132()
	{
		setTestName("Test132");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client132.class);
	}

	@Test public void RawResources01_2b_Test133()
	{
		setTestName("Test133");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client133.class);
	}

	@Test public void RawResources01_2b_Test134()
	{
		setTestName("Test134");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client134.class);
	}

	@Test public void RawResources01_2b_Test135()
	{
		setTestName("Test135");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client135.class);
	}

	@Test public void RawResources01_2b_Test136()
	{
		setTestName("Test136");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client136.class);
	}

	@Test public void RawResources01_2b_Test137()
	{
		setTestName("Test137");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client137.class);
	}

	@Test public void RawResources01_2b_Test138()
	{
		setTestName("Test138");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client138.class);
	}

	@Test public void RawResources01_2b_Test139()
	{
		setTestName("Test139");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client139.class);
	}

	@Test public void RawResources01_2b_Test140()
	{
		setTestName("Test140");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client140.class);
	}

	@Test public void RawResources01_2b_Test141()
	{
		setTestName("Test141");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client141.class);
	}

	@Test public void RawResources01_2b_Test142()
	{
		setTestName("Test142");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client142.class);
	}

	@Test public void RawResources01_2b_Test143()
	{
		setTestName("Test143");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client143.class);
	}

	@Test public void RawResources01_2b_Test144()
	{
		setTestName("Test144");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client144.class);
	}

	@Test public void RawResources01_2b_Test145()
	{
		setTestName("Test145");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client145.class);
	}

    @Test public void RawResources01_2b_Test146()
	{
		setTestName("Test146");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client146.class);
	}

    @Test public void RawResources01_2b_Test147()
	{
		setTestName("Test147");
        runOneServerOneClient(org.jboss.jbossts.qa.RawResources01Clients2.Client147.class);
	}
}
