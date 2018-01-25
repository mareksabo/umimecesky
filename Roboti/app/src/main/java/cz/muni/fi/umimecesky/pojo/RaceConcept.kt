package cz.muni.fi.umimecesky.pojo

import cz.muni.fi.umimecesky.utils.WebUtil
import java.io.Serializable

/**
 * Represents concept/category with current state.
 * F.e. pisanie i/y_
 */
class RaceConcept(val name: String, val categoryIDs: List<Int>, val numberOfLevels: Int) : Serializable, Comparable<RaceConcept> {
    private var currentLevel: Int = 0

    init {
        currentLevel = 1
    }

    fun getCurrentLevel(): Int = currentLevel

    private fun setCurrentLevel(currentLevel: Int) {
        if (currentLevel <= numberOfLevels) {
            this.currentLevel = currentLevel
        }
    }

    /**
     * Increases the current level.
     *
     * @return true if level was increased
     */
    fun increaseLevel(): Boolean {
        val oldLevel = currentLevel
        setCurrentLevel(currentLevel + 1)
        WebUtil.updateConcept(this)
        return oldLevel != currentLevel
    }

    /**
     * Number describing how far in terms of levels user is.
     * @return  interval higher than 0, lower or equal to 1
     */
    fun levelProgress(): Float = currentLevel / numberOfLevels.toFloat()

    override fun toString(): String {
        return "RaceConcept{" +
                "name='" + name + '\'' +
                ", categoryIDs=" + categoryIDs +
                ", numberOfLevels=" + numberOfLevels +
                ", currentLevel=" + currentLevel +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as RaceConcept?

        return name == that!!.name

    }

    override fun hashCode(): Int = name.hashCode()

    override fun compareTo(other: RaceConcept): Int = this.name.compareTo(other.name)
}
