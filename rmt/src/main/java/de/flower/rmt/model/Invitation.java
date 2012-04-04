package de.flower.rmt.model;

import de.flower.common.model.AbstractBaseEntity;
import de.flower.rmt.model.event.Event;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author flowerrrr
 */
@Entity
public class Invitation extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{validation.rsvpstatus.notnull}")
    @Index(name = "ix_status")
    private RSVPStatus status = RSVPStatus.NORESPONSE;

    /**
     * Date of response. Might be updated when user changes his status.
     */
    @Column
    private Date date;

    @Column(length = 255)
    @Size(max = 255)
    private String comment;

    @Column(length = 255)
    @Size(max = 255)
    private String managerComment;

    @Column
    @Size(max = 50)
    private String guestName;

    @NotNull
    @Column
    private Boolean invitationSent = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name = "ix_event")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name = "ix_user")
    private User user;

    protected Invitation() {
    }

    public Invitation(Event event, User user) {
        this.user = user;
        this.event = event;
    }

    public Invitation(final Event event, final String guestName) {
        this.event = event;
        this.guestName = guestName;
    }

    public RSVPStatus getStatus() {
        return status;
    }

    public void setStatus(RSVPStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(final String managerComment) {
        this.managerComment = managerComment;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Boolean isInvitationSent() {
        return invitationSent;
    }

    public void setInvitationSent(final Boolean invitationSent) {
        this.invitationSent = invitationSent;
    }

    public String getName() {
        if (user == null) {
            // TODO (flowerrrr - 04.04.12) marking the name should be done in ui!
            return guestName + " (G)";
        } else {
            return user.getFullname();
        }
    }

    public String getEmail() {
        if (user == null) {
            throw new IllegalStateException("Call #isEmail before accessing email-field.");
        } else {
            return user.getEmail();
        }
    }

    public boolean isEmail() {
        return user != null;
    }
}
