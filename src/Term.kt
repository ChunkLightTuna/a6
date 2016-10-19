interface Term: FormationTree {
    fun contains(term: Term): Boolean
    fun copy(): Term
}

