data class Variable(val label: String, val string: String) : Term {
    constructor(string: String, clauseId: Int) : this("$string$clauseId", string)


    override fun toString() = label

    override fun equals(other: Any?) = other is Variable && label == other.label

    override fun hashCode() = label.hashCode()

    override fun contains(v: Variable) = equals(v)

    override fun terms() = null

    override fun label() = label
}
