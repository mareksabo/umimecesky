package cz.muni.fi.umimecesky.utils

import cz.muni.fi.umimecesky.random

/**
 * @author Marek Sabo
 */
class UniqueRandom(private val max: Int = 32) {

    private val alreadyUsedSet = LinkedHashSet<Int>()

    fun next(): Int {
        if (alreadyUsedSet.size == max) alreadyUsedSet.clear()

        var result: Int
        do { result = random.nextInt(max) } while (!alreadyUsedSet.add(result))
        return result
    }
}