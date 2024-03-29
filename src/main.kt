import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream("sample-input/e4.cnf"))
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

    val unifier = Unifier()

    unifier.unify(clauses[0], clauses[1])
}
