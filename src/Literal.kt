data class Literal private constructor(val negated: Boolean, val predicate: Predicate) {
    constructor(string: String, clauseId: Int) : this(string.startsWith("-"), Predicate(string.dropWhile { it == '-' }, clauseId))
    constructor(literal: Literal) : this(literal.negated, Predicate(literal.predicate))

    override fun toString() = "${(if (negated) "-" else "")}$predicate"
}
