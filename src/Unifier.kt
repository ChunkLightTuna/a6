//variables, functions and constants are terms
//a vertex labeled with a term is a formation tree

//functions evaluate to terms
//predicates evaluate to T/F

//predicate -> formation tree
//term ->
class Unifier {
    //Most General Unifier!

    fun unify(clauseA: Clause, clauseB: Clause) {
        clauseA.literals.forEach { literalA ->
            clauseB.literals.forEach { literalB ->

                //skip if sign is the same or predicate labels are different
                if (literalA.negated != literalB.negated && literalA.predicate.label == literalB.predicate.label) {

                    val clauseCopyA = Clause(clauseA)
                    val clauseCopyB = Clause(clauseB)
                    val literalCopyA = literalA.copy()
                    val literalCopyB = literalB.copy()

//                    val asdfA: List<TermList> = copyA.literals.map { it.predicate }.map { it.termList }

                    if (mostGeneralUnifier(clauseCopyA, clauseCopyB, literalCopyA.predicate.termList, literalCopyB.predicate.termList)) {
                        println("$clauseCopyA\n$clauseCopyB\n")
                    }
                }
            }
        }
    }


    fun mostGeneralUnifier(clauseA: Clause,
                           clauseB: Clause,
                           termListA: TermList,
                           termListB: TermList): Boolean {

        if (termListA.terms.size != termListB.terms.size) {
            return false
        }

        for (i in termListA.terms.indices) {
            val termA = termListA.terms[i]
            val termB = termListB.terms[i]

            if (termA is Constant && termB is Constant) {//2
                return termA == termB

            } else if (constAndFun(termA, termB)) {//3
                println("3 $termA $termB")
                return false

            } else if (constAndVar(termA, termB)) {//4
                clauseB.update(termB, termA)
//                termListB.terms[i] = termA
//                println("!!!!!!!!!$termA $termB")

            } else if (constAndVar(termB, termA)) {//4
                clauseA.update(termA, termB)
//                termListA.terms[i] = termB
//                println("!!!!!!!!!$termA $termB")

            } else if (funAndFun(termA, termB)) {//5
                if (termA.label() != termB.label()) {//6
                    println("6 $termA $termB")
                    return false
                } else {//7
                    mostGeneralUnifier(
                            clauseA,
                            clauseB,
                            (termA as Function).termList,
                            (termB as Function).termList)
                }

            } else if (funAndVar(termA, termB)) {
                if (termA.contains(termB as Variable)) {//8
                    println("81 $termA $termB")
                    return false
                } else {//9
//                    termListB.terms[i] = termA
//                    println("!!!!!!!!!$termA $termB")
                    clauseB.update(termB, termA)
                }

            } else if (funAndVar(termB, termA)) {
                if (termB.contains(termA as Variable)) {//8
                    println("82 $termA $termB")
                    return false
                } else {//9
//                    termListA.terms[i] = termB
//                    println("!!!!!!!!!$termA $termB")
                    clauseA.update(termA, termB)
                }

            } else {//10
                assert(termA is Variable && termB is Variable)
//                termListA.terms[i] = termB
//                println("!!!!!!!!!$termA $termB")
                clauseA.update(termA, termB)
            }
        }
        return true
    }

//    private fun validPairing(a: Literal, b: Literal): Boolean {
//        return a.predicate.label == b.predicate.label && a.negated !== b.negated
//                && a.predicate.termList.terms.size == b.predicate.termList.terms.size
//    }

    fun constAndFun(a: Term, b: Term) = (a is Constant && b is Function) || (a is Function && b is Constant)

    fun constAndVar(a: Term, b: Term) = a is Constant && b is Variable

    fun funAndFun(a: Term, b: Term) = a is Function && b is Function

    fun funAndVar(a: Term, b: Term) = a is Function && b is Variable
}
