package net.prismeon.prismeonserver

fun main(args: Array<String>) {
    /**
    This is the point that server will start,
    here we will initalize PrismeonServer and use arguments for some settings
     */

    // TODO use arguments
    PrismeonServer.instance = PrismeonServer()
    PrismeonServer.instance!!.start()
}

