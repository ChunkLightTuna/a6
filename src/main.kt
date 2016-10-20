import java.io.FileInputStream
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

//main is now the resolver yo
fun main(args: Array<String>) {
    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream("sample-input/problems/cnf/simple/unification-2.cnf"))
        }
    }
    val reader = System.`in`
    val clauses = ArrayList<Clause>()
//    val clauses = mutableSetOf<Clause>()
    val sos = mutableSetOf<Clause>()
    val added = ArrayList<Pair<Int, Int>>()
    var neg = false



    reader.bufferedReader().lines().map {
        it.filter { !it.isWhitespace() }
    }.forEach {
        if (it == "---negatedquery---") {
            neg = true
        } else {

            clauses.add(Clause(it)/*, neg*/)
            if (neg) {
                sos.add(Clause(it))
            }
        }
    }

    reader.close()

    val unifier = Unifier()

    fun index(clauseA: Clause): Int {
        clauses.forEachIndexed { i, clauseB ->
            if (clauseA.closeEnough(clauseB)) {
                return i
            }
        }
        throw Exception("clause not found")
    }

    fun yoonify(): ShittyMultiContainer? {
        sos.forEach {
            val a = it
            a.literals.forEach {
                val literal = it
                val matches = clauses.filter { it.containsN(literal) }
                if (matches.isNotEmpty()) {
                    val b = matches.first()
                    val iA = index(a)
                    val iB = index(b)
                    if (!added.contains(Pair(iA, iB)) && !added.contains(Pair(iA, iB))) {
                        added.add(Pair(iA, iB))

                        val sc = unifier.unify(a, b)
                        return ShittyMultiContainer(sc, iA, iB)
                    }
                }
            }
        }
        return null
    }

    //REPEATING MYSELF YEA
    fun resolve(ssA: ShittySingleContainer, ssB: ShittySingleContainer): Clause? {
        val clauseA = ssA.clause
        val clauseB = ssB.clause
        if (clauseA.literals.size == 1 && clauseB.literals.size == 1 && clauseA.containsN(clauseB.literals.first())) {
            return null //we did it
        }


        //remove negated pair and return new clause
        clauseA.literals.forEachIndexed { i1, a ->
            clauseB.literals.forEachIndexed { j1, b ->
                if (a.negated !== b.negated && a.predicate.label == b.predicate.label) {
                    return Clause(
                            clauseA.literals.filterIndexed { i2, literal -> i1 != i2 } +
                                    clauseB.literals.filterIndexed { j2, literal -> j1 != j2 }
                    ).copy() //tinfoil hats
                }
            }
        }
        throw Exception("we're repeating ourself, this shouldn't happen")
    }

    val output = Output()
    clauses.forEachIndexed { i, clause -> output.append(clause.toString()) }

    var i = 0
    var k = 0
    while (i == 0) {
        val unified = yoonify()
        if (unified == null) {
            i = -1
            output.fail()
        } else {
            unified.clauses.indices.filter { it % 2 == 0 }.forEach {
                val newClause = resolve(unified.clauses[it], unified.clauses[it + 1])
                k++
                if (newClause == null) {
                    output.append("<empty>", unified.indexA, unified.indexB, true)
                    i = 1
                } else {
                    val newClauses = mutableSetOf<Clause>()
                    clauses.forEach {
                        if (!it.closeEnough(newClause)) {
                            newClauses.add(newClause)
                        }
                    }
                    clauses.addAll(newClauses)
                    newClauses.forEach {
                        output.append("$it", unified.indexA, unified.indexB, false)
                    }
                    sos.add(newClause)
                }
            }
        }
    }

    output.print()
    println("$k total resolutions\n")
}

class Output() {
    val array: ArrayList<Line> = ArrayList()
    var fail = false

    fun append(string: String) {
        array.add(Line(string, -1, -1, true, 0))
    }

    fun append(string: String, relA: Int, relB: Int, used: Boolean) {
        array.add(Line(string, relA, relB, used, 0))
    }

    fun fail() {
        fail = true
    }

    data class Line(val string: String, val relA: Int, val relB: Int, var used: Boolean, var actual:Int) {
    }

    fun print() {
        val s = array.size - 1
        array.forEachIndexed { i, line ->
            val cur = array[s - i]
            if (cur.used && cur.relA != -1) {
                array[cur.relA].used = true
                array[cur.relB].used = true
            }
        }

        var k = 0
        array.forEachIndexed { i, line ->
            if (line.used || fail) {
                line.actual = k + 1
                if (line.relA != -1) {
                    print("${array[line.relA].actual} and ${array[line.relB].actual} give ")
                }
                println("${k + 1}: ${line.string}")
                k++
            }
        }
        if(fail) {
            println("No proof exists.")
        }
    }
}


class ShittyMultiContainer(val clauses: List<ShittySingleContainer>, val indexA: Int, val indexB: Int)

/*
1: -Human(x1) | Mortal(x1)
2: Human(Socrates)
3: Animal(F2(x2)) | Loves(F1(x2), x2)
4: -Loves(x2, F2(x2)) | Loves(F1(x2), x2)
5: -Mortal(Socrates)
1 and 2 give 6: Mortal(Socrates)
5 and 6 give 7: <empty>
1646 total resolutions
 */