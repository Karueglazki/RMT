package de.flower.rmt.ui.manager.page.players

import de.flower.rmt.test.WicketTests
import org.testng.annotations.Test
/**
 * 
 * @author flowerrrr
 */

class PlayerEditPageTest extends WicketTests {

    @Test
    def renderPage() {
        wicketTester.startPage(new PlayerEditPage())
        wicketTester.dumpPage()
    }


}