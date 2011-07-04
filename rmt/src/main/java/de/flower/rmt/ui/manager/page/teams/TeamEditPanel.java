package de.flower.rmt.ui.manager.page.teams;

import de.flower.common.ui.FormMode;
import de.flower.common.ui.ajax.updatebehavior.AjaxRespondListener;
import de.flower.common.ui.ajax.updatebehavior.events.Event;
import de.flower.common.ui.form.MyForm;
import de.flower.common.ui.form.ValidatedTextField;
import de.flower.common.validation.unique.Unique;
import de.flower.rmt.model.Team;
import de.flower.rmt.model.Users;
import de.flower.rmt.service.ITeamManager;
import de.flower.rmt.ui.app.RMTSession;
import de.flower.rmt.ui.common.panel.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.jsr303.BeanValidator;
import org.wicketstuff.jsr303.ConstraintFilter;
import org.wicketstuff.jsr303.WicketBeanValidator;

/**
 * @author oblume
 */
public class TeamEditPanel extends BasePanel {

    private FormMode mode;

    private Form<Team> form;

    @SpringBean
    private ITeamManager teamManager;

    public TeamEditPanel(String id) {
        super(id);

        form = new MyForm<Team>("form", new Team());
        add(form);

        ValidatedTextField name;
        form.add(name = new ValidatedTextField("name"));
        name.add(new WicketBeanValidator(Unique.class, new ConstraintFilter("{de.flower.validation.constraints.unique.message.uc_name}")));
        form.add(new ValidatedTextField("url"));

        AjaxSubmitLink submitLink;
        form.add(submitLink = new AjaxSubmitLink("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!new BeanValidator(form).isValid(form.getModelObject())) {
                    onError(target, form);
                } else {
                    teamManager.save((Team) form.getModelObject());
                    target.registerRespondListener(new AjaxRespondListener(Event.EntityCreated(Team.class), Event.EntityUpdated(Team.class)));
                    // target.add(form);
                    ModalWindow.closeCurrent(target);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(form);
            }
        });
        // submitLink.add()
    }

    public void init(IModel<Team> model) {
        if (model == null) {
            Users user = RMTSession.get().getUser();
            model = Model.of(new Team(user.getClub()));
        }
        form.setModel(new CompoundPropertyModel<Team>(model));
        // clear css marker of previous validations

    }
}
