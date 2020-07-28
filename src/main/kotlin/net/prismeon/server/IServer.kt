package net.prismeon.server

import net.prismeon.interfaces.Setupable
import net.prismeon.interfaces.Startable

interface IServer : Startable, Setupable {

    override fun start()
    override fun setup()
    override fun stop()
}