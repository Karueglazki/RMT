package de.flower.rmt.ui.page.opponents.manager;

import de.flower.rmt.test.AbstractRMTWicketIntegrationTests;
import org.testng.annotations.Test;

/**
 * 
 * @author flowerrrr
 */

public class OpponentEditPageTest extends AbstractRMTWicketIntegrationTests {

    @Test
    public void testRender() {
        wicketTester.startPage(new OpponentEditPage());
        wicketTester.dumpPage();
    }


}