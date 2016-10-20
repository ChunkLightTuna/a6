data class Clause(val literals: List<Literal>/*, val negatedQuery: Boolean*/) {

    constructor(string: String/*, negatedQuery: Boolean*/) : this(generate(string)/*, negatedQuery*/)

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

    fun containsN(literal: Literal): Boolean {
        literals.forEach {
            if (it.negated !== literal.negated && it.predicate.label == literal.predicate.label)
                return true
        }
        return false
    }

    fun closeEnough(clause: Clause):Boolean {
        if(clause.literals.size != literals.size) {
//            println("${clause.literals}\n $literals\n\n")
            return false
        }
        literals.forEachIndexed { i, literal ->
            if(!literal.closeEnough(clause.literals[i])) {
                return false
            }
        }
        return true
    }

    fun copy() = Clause(literals.map(Literal::copy)/*, negatedQuery*/)
}
