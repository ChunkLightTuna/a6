data class Node(
        val state: List<Predicate>,
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

    constructor(
            state: List<Predicate>,
            parent: Node,
            action: Action,
            domain: Domain,
            gFun: (Node) -> Int,
            hFun: (Domain, Node) -> Int) : this(state = state, parent = parent, action = action) {
        gValue = gFun(this)
        fValue = gValue + hFun(domain, this)
    }

    override fun compareTo(other: Node): Int {
        //only gValue and fValue matter for comparison
        if (fValue == other.fValue) { //go w/ the heuristic in the event of a tiebreaker
            return (fValue - gValue).compareTo(other.fValue - other.gValue)
        } else {
            return fValue.compareTo(other.fValue)
        }
    }

    override fun hashCode(): Int {
        var k = 0
        state.forEachIndexed { i, s -> k += (i + 1) * s.hashCode() }

        return gValue + k
    }

//    override fun equals(other: Any?) = other is Node && state.containsAll(other.state) && other.state.containsAll(state)

    override fun toString(): String {

        val sb = StringBuilder()
        state.forEach {
            sb.append("$fValue ${it.name.substring(0, 3)}").append(it.args.map { it -> it.substring(0, 3) }).append(" ")
        }

        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Node) {
            return false
        }

        //inefficient, but they're small so w/e
        state.forEachIndexed { i, s ->
            if (s != other.state[i]) {
                return false
            }
        }

        return true
    }


    fun genChildren(domain: Domain): List<Node> {

        val children = mutableListOf<Node>()

        domain.actions.forEach {
            if (state.containsAll(it.pre) && state.intersect(it.preNeg).isEmpty()) {
                val newPos = state + it.add - it.del
//                val newNeg = state.neg - it.add + it.del
                children.add(

                        Node(
                                newPos,
                                this,
                                it,
                                domain,
                                domain.gFun,
                                domain.hFun
                        )
                        /*
                        constructor(
                        state: List<Predicate>,
                        parent: Node,
                        action: Action,
                        gFun: (Node) -> Int,
                        hFun: (Node) -> Int)
                        */


//                        Node(
//                                newPos,
//                                this,
//                                it,
//                                gValue + 1,
//                                0
//                        )
                )
            }
        }

        return children
    }
}