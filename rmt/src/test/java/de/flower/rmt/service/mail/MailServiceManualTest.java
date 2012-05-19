package de.flower.rmt.service.mail;

import de.flower.rmt.test.AbstractRMTIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */
// use real mailsender (by defaul all unit-tests use mock-mailsender)
@ContextConfiguration(locations = { "mailServiceTest.xml" } )
public class MailServiceManualTest extends AbstractRMTIntegrationTests {

    private String mailAddress = "das-tool@flower.de";

    /**
     * Autowiring will ensure that real mailSender is used.
     */
    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * Sends a mail to some real address.
     */
    @Test
    public void testSendMail() {
        String body = "This mail is generated by " + this.getClass().getName();
        body += "\nSome umlaute: äöüÄÖÜß\n";
        securityService.getUser().setEmail("no-reply@flower.de");
        mailService.sendMail(mailAddress, securityService.getUser().getEmail(), "Unit test generated mail", body);
        // test empty bcc as we had errors with initial implementation
        mailService.sendMail(mailAddress, null, "Unit test generated mail", body);
        log.info("Check mail at [" + mailAddress + "] to verify mail has been sent.");
    }

}
