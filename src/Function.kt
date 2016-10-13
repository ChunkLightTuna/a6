data class Function private constructor(val label: String, val termList: TermList) : Term {
    constructor(string: String, clauseId: Int) :
    this(string.takeWhile { it != '(' }, TermList(string.dropWhile { it != '(' }.drop(1).dropLast(1), clauseId))

    override fun toString() = "$label($termList)"

    override fun equals(other: Any?) = other is Function && label == other.label && termList == other.termList

    override fun hashCode() = label.hashCode() * termList.hashCode()

    override fun contains(v: Variable) = termList.contains(v)

    override fun terms() = termList

    override fun label() = label
}
