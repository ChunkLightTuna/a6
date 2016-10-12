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

