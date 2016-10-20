interface Term: FormationTree {
    fun contains(term: Term): Boolean
    fun copy(): Term
    fun closeEnough(term: Term):Boolean
}

