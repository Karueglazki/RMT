package de.flower.rmt.ui.page.event.player;

import de.flower.common.test.wicket.AbstractWicketUnitTests;
import de.flower.rmt.model.db.entity.Invitation;
import de.flower.rmt.model.db.entity.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */
public class InvitationFormPanelTest extends AbstractWicketUnitTests {

    @Test
    public void testRender() {
        Invitation invitation = new Invitation(null, (User) null);
        wicketTester.startComponentInPage(new InvitationFormTestPanel(Model.of(invitation)));
        wicketTester.dumpComponentWithPage();
    }

    private static class InvitationFormTestPanel extends InvitationFormPanel {

        public InvitationFormTestPanel(final IModel<Invitation> model) {
            super("panel", model);
        }

        @Override
        protected void onSubmit(final Invitation invitation, final AjaxRequestTarget target) {
        }
    }

}
