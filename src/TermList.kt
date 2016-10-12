data class TermList private constructor(val terms: List<Any>) {
    constructor(string: String, clauseId: Int) : this(generate(string).map {
        when {
            it.endsWith(')') -> Function(it, clauseId)
            it[0].isLowerCase() -> Variable(it, clauseId)
            else -> Constant(it)
        }
    })

    override fun toString(): String {
        return terms.toString().drop(1).dropLast(1)
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
                            return listOf(string.take(i)) + generate(string.drop(i + 1))
                        }
                    }
                }
            }

            return listOf(string) //reached the end
        }
    }
}