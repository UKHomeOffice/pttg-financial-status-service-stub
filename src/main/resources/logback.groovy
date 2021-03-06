import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.status.OnConsoleStatusListener

import static ch.qos.logback.classic.Level.*

// Add a status listener to record the state of the logback configuration when the logging system is initialised.
statusListener(OnConsoleStatusListener)

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}


// Define logging levels for specific packages
logger("org.eclipse.jetty", INFO)
logger("org.mongodb.driver.cluster", INFO)
logger("org.springframework", INFO)

root(DEBUG, ["STDOUT"])

// Check config file every 30 seconds and reload if changed
scan("30 seconds")