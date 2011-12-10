package de.flower.rmt.ui.manager.component;

import de.flower.rmt.model.Venue;
import de.flower.rmt.service.IVenueManager;
import de.flower.rmt.ui.common.form.field.DropDownChoicePanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author flowerrrr
 */
public class VenueDropDownChoicePanel extends DropDownChoicePanel<Venue> {

    @SpringBean
    private IVenueManager venueManager;

    public VenueDropDownChoicePanel(String id) {
        super(id);
        setChoices(getVenueChoices());
        setChoiceRenderer(new IChoiceRenderer<Venue>() {
            @Override
            public Object getDisplayValue(Venue venue) {
                return venue.getName();
            }

            @Override
            public String getIdValue(Venue venue, int index) {
                return venue.getId().toString();
            }
        });
    }

    private IModel<List<Venue>> getVenueChoices() {
        return new LoadableDetachableModel<List<Venue>>() {
            @Override
            protected List<Venue> load() {
                return venueManager.findAll();
            }
        };
    }
}