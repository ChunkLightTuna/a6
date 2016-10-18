data class TermList private constructor(val terms: Array<Term>) {
    constructor(string: String, clauseId: Int) : this(generate(string).map {
        when {
            it.endsWith(')') -> Function(it, clauseId)
            it[0].isLowerCase() -> Variable(it, clauseId)
            else -> Constant(it)
        }
    }.toTypedArray())

    override fun toString(): String {
        val sb = StringBuilder()
        terms.forEachIndexed { i, term ->
            if (i == terms.size - 1) sb.append(term)
            else sb.append("$term, ")
        }
        return sb.toString()
    }

    private companion object {
        fun generate(string: String): List<String> {
            var parenCount = 0

            string.forEachIndexed { i, c ->
                assert(parenCount >= 0)
                when (c) {
                    '(' -> parenCount++
                    ')' -> parenCount--
                    ',' -> when (parenCount) {
                        0 -> {
                            return (listOf(string.take(i)) + generate(string.drop(i + 1)))
                        }
                    }
                }
            }

            return listOf(string) //reached the end
        }
    }

//    fun replace(t: Term, v: Variable): TermList {
//        return TermList(terms.map {
//            when (it) {
//                v -> t
//                else -> v
//            }
//        }.toTypedArray())
//    }
//
//    fun firstVariable(): Variable? {
//        terms.forEach {
//            if (it is Variable)
//                return it
//        }
//        return null
//    }

    override fun equals(other: Any?): Boolean {
        if (other !is TermList)
            return false

        terms.forEachIndexed { i, term ->
            if (terms[i] != other.terms[i]) return false
        }

        return true
    }

    override fun hashCode() = terms.map(Term::hashCode).reduce { total, next -> total * next }

    fun contains(v: Variable): Boolean {
        terms.forEach {
            if (it.contains(v))
                return true
        }
        return false
    }

    fun copy() = TermList(terms.map(Term::copy).toTypedArray())
}
