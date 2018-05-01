package cz.muni.fi.umimecesky.game.robots

import cz.muni.fi.umimecesky.random

/**
 * @author Marek Sabo
 */
class UniqueRandom(private val max: Int) {

    private val alreadyUsedSet = LinkedHashSet<Int>()

    fun next(): Int {
        if (alreadyUsedSet.size == max) alreadyUsedSet.clear()

        var result: Int
        do { result = random.nextInt(max) } while (!alreadyUsedSet.add(result))
        return result
    }
}