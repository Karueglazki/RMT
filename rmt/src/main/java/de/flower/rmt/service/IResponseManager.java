package de.flower.rmt.service;

import de.flower.rmt.model.Player;
import de.flower.rmt.model.RSVPStatus;
import de.flower.rmt.model.Response;
import de.flower.rmt.model.User;
import de.flower.rmt.model.event.Event;

import java.util.List;

/**
 * @author flowerrrr
 */
public interface IResponseManager {

    Response findById(Long id);

    List<Response> findByEventAndStatus(Event event, RSVPStatus rsvpStatus);

    Response findByEventAndUser(Event event, User user);

    Response respond(Event event, Player player, RSVPStatus status, String comment);

    Response respond(Event event, RSVPStatus status, String comment);
}