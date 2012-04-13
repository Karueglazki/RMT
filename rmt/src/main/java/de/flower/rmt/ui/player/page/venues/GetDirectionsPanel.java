package de.flower.rmt.ui.player.page.venues;

import de.flower.rmt.model.Venue;
import de.flower.rmt.ui.app.Links;
import de.flower.rmt.ui.common.panel.BasePanel;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;

/**
 * @author flowerrrr
 */
public class GetDirectionsPanel extends BasePanel {

    public GetDirectionsPanel(final IModel<Venue> model) {
        add(new ExternalLink("link", Links.getDirectionsLink(model.getObject())));
    }

}
