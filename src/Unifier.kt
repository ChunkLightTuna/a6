//variables, functions and constants are terms
//a vertex labeled with a term is a formation tree

//functions evaluate to terms
//predicates evaluate to T/F

//predicate -> formation tree
//term ->
class Unifier {
    //Most General Unifier!


    fun fuckTheBrownCoats(a: Clause, b: Clause) {

        val tmA = a.literals[0].predicate.termList
        val tmB = b.literals[0].predicate.termList

        mgu(tmA, tmB)
    }

    fun mgu(a: TermList, b: TermList): Boolean {

        b.formationTree.forEach {
            val T_0 = it
            a.formationTree.forEach {
                val T_1 = it

                //Compare
                if (T_0.l() != T_1.l()) {
                    //Substitute

                    if (T_0 !is Variable && T_1 !is Variable) {
                        //(a)
                        return false


                    } else if (T_0 is Variable && T_1.contains(T_0) || T_1 is Variable && T_0.contains(T_1)) {
                        //(b)
                        return false


                    } else {
                        //(c)
                        T_0
                    }


                } else {
                    //Advance
                }
            }
        }


        return false
    }

    private fun validPairing(a: Literal, b: Literal): Boolean {
        return a.predicate.name == b.predicate.name && a.negated !== b.negated
                && a.predicate.termList.formationTree.size == b.predicate.termList.formationTree.size
    }
}