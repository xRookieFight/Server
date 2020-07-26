package old.prismeonserver.log

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.LogRecord

class ForwardLogHandler : ConsoleHandler() {
    private val cachedLoggers: MutableMap<String, Logger?> =
        ConcurrentHashMap()

    private fun getLogger(name: String): Logger? {
        var logger = cachedLoggers[name]
        if (logger == null) {
            logger = LogManager.getLogger(name)
            cachedLoggers[name] = logger
        }
        return logger
    }

    override fun publish(record: LogRecord) {
        val logger = getLogger(record.loggerName.toString()) // See SPIGOT-1230
        val exception = record.thrown
        val level = record.level
        val message = formatter.formatMessage(record)
        if (level === Level.SEVERE) {
            logger!!.error(message, exception)
        } else if (level === Level.WARNING) {
            logger!!.warn(message, exception)
        } else if (level === Level.INFO) {
            logger!!.info(message, exception)
        } else if (level === Level.CONFIG) {
            logger!!.debug(message, exception)
        } else {
            logger!!.trace(message, exception)
        }
    }

    override fun flush() {}

    @Throws(SecurityException::class)
    override fun close() {
    }
}