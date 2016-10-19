data class Constant(val label: String) : Term {

    override fun toString() = label

    override fun equals(other: Any?) = other is Constant && label == other.label

    override fun hashCode() = label.hashCode()

    override fun contains(term: Term) = this == term

    override fun terms() = null

    override fun label() = label

    override fun copy() = Constant(label)
}

