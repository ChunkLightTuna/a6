import java.io.InputStream

/**
 * Created by ChunkLightTuna on 10/25/16.
 */

class Parser() {

    companion object {
        fun parse(reader: InputStream, hFun: (Domain, Node) -> Int): Domain {
            val actions = mutableListOf<Action>()

            var desc = ""
            var pre = ""
            var preNeg = ""
            var del = ""
            var goalB = false
            val predicates = mutableListOf<Predicate>()
            val constants = mutableListOf<String>()

            val initConstants = mutableListOf<String>()
            val initState = mutableListOf<Predicate>()
            val goal = mutableListOf<Predicate>()
            val goalNeg = mutableListOf<Predicate>()
            var numActions = 0

            reader.bufferedReader().lines().forEach {
                when {
                    it.startsWith("predicates: ") -> {
                        val preds = Predicate.genL(it.drop("predicates: ".length))
                        predicates.addAll(preds)
                    }
                    it.startsWith("constants: ") && !goalB -> {
                        constants.addAll(genConstants(it.drop("constants: ".length)))
                        goalB = true
                    }

                    it.startsWith("constants: ") && goalB -> {
                        initConstants.addAll(genConstants(it.drop("constants: ".length)))
                    }
                    it.startsWith("initial: ") -> {
                        val preds = Predicate.genL(it.drop("initial: ".length))
                        initState.addAll(preds)
                    }
                    it.startsWith("goal: ") -> {
                        val preds = Predicate.genL(it.drop("goal: ".length))
                        goal.addAll(preds)
                    }
                    it.startsWith("goalneg: ") -> {
                        val preds = Predicate.genL(it.drop("goalneg: ".length))
                        goalNeg.addAll(preds)
                    }

                    it.endsWith("actions") -> numActions = it.dropLast("actions".length).filterNot(Char::isWhitespace).toInt()
                    it.startsWith("#") || it.isEmpty() -> Unit

                    it.startsWith("pre:") -> pre = it.drop("pre:".length)
                    it.startsWith("preneg:") -> preNeg = it.drop("preneg:".length)
                    it.startsWith("del:") -> del = it.drop("del:".length)

                    it.startsWith("add:") -> {
                        actions.add(Action.gen(
                                desc,
                                Predicate.genL(pre),
                                Predicate.genL(preNeg),
                                Predicate.genL(del),
                                Predicate.genL(it.drop("add:".length))
                        ))
                    }
                    else -> desc = it
                }
            }

            reader.close()

            assert(numActions == actions.size)
            return Domain.getInstance(predicates, initConstants, constants, actions, initState, goal, goalNeg, hFun)
        }
    }
}

fun genConstants(string: String): List<String> {
    return string.split(" ")
}