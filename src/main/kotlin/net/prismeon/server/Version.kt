package net.prismeon.server

data class Version(val marjor : Int = 0, val minor : Int = 0, val patch : Int = 0){

    fun getString() : String {
        return "${marjor}.${minor}.${patch}"
    }
}
