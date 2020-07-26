package net.prismeon

import net.prismeon.server.Server
import net.prismeon.server.Version

class Prismeon {

    companion object {
        val version : Version = Version(1, 0, 0, true)

        @JvmStatic
        fun main(args : Array<String>) {
            Server.instance.start()
        }
    }
}
