data class Action(val name: String, val vars: List<String>, val pre: List<Predicate>, val preNeg: List<Predicate>, val del: List<Predicate>, val add: List<Predicate>) {

    companion object {
        fun gen(label: String, pre: List<Predicate>, preNeg: List<Predicate>, del: List<Predicate>, add: List<Predicate>): Action {
//            Study subject person
            val labelS = label.split(" ")
            return Action(labelS[0], labelS.drop(1), pre, preNeg, del, add)
        }
    }
}