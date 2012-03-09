package de.flower.rmt.ui.manager.page.opponents;

import de.flower.common.ui.ajax.AjaxLinkWithConfirmation;
import de.flower.common.ui.ajax.updatebehavior.AjaxRespondListener;
import de.flower.common.ui.ajax.updatebehavior.AjaxUpdateBehavior;
import de.flower.common.ui.ajax.updatebehavior.events.AjaxEvent;
import de.flower.rmt.model.Opponent;
import de.flower.rmt.service.IOpponentManager;
import de.flower.rmt.ui.common.panel.BasePanel;
import de.flower.rmt.ui.common.panel.DropDownMenuPanel;
import de.flower.rmt.ui.model.OpponentModel;
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
public class OpponentListPanel extends BasePanel {

    @SpringBean
    private IOpponentManager opponentManager;

    public OpponentListPanel() {

        final IModel<List<Opponent>> listModel = getListModel();
        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        add(listContainer);
        listContainer.add(new WebMarkupContainer("noEntry") {
            @Override
            public boolean isVisible() {
                return listModel.getObject().isEmpty();
            }
        });
        listContainer.add(new ListView<Opponent>("list", listModel) {
            @Override
            public boolean isVisible() {
                return !getList().isEmpty();
            }

            @Override
            protected void populateItem(final ListItem<Opponent> item) {
                Link editLink = createEditLink("editLink", item);
                editLink.add(new Label("name", item.getModelObject().getName()));
                item.add(editLink);
                DropDownMenuPanel menuPanel = new DropDownMenuPanel();
                item.add(menuPanel);
                menuPanel.addLink(createEditLink("link", item), "button.edit");
                menuPanel.addLink(new AjaxLinkWithConfirmation("link", new ResourceModel("manager.opponent.delete.confirm")) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        opponentManager.delete(item.getModelObject());
                        target.registerRespondListener(new AjaxRespondListener(AjaxEvent.EntityDeleted(Opponent.class)));
                    }
                }, "button.delete");
            }
        });
        listContainer.add(new AjaxUpdateBehavior(AjaxEvent.EntityAll(Opponent.class)));
    }

    private IModel<List<Opponent>> getListModel() {
        return new LoadableDetachableModel<List<Opponent>>() {
            @Override
            protected List<Opponent> load() {
                return opponentManager.findAll();
            }
        };
    }

    private Link createEditLink(String id, final ListItem<Opponent> item) {
        return new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(new OpponentEditPage(new OpponentModel(item.getModel())));
            }
        };
    }
}
