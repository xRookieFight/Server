package net.prismeon.server

data class ServerVersion(val marjor : Int = 0, val minor : Int = 0, val patch : Int = 0, val development : Boolean = true){

    fun getString() : String {
        return "${marjor}.${minor}.${patch}"
    }

    fun isDevelopment() : Boolean {
        return development
    }
}
