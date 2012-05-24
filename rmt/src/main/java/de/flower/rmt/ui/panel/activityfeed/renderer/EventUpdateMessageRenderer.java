package de.flower.rmt.ui.panel.activityfeed.renderer;

import de.flower.rmt.model.db.type.activity.EventUpdateMessage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author flowerrrr
 */
public class EventUpdateMessageRenderer extends AbstractEventMessageRenderer implements IMessageRenderer<EventUpdateMessage> {

    @Override
    public String toString(final EventUpdateMessage message) {
        String key;
        Object[] params = new Object[]{ getEventArticle(message), getEventLink(message), linkProvider.deepLinkEvent(message.getEventId())};
        if (message.getType() == EventUpdateMessage.Type.CREATED) {
            key = "activity.event.create.message";
        } else if (message.getType() == EventUpdateMessage.Type.UPDATED) {
            key = "activity.event.update.message";
        } else if (message.getType() == EventUpdateMessage.Type.CANCELED) {
            key = "activity.event.cancel.message";
        } else {
            throw new RuntimeException("Unkonwn message type [" + message + "].");
        }
        String s = new StringResourceModel(key, Model.of(message), params).getObject();
        return s;
    }

    @Override
    public boolean canHandle(final Object message) {
        return message instanceof EventUpdateMessage;
    }
}
