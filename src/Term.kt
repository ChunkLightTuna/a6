/**
 * Created by ChunkLightTuna on 10/12/16.
 */
interface Term: FormationTree {
    fun contains(v: Variable): Boolean
    fun l(): String
}