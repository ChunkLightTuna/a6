//variables, functions and constants are terms
//a vertex labeled with a term is a formation tree

//functions evaluate to terms
//predicates evaluate to T/F

//predicate -> formation tree
//term ->

data class ShittySingleContainer(val clause: Clause, val i: Int, val j: Int)

class Unifier {
    //Most General Unifier!

    fun unify(clauseA: Clause, clauseB: Clause): List<ShittySingleContainer> {
        val out = mutableListOf<ShittySingleContainer>()

        clauseA.literals.forEachIndexed { i, literalA ->
            clauseB.literals.forEachIndexed { j, literalB ->

                //skip if sign is the same or predicate labels are different
                if (literalA.negated != literalB.negated && literalA.predicate.label == literalB.predicate.label) {

                    val clauseCopyA = clauseA.copy()
                    val clauseCopyB = clauseB.copy()
                    val literalCopyA = literalA.copy()
                    val literalCopyB = literalB.copy()

                    if (mostGeneralUnifier(clauseCopyA, clauseCopyB, literalCopyA.predicate.termList, literalCopyB.predicate.termList)) {
                        out.add(ShittySingleContainer(clauseCopyA, i, j))
                        out.add(ShittySingleContainer(clauseCopyB, i, j))
//                        println("$clauseCopyA\n$clauseCopyB\n")
                    }
                }
            }
        }
        return out
    }

    fun mostGeneralUnifier(clauseA: Clause,
                           clauseB: Clause,
                           termListA: TermList,
                           termListB: TermList): Boolean {

        val containerA = Predicate("", termListA)
        val containerB = Predicate("", termListB)


        if (termListA.terms.size != termListB.terms.size) {
            return false
        }

        for (i in termListA.terms.indices) {
            val termA = termListA.terms[i]
            val termB = termListB.terms[i]

            when {
                constAndConst(termA, termB) -> if (termA != termB) return false

                constAndFun(termA, termB) -> return false

                constAndVar(termA, termB) -> {
                    update(clauseB, clauseA, containerA, containerB, termB, termA)
                }

                constAndVar(termB, termA) -> {
                    update(clauseA, clauseB, containerA, containerB, termA, termB)
                }

                funAndFun(termA, termB) -> {
                    if (termA.label() != termB.label()) {//6
                        return false
                    } else {//7
                        mostGeneralUnifier(
                                clauseA,
                                clauseB,
                                (termA as Function).termList,
                                (termB as Function).termList)
                    }
                }

                funAndVar(termA, termB) -> {
//                    println("8a fun:\"$termA\" var:\"$termB\" contains: ${termA.contains(termB as Variable)}")
                    if (termA.contains(termB)) {//8
                        return false
                    } else {//9
                        update(clauseA, clauseB, containerA, containerB, termB, termA)
                    }
                }

                funAndVar(termB, termA) -> {
//                    println("8b fun:\"$termA\" var:\"$termB\" contains: ${termA.contains(termB as Variable)}")
                    if (termB.contains(termA as Variable)) {//8
                        return false
                    } else {//9
                        update(clauseA, clauseB, containerA, containerB, termA, termB)
                    }
                }

                else -> {
                    assert(termA is Variable && termB is Variable)
//                    println("10 fun:\"$termA\" var:\"$termB\" contains: ${termA.contains(termB as Variable)}")
                    update(clauseA, clauseB, containerA, containerB, termA, termB)
                }

            }
        }
        return true
    }

    private fun update(clauseA: Clause, clauseB: Clause, containerA: Predicate, containerB: Predicate, termA: Term, termB: Term) {
        clauseA.update(termA, termB)
        clauseB.update(termA, termB)
        containerA.update(termA, termB)
        containerB.update(termA, termB)
    }

    fun constAndConst(a: Term, b: Term) = a is Constant && b is Constant

    fun constAndFun(a: Term, b: Term) = (a is Constant && b is Function) || (a is Function && b is Constant)

    fun constAndVar(a: Term, b: Term) = a is Constant && b is Variable

    fun funAndFun(a: Term, b: Term) = a is Function && b is Function

    fun funAndVar(a: Term, b: Term) = a is Function && b is Variable
}

