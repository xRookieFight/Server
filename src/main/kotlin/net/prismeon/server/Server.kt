package net.prismeon.server

import net.prismeon.Prismeon
import net.prismeon.network.Network
import net.prismeon.interfaces.Setupable
import net.prismeon.interfaces.Startable

class Server : Startable, Setupable {

    companion object {
        val instance : Server = Server()
        val network : Network = Network()
    }

    override fun start(){
        println("Starting Prismeon Server v${Prismeon.version.getString()}")
    }

    override fun setup(){
    }

    override fun stop(){
    }
}