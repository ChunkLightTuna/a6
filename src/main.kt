import java.io.FileInputStream
import java.util.*
import java.io.InputStream

//main is now the resolver yo
fun main(args: Array<String>) {


    var hSet = false

    val heuristics: Map<String, (Domain, Node) -> Int> = mapOf(
            "h0" to { domain, node -> 0 }, //no heuristic! doesn't work!
            "h1" to { domain, node -> (domain.goal - node.state).size/* + (domain.goalNeg - node.state.neg).size*/ }
    )
    var hPrime: (Domain, Node) -> Int = { domain, node -> 0 }
    var weight = 1.0

    args.forEach {
        if (it.endsWith(".in")) {
            System.setIn(FileInputStream(it))
            //            System.setIn(FileInputStream("sample-input/study2.in"))
        } else if (!hSet && heuristics.containsKey(it)) {
            hPrime = heuristics[it]!!
            hSet = true
        } else {
            try {
                weight = it.toDouble()
                assert(weight >= 1)
            } catch (e: Exception) {
                weight = 1.0
                println("expecting weight as an int >= 1")
            }
        }
    }

//    hPrime = heuristics["h1"]!!
//    weight = 1.0
    val hFun: (Domain, Node) -> Int = { domain, node -> (hPrime(domain, node) * weight).toInt() }


    val reader = System.`in`
    val domain = Parser.parse(reader, hFun)
    reader.close()

    val result = a_star(domain)

    var node: Node? = result.node
    if (node == null) {
        println("no solution found")
    }

    val stack = Stack<Action>()
    while (node != null && node.action != null) {
        stack.add(node.action)
        node = node.parent
    }

    var tick = 0
    while (!stack.isEmpty()) {

        val action = stack.pop()

        val sb = StringBuilder()
        sb.append("$tick ${action.name} ")
        action.vars.forEach { sb.append("$it ") }

        println(sb)
        /*
        0 Move B A C
        1 FromTable A B
        10 nodes generated
        4 nodes expanded
        */

        tick++
    }

    println(result.generated.toString() + " nodes generated")
    println(result.expanded.toString() + " nodes expanded")
}

fun a_star(domain: Domain): Result {
    val openList = PriorityQueue<Node>()
    openList.add(Node(domain.initState))

    var gen = 1
    var exp = 0

    val closedList = HashSet<Node>()

    var goal: Node? = null

    while (!openList.isEmpty()) {
        val current = openList.remove()

//        println(current)

        if (current.state.containsAll(domain.goal) && current.state.intersect(domain.goalNeg).isEmpty()/* && current.state.neg.containsAll(domain.goalNeg)*/) {
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