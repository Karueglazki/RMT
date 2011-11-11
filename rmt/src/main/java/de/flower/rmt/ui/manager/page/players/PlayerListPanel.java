package de.flower.rmt.ui.manager.page.players;

import de.flower.common.ui.ajax.AjaxLinkWithConfirmation;
import de.flower.common.ui.ajax.updatebehavior.AjaxRespondListener;
import de.flower.common.ui.ajax.updatebehavior.AjaxUpdateBehavior;
import de.flower.common.ui.ajax.updatebehavior.events.AjaxEvent;
import de.flower.rmt.model.User;
import de.flower.rmt.model.User_;
import de.flower.rmt.service.IUserManager;
import de.flower.rmt.service.security.ISecurityService;
import de.flower.rmt.ui.common.panel.BasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author flowerrrr
 */
public class PlayerListPanel extends BasePanel {

    @SpringBean
    private IUserManager playerManager;

    @SpringBean
    private ISecurityService securityService;

    public PlayerListPanel() {
        super();

        WebMarkupContainer playerListContainer = new WebMarkupContainer("listContainer");
        add(playerListContainer);
        playerListContainer.add(new ListView<User>("playerList", getPlayerListModel()) {


            @Override
            protected void populateItem(final ListItem<User> item) {
                User player = item.getModelObject();
                item.add(new Label("fullname", player.getFullname()));
                item.add(new Label("email", player.getEmail()));
                Component manager;
                item.add(manager = new WebMarkupContainer("manager"));
                manager.setVisible(player.isManager());
                item.add(new Link("editButton") {

                    @Override
                    public void onClick() {
                        onEdit(null, item.getModel());
                    }
                });
                AjaxLinkWithConfirmation deleteButton;
                item.add(deleteButton = new AjaxLinkWithConfirmation("deleteButton", new ResourceModel("manager.players.delete.confirm")) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        playerManager.delete(item.getModelObject());
                        target.registerRespondListener(new AjaxRespondListener(AjaxEvent.EntityDeleted(User.class)));
                    }

                });
                deleteButton.setVisible(!securityService.isCurrentUser(player));
            }
        });
        playerListContainer.add(new AjaxUpdateBehavior(AjaxEvent.EntityAll(User.class)));
    }

    /**
     * Implement this method if you want to respond to a click on the edit button.
     */
    protected void onEdit(AjaxRequestTarget target, IModel<User> model) {
        throw new UnsupportedOperationException("Subclasses must override this method!");
    }

    private IModel<List<User>> getPlayerListModel() {
        return new LoadableDetachableModel<List<User>>() {
            @Override
            protected List<User> load() {
                return playerManager.findAll(User_.roles);
            }
        };
    }

}