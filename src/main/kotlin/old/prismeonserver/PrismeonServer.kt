package old.prismeonserver

import jline.console.ConsoleReader
import old.prismeonserver.log.ForwardLogHandler
import old.prismeonserver.log.LoggerOutputStream
import old.prismeonserver.log.TerminalConsoleWriterThread
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.appender.ConsoleAppender
import java.io.IOException
import java.io.PrintStream

class PrismeonServer : Runnable {
    companion object {
        val LOGGER: Logger = LogManager.getLogger()
        val instance: PrismeonServer
        init {
            instance = PrismeonServer()
        }
    }

    var running = true
    var stopped = false
    var useJline = false
    var useConsole = false
    var startTimestamp = System.nanoTime()
    lateinit var reader: ConsoleReader
    var serverThread: Thread

    init {
        if (System.console() == null && System.getProperty("jline.terminal") == null) {
            System.setProperty("jline.terminal", "jline.UnsupportedTerminal")
            useJline = false
        }

        try {
            reader = ConsoleReader(System.`in`, System.out)
            reader.expandEvents = false
        } catch (e: Throwable) {
            try {
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal")
                System.setProperty("user.language", "en")
                useJline = false
                reader = ConsoleReader(System.`in`, System.out)
                reader.expandEvents = false
            } catch (ex: IOException) {
                LOGGER.warn(null as String?, ex)
            }
        }
        // TODO Shutdown Thread
        //Runtime.getRuntime().addShutdownHook(ServerShutdownThread(this))

        this.serverThread = Thread(this, "Server thread")
        val thread: Thread = object : Thread("Server console handler") {
            override fun run() {
                if (!useConsole) {
                    return
                }

                val bufferedreader: ConsoleReader = reader
                var s: String?
                try {
                    while (!stopped && running) {
                        s = if (useJline) {
                            bufferedreader.readLine(">", null)
                        } else {
                            bufferedreader.readLine()
                        }
                        if (s != null && s.trim { it <= ' ' }.length > 0) {
                            // Todo executing commands
                        }
                    }
                } catch (ioexception: IOException) {
                    LOGGER.error("Exception handling console input", ioexception)
                }
            }
        }

        val global = java.util.logging.Logger.getLogger("")
        global.useParentHandlers = false
        for (handler in global.handlers) {
            global.removeHandler(handler)
        }
        global.addHandler(ForwardLogHandler())

        val logger = LogManager.getRootLogger() as org.apache.logging.log4j.core.Logger
        for (appender in logger.appenders.values) {
            if (appender is ConsoleAppender) {
                logger.removeAppender(appender)
            }
        }

        Thread(TerminalConsoleWriterThread(System.out, this.reader)).start()

        System.setOut(PrintStream(LoggerOutputStream(logger, Level.INFO), true))
        System.setErr(PrintStream(LoggerOutputStream(logger, Level.WARN), true))

        thread.isDaemon = true
        thread.start()
        LOGGER.info("Starting minecraft server")
        start()
    }
    fun start() {
        // Initalize
        try {
            while (running){
                // Server Loop
            }
        } catch (exception: Exception){
            // TODO Saving Worlds/Players, Disabling Plugins.
            stop()
        } finally {
            stopped = true
        }
    }
    fun stop() {

    }

    override fun run() {


    }
}