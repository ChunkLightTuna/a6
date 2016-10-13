data class Clause private constructor(val literals: List<Literal>) {

    constructor(string: String) : this(generate(string))

    companion object {
        var ticker = 0
        fun generate(string: String): List<Literal> {
            ticker++
            val list = string.split("|").map { Literal(it, ticker) }
            return list
        }
    }

    override fun toString(): String {

        val sb = StringBuilder()


        literals.mapIndexed { i, literal ->
            when (i) {
                literals.size - 1 -> literal.toString()
                else -> "$literal | "
            }
        }.forEach { sb.append(it) }

        return sb.toString()
    }

    fun update(old: Term, new: Term) {
        literals.forEach { it.predicate.update(old, new) }
    }
}

