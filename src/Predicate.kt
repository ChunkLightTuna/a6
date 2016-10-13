data class Predicate private constructor(val label: String, val termList: TermList) : FormationTree {
    constructor(string: String, clauseId: Int) :
    this(string.takeWhile { it != '(' }, TermList(string.dropWhile { it != '(' }.drop(1).dropLast(1), clauseId))

    override fun toString(): String {
        return "$label($termList)"
    }

    override fun terms() = termList
    override fun label() = label

    fun update(old: Term, new: Term) {
        termList.terms.forEachIndexed { i, term ->
            if (term == old) {
                termList.terms[i] = new
            }
        }
    }
}
