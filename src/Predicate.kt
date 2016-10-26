
data class Predicate private constructor(val name: String, val args: List<String>) {
    private constructor(predicate: Predicate) : this(predicate.name, predicate.args)
    constructor(string: String) : this(gen(string))

    companion object {
        fun gen(string: String): Predicate {
            return if (string.isBlank()) {
                Predicate("", listOf())
            } else {
                val split = string.split("(")
                val name = split[0].filterNot(Char::isWhitespace)
                val args = split[1].split(",")

                Predicate(name, args)
            }
        }

        fun genL(string: String): List<Predicate> {
            return if (string.isBlank()) {
                listOf()
            } else {
                string.filterNot(Char::isWhitespace)
                        .split(")")
                        .dropLast(1)
                        .map(::Predicate)
            }
        }
    }
}