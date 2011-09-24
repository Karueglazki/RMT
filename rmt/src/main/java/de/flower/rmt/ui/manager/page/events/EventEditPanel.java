package de.flower.rmt.ui.manager.page.events;

import de.flower.common.ui.ajax.MyAjaxSubmitLink;
import de.flower.common.ui.ajax.updatebehavior.AjaxRespondListener;
import de.flower.common.ui.ajax.updatebehavior.events.AjaxEvent;
import de.flower.common.ui.form.MyForm;
import de.flower.common.ui.form.TimeSelect;
import de.flower.common.ui.form.ValidatedFormComponent;
import de.flower.common.ui.form.ValidatedTextField;
import de.flower.common.util.Check;
import de.flower.rmt.model.event.Event;
import de.flower.rmt.service.IEventManager;
import de.flower.rmt.ui.common.panel.BasePanel;
import de.flower.rmt.ui.manager.component.TeamSelect;
import de.flower.rmt.ui.manager.component.VenueSelect;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.jsr303.BeanValidator;

/**
 * @author oblume
 */
public class EventEditPanel extends BasePanel {

    private Form<Event> form;

    @SpringBean
    private IEventManager eventManager;

    public EventEditPanel(String id, IModel<Event> model) {
        super(id, model);
        Check.notNull(model.getObject());

        form = new MyForm<Event>("form", model.getObject());
        form.setOutputMarkupPlaceholderTag(true);
        add(form);

        form.add(new TeamSelect("team"));

        DateTextField dateField = DateTextField.forDateStyle("date", "S-");
        // dateField.add(new DatePicker());
        form.add(new ValidatedFormComponent(dateField));

        TimeSelect timeField = new TimeSelect("time");
        form.add(new ValidatedFormComponent(timeField));

        VenueSelect venueSelect = new VenueSelect("venue");
        form.add(venueSelect);

        // form.add(surface label)

        form.add(new ValidatedTextField("summary"));

        form.add(new TextArea("comment"));

        // form.add(participants)


        form.add(new MyAjaxSubmitLink("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!new BeanValidator(form).isValid(form.getModelObject())) {
                    onError(target, form);
                } else {
                    eventManager.save((Event) form.getModelObject());
                    target.registerRespondListener(new AjaxRespondListener(AjaxEvent.EntityCreated(Event.class), AjaxEvent.EntityUpdated(Event.class)));
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(form);
            }
        });
    }


}
