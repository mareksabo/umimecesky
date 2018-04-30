package cz.muni.fi.umimecesky.game.flappy;

import android.content.Context
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import java.util.*

/**
 * @author Marek Sabo
 */
class WordGenerator(private val context: Context, private val raceConcept: RaceConcept) {

    private var words = Stack<FillWord>()

    fun getNextWord(): FillWord {
        if (words.empty()) {
            words = context.joinCategoryWordOpenHelper
                    .getRandomCategoryWords(raceConcept.categoryIDs, prefs.flappyGradeName.grade)
        }
        return words.pop()
    }
}