/**
 * Created by ChunkLightTuna on 10/24/16.
 */
class Domain private constructor(
        val predicates: List<Predicate>,
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

            val allConstants = constants + initConstants
            val allActions = mutableListOf<Action>()

            actions.forEach {
                val action = it
                val numVars = action.vars.size

                //more than this would be cruel, but this should be done w/o ifs!
                if (numVars == 1) {
                    allActions.addAll(allConstants.map { action.replace(listOf(it)) })
                }

                if (numVars == 2) {
                    allActions.addAll(allConstants.flatMap { i ->
                        allConstants.map { j ->
                            listOf(i, j)
                        }
                    }.map { action.replace(it) })
                }

                if (numVars == 3) {
                    allActions.addAll(allConstants.flatMap { i ->
                        allConstants.flatMap { j ->
                            allConstants.map { k ->
                                listOf(i, j, k)
                            }
                        }
                    }.map { action.replace(it) })
                }

                if (numVars == 4) {
                    allActions.addAll(allConstants.flatMap { i ->
                        allConstants.flatMap { j ->
                            allConstants.flatMap { k ->
                                allConstants.map { l ->
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
                    constants = allConstants,
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
