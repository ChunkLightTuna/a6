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
        val hFun: (Domain, Node) -> Int) {

    companion object {
        fun getInstance(
                predicates: List<Predicate>,
                initConstants: List<String>,
                constants: List<String>,
                actions: List<Action>,
                initState: List<Predicate>,
                goal: List<Predicate>,
                goalNeg: List<Predicate>,
                hFun: (Domain, Node) -> Int): Domain {
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


            val allConst = constants + initConstants
            val allActions = mutableListOf<Action>()

            actions.forEach {
                val action = it
                val numVars = action.vars.size


                if (numVars == 1) {
                    allActions.addAll(allConst.map { action.replace(listOf(it)) })
                }


                if (numVars == 2) {
                    allActions.addAll(allConst.flatMap { i ->
                        allConst.map { j ->
                            listOf(i, j)
                        }
                    }.map { action.replace(it) })
                }

                if (numVars == 3) {
                    allActions.addAll(allConst.flatMap { i ->
                        allConst.flatMap { j ->
                            allConst.map { k ->
                                listOf(i, j, k)
                            }
                        }
                    }.map { action.replace(it) })
                }

                if (numVars == 4) {//more than this would be cruel, but this should be done w/o ifs!
                    allActions.addAll(allConst.flatMap { i ->
                        allConst.flatMap { j ->
                            allConst.flatMap { k ->
                                allConst.map { l ->
                                    listOf(i, j, k, l)
                                }
                            }
                        }
                    }.map { action.replace(it) })
                }
            }


            val gFun: (Node) -> Int = { node -> node.parent!!.gValue + 1 }

            return Domain(
                    predicates = predicates,
                    initConstants = initConstants,
                    constants = constants,
                    actions = allActions,
                    initState = initState,
                    goal = goal,
                    goalNeg = goalNeg,
                    gFun = gFun,
                    hFun = hFun
            )
        }
    }
}
