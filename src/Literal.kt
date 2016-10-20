data class Literal private constructor(val negated: Boolean, val predicate: Predicate) {
    constructor(string: String, clauseId: Int) : this(string.startsWith("-"), Predicate(string.dropWhile { it == '-' }, clauseId))

    override fun toString() = "${(if (negated) "-" else "")}$predicate"

    fun copy() = Literal(negated, predicate.copy())

    fun closeEnough(literal:Literal):Boolean {
        return negated === literal.negated && predicate.closeEnough(literal.predicate)
    }
}

