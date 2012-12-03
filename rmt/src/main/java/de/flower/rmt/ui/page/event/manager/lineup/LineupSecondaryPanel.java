package de.flower.rmt.ui.page.event.manager.lineup;

import de.flower.common.ui.ajax.event.AjaxEventListener;
import de.flower.common.ui.panel.BasePanel;
import de.flower.rmt.model.db.entity.Invitation;
import de.flower.rmt.model.db.entity.Lineup;
import de.flower.rmt.model.db.entity.LineupItem;
import de.flower.rmt.model.db.entity.event.Event;
import de.flower.rmt.service.ILineupManager;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author flowerrrr
 */
public class LineupSecondaryPanel extends BasePanel {

    public LineupSecondaryPanel(final IModel<Event> model) {

        // treat subpanels as top level secondary panels to have spacer between them
        setRenderBodyOnly(true);

        add(new LineupPublishPanel(model));

        add(new LineupInviteeListPanel(model));
    }

    public static class LineupPublishPanel extends PublishPanel<Event> {

        @SpringBean
        private ILineupManager lineupManager;

        private IModel<Lineup> lineupModel;

        public LineupPublishPanel(final IModel<Event> model) {
            super(model);
            lineupModel = new LoadableDetachableModel<Lineup>() {
                @Override
                protected Lineup load() {
                    return lineupManager.findOrCreateLineup(model.getObject());
                }
            };
        }

        @Override
        protected void publish(final AjaxRequestTarget target) {
            lineupManager.publishLineup(getModel().getObject());
            lineupModel.detach();
        }

        @Override
        protected boolean isPublished() {
            return lineupModel.getObject().isPublished();
        }
    }

    public static class LineupInviteeListPanel extends DraggableInviteeListPanel {

        @SpringBean
        private ILineupManager lineupManager;

        // used to filter out those players that are already dragged to the lineup-grid.
        private final IModel<List<Invitation>> lineupItemListModel;

        public LineupInviteeListPanel(final IModel<Event> model) {
            super(model);
            add(new AjaxEventListener(LineupItem.class));

            lineupItemListModel = new LoadableDetachableModel<List<Invitation>>() {
                @Override
                protected List<Invitation> load() {
                    return lineupManager.findInvitationsInLinuep(model.getObject());
                }
            };
        }

        @Override
        protected boolean isDraggablePlayerVisible(final Invitation invitation) {
            return lineupItemListModel.getObject().contains(invitation);
        }

        @Override
        protected void onDetach() {
            super.onDetach();
            if (lineupItemListModel != null) {
                lineupItemListModel.detach();
            }
        }
    }
}
