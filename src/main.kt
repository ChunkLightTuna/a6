import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream

fun main(args: Array<String>) {

    assert(args.size == 3)

    if (args[0].endsWith(".mdp")) {
        System.setIn(FileInputStream(args[0]))
    }

    val discountFactor = args[1].toDouble()
    assert(discountFactor > 0 && discountFactor <= 1)

    val terminationCriterion = args[2].toDouble()

    val reader = System.`in`
    val domain = parse(reader, discountFactor, terminationCriterion)
    reader.close()

    //initialized to reward cha
    val utilities = Array(domain.states.size, { 0.0 })
    var backupCount = 0

    var maxDiff = Double.MAX_VALUE

    //vi
    while (maxDiff > domain.terminationCriterion) {// || backupCount < 400) {
        maxDiff = -Double.MAX_VALUE

        val utilitiesCurrent = Array(domain.states.size, { 0.0 })

        val diffs = mutableListOf<Double>()

        domain.states.forEachIndexed { i, state ->
            val newUtility = domain.states[i].reward + (domain.discountFactor * state.max(utilities))
            diffs.add(newUtility)
            utilitiesCurrent[i] = newUtility
            maxDiff = Math.max(maxDiff, Math.abs(utilities[i] - newUtility))
            backupCount++
        }

        utilitiesCurrent.forEachIndexed { index, double ->
            utilities[index] = double
        }
    }

    //ok now at this point our utility numbers should be gucci, just need to print out argMax of each state
    domain.states.forEach {
        println(if (it.actions.isEmpty()) "" else it.argMax(utilities))
    }

    println("$backupCount backups performed.")
}


fun parse(reader: InputStream, discountFactor: Double, terminationCriterion: Double): Domain {
    fun readSkip(reader: BufferedReader): List<String> {
        var line = "#"
        while (line.startsWith('#')) {
            line = reader.readLine()
        }
        return line.split(' ')
    }

    val lines: BufferedReader = reader.bufferedReader()

    val numStates = readSkip(lines).last().toInt()

    val startState = readSkip(lines).last().toInt()

    val states = mutableListOf<State>()

    (1..numStates).forEach {
        val firstLine = readSkip(lines)

        val reward = firstLine[0].toDouble()
        val terminal = firstLine[1] == "1"
        val numActions = firstLine[2].toInt()

        val actions = mutableListOf<Action>()

        (1..numActions).forEach {
            val actionString = readSkip(lines)

            val numSuccessors = actionString[0].toInt()
            val action = mutableListOf<Pair<Int, Double>>()
            (1..numSuccessors).forEach { j ->
                action.add(Pair(actionString[j * 2 - 1].toInt(), actionString[j * 2].toDouble()))
            }
            actions.add(Action(action))
        }

        states.add(State(reward, terminal, actions))
    }

    reader.close()

    return Domain(startState, states, discountFactor, terminationCriterion)
}

data class Domain(val s0: Int, val states: List<State>, val discountFactor: Double, val terminationCriterion: Double)

data class State(val reward: Double, val terminalState: Boolean, val actions: List<Action>) {

    fun max(utility: Array<Double>): Double {
        if (actions.isEmpty()) return 0.0

        var maxValue = -Double.MAX_VALUE
        actions.forEach {
            maxValue = Math.max(maxValue, it.value(utility))
        }

        return maxValue
    }

    fun argMax(utility: Array<Double>): Int {
        var maxValue = -Double.MAX_VALUE
        var maxAction = 0

        actions.forEachIndexed { i, action ->
            if (action.value(utility) > maxValue) {
                maxValue = action.value(utility)
                maxAction = i
            }
        }

        return maxAction
    }
}

data class Action(val action: List<Pair<Int/*destination state*/, Double/*transition probability*/>>) {

    fun value(utility: Array<Double>): Double {
        var sum = 0.0
        action.forEachIndexed { i, pair ->
            sum += pair.second * utility[pair.first]
        }
        return sum
    }
}