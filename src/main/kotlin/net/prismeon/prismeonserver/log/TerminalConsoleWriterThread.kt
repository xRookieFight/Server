package net.prismeon.prismeonserver.log

import jline.console.ConsoleReader
import net.prismeon.prismeonserver.PrismeonServer.Companion.instance
import java.io.IOException
import java.io.OutputStream
import java.util.logging.Level
import java.util.logging.Logger

class TerminalConsoleWriterThread(private val output: OutputStream, private val reader: ConsoleReader) :
    Runnable {
    override fun run() {
        var message: String?
        while (true) {
            message = QueueLogAppender.getNextLogEvent("TerminalConsole")
            if (message == null) {
                continue
            }
            try {
                if (instance.useJline) {
                    reader.print(ConsoleReader.RESET_LINE.toString() + "")
                    reader.flush()
                    output.write(message.toByteArray())
                    output.flush()
                    try {
                        reader.drawLine()
                    } catch (ex: Throwable) {
                        reader.cursorBuffer.clear()
                    }
                    reader.flush()
                } else {
                    output.write(message.toByteArray())
                    output.flush()
                }
            } catch (ex: IOException) {
                Logger.getLogger(TerminalConsoleWriterThread::class.java.name)
                    .log(Level.SEVERE, null, ex)
            }
        }
    }

}