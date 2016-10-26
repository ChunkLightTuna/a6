import java.util.*

/**
 * Created by ChunkLightTuna on 10/24/16.
 */
class Domain private constructor(
        val predicates: List<Predicate>,
        val initConstants: List<String>,
        val constants: List<String>,
        val actions: List<Action>,
        val initState: List<Predicate>,
        val goal: List<Predicate>,
        val goalNeg: List<Predicate>,
        val gFun: (Node) -> Int,
        val hFun: (Node) -> Int) {

    companion object {
        fun getInstance(
                predicates: List<Predicate>,
                initConstants: List<String>,
                constants: List<String>,
                actions: List<Action>,
                initState: List<Predicate>,
                goal: List<Predicate>,
                goalNeg: List<Predicate>): Domain {
//
//            var increment = 0
//
//
//            val hm = HashMap<Int, String>()
//
//            constants.plus(initConstants).forEach {
//
//
//            }
//

//            constants.forEach { hm.put(increment++, it) }


            println("predicates:$predicates\n" +
                    "initConstants:$initConstants\n" +
                    "constants:$constants\n" +
                    "actions:$actions\n" +
                    "initState:$initState\n" +
                    "goal:$goal\n" +
                    "goalNeg:$goalNeg\n")
//
//            predicates.forEach {
//
//                val predicate: Predicate = it
//
////                predicate.
//
//            }

            val hFun: (Node) -> Int = { node -> 0 }
            val gFun: (Node) -> Int = { node -> node.parent!!.gValue + 1 }

            return Domain(
                    predicates = predicates,
                    initConstants = initConstants,
                    constants = constants,
                    actions = actions,
                    initState = initState,
                    goal = goal,
                    goalNeg = goalNeg,
                    gFun = gFun,
                    hFun = hFun
            )
        }
    }
}
