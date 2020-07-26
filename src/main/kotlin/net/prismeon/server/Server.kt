package net.prismeon.server

import net.prismeon.Prismeon

class Server {

    companion object {

        val instance : Server = Server()
    }

    fun start(){
        println("Starting Prismeon Server v${Prismeon.version.getString()}")
    }

    fun setup(){
    }

    fun stop(){
    }
}