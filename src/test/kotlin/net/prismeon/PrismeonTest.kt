package net.prismeon

import net.prismeon.prismeonserver.PrismeonServer
import org.junit.Test
import kotlin.test.assertEquals

class PrismeonTest {
    fun a(): Unit {
        PrismeonServer.instance!!.start()
    }
}
