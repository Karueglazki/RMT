package de.flower.rmt.ui.manager.page.player;

import de.flower.common.ui.ajax.updatebehavior.AjaxRespondListener;
import de.flower.common.ui.ajax.updatebehavior.events.AjaxEvent;
import de.flower.rmt.model.User;
import de.flower.rmt.service.IRoleManager;
import de.flower.rmt.service.IUserManager;
import de.flower.rmt.ui.common.form.EntityForm;
import de.flower.rmt.ui.common.form.field.CheckBoxPanel;
import de.flower.rmt.ui.common.form.field.FormFieldPanel;
import de.flower.rmt.ui.common.form.field.TextFieldPanel;
import de.flower.rmt.ui.common.panel.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.jsr303.FormComponentBeanValidator;

/**
 * @author flowerrrr
 */
public class PlayerGeneralPanel extends BasePanel<User> {

    @SpringBean
    private IUserManager userManager;

    @SpringBean
    private IRoleManager roleManager;

    private IModel<Boolean> managerModel;

    public PlayerGeneralPanel(String id, IModel<User> model) {
        super(id, model);

        managerModel = getManagerModel(model);
        EntityForm<User> form = new EntityForm<User>("form", model) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<User> form) {
                User user = form.getModelObject();
                userManager.save(user, managerModel.getObject());
                target.registerRespondListener(new AjaxRespondListener(AjaxEvent.EntityCreated(User.class), AjaxEvent.EntityUpdated(User.class)));
            }
        };
        add(form);

        TextFieldPanel fullname = new TextFieldPanel("fullname");
        form.add(fullname);
        fullname.addValidator(new FormComponentBeanValidator(User.Validation.INameUnique.class));
        TextFieldPanel email = new TextFieldPanel("email");
        form.add(email);
        email.addValidator(new FormComponentBeanValidator(User.Validation.IEmailUnique.class));
        CheckBoxPanel manager = new CheckBoxPanel("manager", new CheckBox(FormFieldPanel.ID, managerModel));
        manager.setVisible(!securityService.isCurrentUser(model.getObject()));
        form.add(manager);
    }

    private Model<Boolean> getManagerModel(final IModel<User> model) {
        User user = model.getObject();
        if (user.isNew()) {
            return Model.of(false);
        } else {
            return Model.of(roleManager.isManager(user.getId()));
        }
    }
}