package net.prismeon.prismeonserver.log

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import java.io.ByteArrayOutputStream
import java.io.IOException

class LoggerOutputStream(private val logger: Logger, private val level: Level) :
    ByteArrayOutputStream() {
    private val separator = System.getProperty("line.separator")

    @Throws(IOException::class)
    override fun flush() {
        synchronized(this) {
            super.flush()
            val record = this.toString()
            super.reset()
            if (record.length > 0 && record != separator) {
                logger.log(level, record)
            }
        }
    }

}