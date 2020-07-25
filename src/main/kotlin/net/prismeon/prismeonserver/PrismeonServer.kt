package net.prismeon.prismeonserver

class PrismeonServer {
    companion object {
        var instance: PrismeonServer? = null
            set(value){
                if (field != null) throw UnsupportedOperationException("Cannot init server twice!")
                field = value
            }
    }

    var running = true
    var stopped = false

    fun start() {
        // Initalize
        try {
            while (running){
                // Server Loop
            }
        } catch (exception: Exception){
            // TODO Saving Worlds/Players, Disabling Plugins.
        } finally {
            stopped = true
        }
    }
}