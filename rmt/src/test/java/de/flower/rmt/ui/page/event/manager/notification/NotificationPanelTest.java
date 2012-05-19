package de.flower.rmt.ui.page.event.manager.notification;

import de.flower.rmt.model.db.entity.event.Event;
import de.flower.rmt.test.AbstractRMTWicketMockitoTests;
import de.flower.rmt.test.TestData;
import org.apache.wicket.model.Model;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */
public class NotificationPanelTest extends AbstractRMTWicketMockitoTests {

    @Test
    public void testRender() {
        Event event = new TestData().newEvent();
        wicketTester.startComponentInPage(new NotificationPanel("panel", Model.of(event)));
        wicketTester.dumpPage();
    }
}
