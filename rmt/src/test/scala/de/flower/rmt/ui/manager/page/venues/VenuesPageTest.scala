package de.flower.rmt.ui.manager.page.venues

import de.flower.rmt.test.AbstractWicketTests
import org.testng.annotations.Test
/**
 * 
 * @author flowerrrr
 */

class VenuesPageTest extends AbstractWicketTests {

    @Test
    def testRender() {
        wicketTester.startPage(new VenuesPage())
        wicketTester.dumpPage()
    }


}