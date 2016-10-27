data class Node(
        val state: State,
        val parent: Node? = null,
        val action: Action? = null,
        var gValue: Int = 0,
        var fValue: Int = 0) : Comparable<Node> {

    //gValue is the cost of going from startNode to this node (0 for startNode)

    //fValue is the cost of getting from startNode to goalNode by traveling through this node
    //startNode -> this the gValue
    //this -> goalNode is estimated by hValue
    //so fValue = gValue + hValue
    //or hValue = fValue - gValue

    constructor(init: List<Predicate>) : this(State(init, listOf()))


    constructor(
            state: State,
            parent: Node,
            gFun: (Node) -> Int,
            hFun: (Node) -> Int) : this(state = state, parent = parent) {
        gValue = gFun(this)
        fValue = gValue + hFun(this)
    }

    override fun compareTo(other: Node): Int {
        //only gValue and fValue matter for comparison
        if (fValue == other.fValue) { //go w/ the heuristic in the event of a tiebreaker
            return (fValue - gValue).compareTo(other.fValue - other.gValue)
        } else {
            return fValue.compareTo(other.fValue)
        }
    }

    override fun hashCode() = state.pos.hashCode() * state.neg.hashCode()

    override fun equals(other: Any?) = other is Node && state.pos == other.state.pos && state.neg == other.state.neg

    fun genChildren(domain: Domain): List<Node> {

        val children = mutableListOf<Node>()

        domain.actions.forEach {
            if (state.pos.containsAll(it.pre) && state.neg.containsAll(it.preNeg)) {
                val newPos = state.pos + it.add - it.del
                val newNeg = state.neg - it.add + it.del
                children.add(
                        Node(
                                State(newPos, newNeg),
                                this,
                                it,
                                gValue + 1,
                                0
                        )
                )
            }
        }

        return children
    }
}