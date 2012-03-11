package de.flower.rmt.ui.manager.page.squad;

import de.flower.common.ui.ajax.behavior.AjaxSlideToggleBehavior;
import de.flower.common.ui.ajax.markup.html.AjaxLink;
import de.flower.common.ui.js.JQuery;
import de.flower.rmt.model.Team;
import de.flower.rmt.ui.common.panel.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author flowerrrr
 */
public class SquadSecondaryPanel extends BasePanel {

    private AjaxSlideToggleBehavior toggleBehavior;

    public SquadSecondaryPanel(IModel<Team> model) {

        final AjaxLink addButton = new AjaxLink("addButton") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                // show inline  dialog with squad edit form.
                toggleBehavior.show(target);
            }
        };
        add(addButton);

        Panel addPlayerPanel = new AddPlayerPanel(model) {
            @Override
            protected void onClose(AjaxRequestTarget target) {
                toggleBehavior.hide(target);
            }
        };
        toggleBehavior = new AjaxSlideToggleBehavior() {
            @Override
            public void onHide(AjaxRequestTarget target) {
                target.prependJavaScript(JQuery.fadeIn(addButton, "slow"));
            }
        };
        addPlayerPanel.add(toggleBehavior);
        add(addPlayerPanel);
    }
}
