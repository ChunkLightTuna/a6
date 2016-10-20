data class Predicate(val label: String, val termList: TermList) : FormationTree {
    constructor(string: String, clauseId: Int) :
    this(string.takeWhile { it != '(' }, TermList(string.dropWhile { it != '(' }.drop(1).dropLast(1), clauseId))

    override fun toString() = "$label($termList)"

    override fun terms() = termList
    override fun label() = label

    override fun equals(other: Any?) = other is Predicate && other.termList == termList && other.label == label

    override fun hashCode(): Int {
        return termList.hashCode() * label.hashCode()
    }

    fun update(old: Term, new: Term) {
        termList.terms.forEachIndexed { i, term ->
            if (termList.terms[i].contains(old)) {
                if (termList.terms[i] is Function) {
                    (termList.terms[i] as Function).update(old, new)
                } else {
                    assert(termList.terms[i] is Variable)
                    if (termList.terms[i] == old) termList.terms[i] = new
                }
            }
        }
    }

    fun copy() = Predicate(label, termList.copy())

    fun closeEnough(predicate:Predicate):Boolean {
        return label == predicate.label && termList.closeEnough(predicate.termList)
    }
}

