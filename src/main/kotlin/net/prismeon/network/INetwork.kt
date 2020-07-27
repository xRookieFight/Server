package net.prismeon.network

import net.prismeon.interfaces.Startable

interface INetwork : Startable {

    override fun start()
    override fun stop()
}
