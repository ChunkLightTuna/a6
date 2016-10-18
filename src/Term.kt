interface Term: FormationTree {
    fun contains(v: Variable): Boolean
    fun copy(): Term
}
