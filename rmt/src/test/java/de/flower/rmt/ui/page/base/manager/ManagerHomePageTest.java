package de.flower.rmt.ui.page.base.manager;

import de.flower.rmt.test.AbstractRMTWicketIntegrationTests;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */

public class ManagerHomePageTest extends AbstractRMTWicketIntegrationTests {

    @Test
    public void testRender() {
        wicketTester.startPage(new ManagerHomePage());
        wicketTester.dumpPage();
    }
}