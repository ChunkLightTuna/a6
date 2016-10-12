data class Literal private constructor(val negated: Boolean, val predicate: Predicate) {
    constructor(string: String, clauseId: Int) :
    this(string.startsWith("-"), Predicate(string.dropWhile { it == '-' }, clauseId))

    override fun toString(): String {
        return "${(if (negated) "-" else "")}$predicate"
    }
}