package de.flower.rmt.service.mail;

import de.flower.rmt.model.db.entity.event.Event;
import de.flower.rmt.model.dto.Notification;
import de.flower.rmt.test.AbstractRMTIntegrationTests;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.util.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author flowerrrr
 */
// use real mailsender (by defaul all unit-tests use mock-mailsender)
@ContextConfiguration(classes = {MailServiceTest.Config.class})
public class MailServiceTest extends AbstractRMTIntegrationTests {

    @Configuration
    @ImportResource({"/de/flower/rmt/service/mail/mailServiceTest.xml"})
    public static class Config {

    }

    /**
     * Autowiring will ensure that real mailSender is used.
     */
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${mail.default.undisclosed-recipients}")
    private String undisclosedRecipient;

    @BeforeMethod
    public void setUp() {
        log.info("Tests require local smtp server.");
        securityService.getUser().setEmail("no-reply@mailinator.com");
    }

    /**
     * Sends a mail.
     */
    @Test
    public void testSendMail() {
        String mailAddress = "foo.bar@mailinator.com";
        String body = "This mail is generated by " + this.getClass().getName();
        body += "\nSome umlaute: äöüÄÖÜß\n";
        securityService.getUser().setEmail("no-reply@flower.de");
        mailService.sendMail(mailAddress, securityService.getUser().getEmail(), "Unit test generated mail", body);
        // test empty bcc as we had errors with initial implementation
        mailService.sendMail(mailAddress, null, "Unit test generated mail", body);
    }

    @Test
    public void testMassMail() throws IOException {
        Notification notification = new Notification();
        notification.setBccMySelf(true);
        notification.setSubject("Unit test generated mail.");

        String body = "This mail is generated by " + this.getClass().getName();
        body += "\nSome umlaute: äöüÄÖÜß\n";
        notification.setBody(body);

        for (int i = 0; i < 20; i++) {
            String prefix = "das-tool-test-" + i;
            notification.addRecipient(prefix + "@mailinator.com", RandomStringUtils.random(20));
            Notification.Attachment attachment = new Notification.Attachment();
            attachment.name = "attachment-" + i;
            attachment.contentType = ICalendarHelper.CONTENT_TYPE_MAIL;
            InputStream is = this.getClass().getResourceAsStream("MailServiceTest.ics.vm");
            String text = IOUtils.toString(is);
            attachment.data = ICalendarHelper.getBytes(text);
            notification.setAttachment(attachment);
        }
        mailService.sendMassMail(notification);
    }

    /**
     * Test uses real smtp server.
     * Set to enabled=false before committing.
     */
    @Test(enabled = false)
    public void testICalendarAttachment() {
        mailSender.setHost("mail.flower.de");
        Event event = testData.createEvent();
        Notification notification = notificationService.newEventNotification(event);
        String address = "oliver@flower.de";
        notification.addRecipient(address, null);
        mailService.sendMassMail(notification);
        log.info("Check postbox at [{}] for email.", address);
    }

    /**
     * Test uses real smtp server.
     * Set to enabled=false before committing.
     */
    @Test(enabled = false)
    public void testSpam() {
        mailSender.setHost("mail.flower.de");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("oliver@flower.de");
        message.setReplyTo("oliver@flower.de");
        message.setTo("oliver@flower.de");
        message.setSubject("Nur ein kleiner Unit test");
        message.setText("Test test, was man so kennt. Grüße, Wiedersehen.");
        mailService.sendMail(message);
        log.info("Check postbox at [{}] for email.");
    }
}
