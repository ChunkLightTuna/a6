data class Predicate private constructor(val name: String, val termList: TermList) {
    constructor(string: String, clauseId: Int) :
    this(string.takeWhile { it != '(' }, TermList(string.dropWhile { it != '(' }.drop(1).dropLast(1), clauseId))

    override fun toString(): String {
        return "$name($termList)"
    }
}