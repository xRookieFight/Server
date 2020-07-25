package net.prismeon.prismeonserver

import jline.TerminalFactory
import jline.UnsupportedTerminal
import joptsimple.OptionException
import joptsimple.OptionParser
import joptsimple.OptionSet
import org.fusesource.jansi.AnsiConsole
import java.io.File
import java.io.IOException
import java.lang.management.ManagementFactory
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

fun main(args: Array<String>) {
    /**
     * This is the point that server will start,
     * here we will initalize PrismeonServer and use arguments for some settings
     */
    // Todo: Installation script
    PrismeonServer() // force init class

    PrismeonServer.instance.startTimestamp = System.nanoTime()

    val parser: OptionParser = object : OptionParser() {
        init {
            acceptsAll(Arrays.asList("P", "plugins"), "Plugin directory to use")
                    .withRequiredArg()
                    .ofType(File::class.java)
                    .defaultsTo(File("plugins"))
                    .describedAs("Plugin directory")
            acceptsAll(Arrays.asList("h", "host", "server-ip"), "Host to listen on")
                    .withRequiredArg()
                    .ofType(String::class.javaObjectType)
                    .describedAs("Hostname or IP")
            acceptsAll(Arrays.asList("W", "world-dir", "universe", "world-container"), "World container")
                    .withRequiredArg()
                    .ofType(File::class.java)
                    .describedAs("Directory containing worlds")
            acceptsAll(Arrays.asList("w", "world", "level-name"), "World name")
                    .withRequiredArg()
                    .ofType(String::class.javaObjectType)
                    .describedAs("World name")
            acceptsAll(Arrays.asList("p", "port", "server-port"), "Port to listen on")
                    .withRequiredArg()
                    .ofType(Int::class.javaObjectType)
                    .describedAs("Port")
            acceptsAll(Arrays.asList("o", "online-mode"), "Whether to use online authentication")
                    .withRequiredArg()
                    .ofType(Boolean::class.javaObjectType)
                    .describedAs("Authentication")
            acceptsAll(Arrays.asList("s", "size", "max-players"), "Maximum amount of players")
                    .withRequiredArg()
                    .ofType(Int::class.javaObjectType)
                    .describedAs("Server size")
            acceptsAll(Arrays.asList("nojline"), "Disables jline and emulates the vanilla console")
            acceptsAll(Arrays.asList("noconsole"), "Disables the console")
            acceptsAll(Arrays.asList("v", "version"), "Show the CraftBukkit Version")
        }
    }

    var options: OptionSet? = null

    try {
        options = parser.parse(*args)
    } catch (ex: OptionException) {
        Logger.getLogger("Prismeon").log(Level.SEVERE, ex.localizedMessage)
    }

    if (options == null || options.has("?")) {
        try {
            parser.printHelpOn(System.out)
        } catch (ex: IOException) {
            Logger.getLogger("Prismeon").log(Level.SEVERE, null, ex)
        }
    } else if (options.has("v")) {
        println(PrismeonServer::class.java.getPackage().implementationVersion)
    } else {
        val path = File(".").absolutePath
        if (path.contains("!") || path.contains("+")) {
            System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.")
            return
        }
        try {
            val jline_UnsupportedTerminal = String(charArrayOf('j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l'))
            val jline_terminal = String(charArrayOf('j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l'))
            PrismeonServer.instance.useJline = jline_UnsupportedTerminal != System.getProperty(jline_terminal)
            if (options.has("nojline")) {
                System.setProperty("user.language", "en")
                PrismeonServer.instance.useJline = false
            }
            if (PrismeonServer.instance.useJline) {
                AnsiConsole.systemInstall()
            } else {
                // This ensures the terminal literal will always match the jline implementation
                System.setProperty(TerminalFactory.JLINE_TERMINAL, UnsupportedTerminal::class.java.name)
            }
            if (options.has("noconsole")) {
                PrismeonServer.instance.useConsole = false
            }

            var maxPermGen = 0 // Kb
            for (s in ManagementFactory.getRuntimeMXBean().inputArguments) {
                if (s.startsWith("-XX:MaxPermSize")) {
                    maxPermGen = s.replace("[^\\d]".toRegex(), "").toInt()
                    maxPermGen = maxPermGen shl 10 * "kmg".indexOf(Character.toLowerCase(s[s.length - 1]))
                }
            }
            if (System.getProperty("java.class.version").toFloat() < 52 && maxPermGen < 128 shl 10) {
                println("Warning, your max perm gen size is not set or less than 128mb. It is recommended you restart Java with the following argument: -XX:MaxPermSize=128M")
            }

            println("Starting server...")
            PrismeonServer.instance.start()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}

