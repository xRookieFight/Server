package net.prismeon

import net.prismeon.server.Server
import net.prismeon.server.ServerVersion

object Prismeon {

    val version : ServerVersion = ServerVersion(1, 0, 0, true)

    @JvmStatic
    fun main(args : Array<String>) {
        Server.instance.start()
    }
}
