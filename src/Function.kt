data class Function private constructor(val label: String, val termList: TermList) : Term {
    constructor(string: String, clauseId: Int) :
    this(string.takeWhile { it != '(' }, TermList(string.dropWhile { it != '(' }.drop(1).dropLast(1), clauseId))

    override fun toString() = "$label($termList)"

    override fun equals(other: Any?) = other is Function && label == other.label && termList == other.termList

    override fun hashCode() = label.hashCode() * termList.hashCode()

    override fun contains(term: Term) = termList.contains(term)

    fun update(old: Term, new: Term) {

//        println("terms old:$termList")
        termList.terms.forEachIndexed { i, term ->
            if (term.contains(old)) {
                if (term is Function) {
                    term.update(old, new)
                } else {
                    assert(term is Variable)
                    if (term == old) termList.terms[i] = new
                }
            }
        }
    }

    override fun terms() = termList

    override fun label() = label

    override fun copy() = Function(label, termList.copy())
}

