package net.prismeon.prismeonserver

class PrismeonServer {
    companion object {
        var instance: PrismeonServer? = null
            set(value){
                if (field != null){
                    throw UnsupportedOperationException("Cannot init server twice!")
                }
                field = value
            }
    }
}