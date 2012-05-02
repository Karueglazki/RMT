package de.flower.common.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * No real test. Just helps debugging into the appender code.
 *
 * @author flowerrrr
 */
public class SMTPEvaluatorTest {

    private final static Logger log = LoggerFactory.getLogger(SMTPEvaluatorTest.class);

    /**
     * Initialization code from http://logback.qos.ch/manual/joran.html.
     */
    @BeforeClass
    public static void setUp() {

        InputStream logfile = SMTPEvaluatorTest.class.getResourceAsStream("logback.xml");

        // assume SLF4J is bound to logback in the current environment
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            // the context was probably already configured by default configuration
            // rules
            lc.reset();
            configurator.doConfigure(logfile);
        } catch (JoranException je) {
            je.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    }

    @Test
    public void testIdleTime() throws InterruptedException {
        log.error("Trigger mail");
        log.error("Don't trigger");
        log.error("Don't trigger");

        // give thread time to sent mail.
        // Thread.sleep(1000);
    }

    @Test
    public void testExcludeFilter() {
        log.error("Illegal argument on static metamodel field injection : de.flower.rmt.model.event.AbstractSoccerEvent_#surfaceList; expected type :  org.hibernate.ejb.metamodel.SingularAttributeImpl; encountered type : javax.persistence.metamodel.ListAttribute");
        log.error("Send email");
    }
}
