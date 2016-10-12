data class TermList private constructor(val formationTree: MutableList<Term>) {
    constructor(string: String, clauseId: Int) : this(generate(string).map {
        when {
            it.endsWith(')') -> Function(it, clauseId)
            it[0].isLowerCase() -> Variable(it, clauseId)
            else -> Constant(it)
        }
    }.toMutableList())

    override fun toString(): String {
        return formationTree.toString().drop(1).dropLast(1)
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

    fun replace(t: Term, v: Variable): TermList {
        return TermList(formationTree.map {
            when (it) {
                v -> t
                else -> v
            }
        }.toMutableList())
    }

    fun firstVariable(): Variable? {
        formationTree.forEach {
            if (it is Variable)
                return it
        }
        return null
    }

    override fun equals(other: Any?) = other is TermList && formationTree.containsAll(other.formationTree) && other.formationTree.containsAll(formationTree)

    fun contains(v: Variable): Boolean {
        formationTree.forEach {
            if (it.contains(v))
                return true
        }
        return false
    }
}