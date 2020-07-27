package net.prismeon.network

import net.prismeon.interfaces.Startable

class Network : Startable {

    companion object {

        val info : NetworkInfo = NetworkInfo(407, "1.16.1")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}