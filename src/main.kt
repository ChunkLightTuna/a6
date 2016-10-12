import java.io.FileInputStream
import java.util.*

/**
 * Created by ChunkLightTuna on 10/11/16.
 */

fun main(args: Array<String>) {
    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream(it))
        }
    }
    val reader = System.`in`
    val clauses = ArrayList<Clause>()
    reader.bufferedReader().lines().map {
        it.filter { !it.isWhitespace() }
    }.forEach {
        clauses.add(Clause(it))
    }
    reader.close()








    clauses.forEach(::println)

}

data class Clause private constructor(val literals: List<Literal>) {

    constructor(string: String) : this(generate(string))

    companion object {
        var ticker = 0
        fun generate(string: String): List<Literal> {
            ticker++
            val list = string.split("|").map { Literal(it, ticker) }
            return list
        }

        fun tickleTheTicker(): Int {
            return ++ticker
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

}


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

