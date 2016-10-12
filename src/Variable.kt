data class Variable private constructor(val name: String, private val original: String) {
    constructor(string: String, clauseId: Int) : this("$string$clauseId", string)

    override fun toString(): String {
        return original
    }
}