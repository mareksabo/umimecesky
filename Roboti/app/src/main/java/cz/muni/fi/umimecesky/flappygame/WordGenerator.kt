package cz.muni.fi.umimecesky.flappygame;

import android.content.Context
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.RaceConcept
import java.util.*

/**
 * @author Marek Sabo
 */
class WordGenerator(private val context: Context, private val raceConcept: RaceConcept) {

    private var words = Stack<FillWord>()

//    unavailable grades
//     Psaní ě, Zdvojené hlásky  3
//    Skloňování, Zkratky a typografie 1

    fun getNextWord(): FillWord {
        if (words.empty()) {
            words = context.joinCategoryWordOpenHelper
                    .getRandomCategoryWords(raceConcept.categoryIDs, 2) // TODO add grade
        }
        return words.pop()
    }
}
