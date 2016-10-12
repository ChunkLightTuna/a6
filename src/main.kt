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