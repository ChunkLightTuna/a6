import java.util.*

/**
 * Created by ChunkLightTuna on 10/26/16.
 */
class Solvr {
    fun a_star(domain: Domain, startNode: Node): Result {
        val openList = PriorityQueue<Node>()
        openList.add(startNode)

        var gen = 1
        var exp = 0

        val closedList = HashSet<Node>()

        var goal: Node? = null

        while (!openList.isEmpty()) {
            val current = openList.remove()

            if (current.state.pos.containsAll(domain.goal) && current.state.neg.containsAll(domain.goalNeg)) {
                goal = current
                break
            }

            if (!closedList.contains(current)) { //because openList may contain multiple copies of a node, we need to check this
                closedList.add(current)
                exp++
                val children = current.genChildren(domain)
                gen += children.size

                children.forEach {
                    if (!closedList.contains(it)) {
                        openList.add(it)
                        //our openlist will contain multiple copies, but only the best will be opened
                    }
                }
            }
        }
        return Result(goal, gen, exp)
    }
}