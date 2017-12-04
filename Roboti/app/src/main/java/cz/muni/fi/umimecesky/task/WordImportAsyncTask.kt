package cz.muni.fi.umimecesky.task

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import cz.muni.fi.umimecesky.db.CategoryDbHelper
import cz.muni.fi.umimecesky.db.WordCategoryDbHelper
import cz.muni.fi.umimecesky.db.WordDbHelper
import cz.muni.fi.umimecesky.utils.Constant.IS_FILLED
import cz.muni.fi.umimecesky.utils.Conversion
import cz.muni.fi.umimecesky.utils.Util
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

private val categoryUnwantedId = 46

class WordImportAsyncTask(val activity: Activity) : AsyncTask<Void, Void, Void>() {

    private lateinit var dialog : MaterialDialog

    override fun onPreExecute() {
        super.onPreExecute()
        dialog = MaterialDialog.Builder(activity)
                .title("Importuji data")
                .content("Nahrávám data, hned to bude.")
                .cancelable(false)
                .show()
    }

    override fun doInBackground(vararg params: Void): Void? {

        try {
            importCategories()
            importConversions()
            importWords()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(IOException::class)
    private fun importCategories() {
        val dbHelper = CategoryDbHelper(activity)
        val db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 1, 2)

        //id;name;
        //id;name;name2; - "&ndash;" problem

        val buffer = openFileIgnoreFirstLine("concepts.csv")

        db.beginTransaction()

        var line: String? = buffer.readLine()

        while (line != null) {
            val columns = line.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            Log.v("Column ", Arrays.toString(columns))

            if (columns.size == 3) {
                columns[1] = columns[1].trim({ it <= ' ' }) + " - " + columns[2].trim({ it <= ' ' })
            }

            val id = Integer.parseInt(columns[0].trim({ it <= ' ' }))
            Log.i("id", id.toString())
            if (id != categoryUnwantedId) {
                dbHelper.addCategory(db, id.toLong(), columns[1].trim({ it <= ' ' }))
            }
            line = buffer.readLine()
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    @Throws(IOException::class)
    private fun openFileIgnoreFirstLine(fileName: String): BufferedReader {

        val inStream = activity.assets.open(fileName)
        val buffer = BufferedReader(InputStreamReader(inStream))
        buffer.readLine() // column names

        return buffer
    }

    /**
     * Format concept;word;.
     * concept - category id
     * word - fill word id
     */
    @Throws(IOException::class)
    private fun importConversions() {
        val dbHelper = WordCategoryDbHelper(activity)
        val db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 1, 2)

        val buffer = openFileIgnoreFirstLine("doplnovacka_concept_word.csv")

        db.beginTransaction()

        var line: String? = buffer.readLine()

        while ((line) != null) {
            val columns = line.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()


            dbHelper.addConversion(db,
                    Integer.parseInt(columns[0].trim({ it <= ' ' })),
                    java.lang.Long.parseLong(columns[1].trim({ it <= ' ' }))
            )
            line = buffer.readLine()
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    /**
     * CSV file has following data :
     *
     *
     * id;word;solved;variant1;variant2;correct;explanation;grade;visible
     * correct - 0 / 1 - if correct is variant1 or variant2
     * grade - word clue complexity
     * visible - 0 / 1 - if this word can be used or not
     */
    @Throws(IOException::class)
    private fun importWords() {

        val wordCSVFilename = "doplnovacka_word.csv"

        val dbHelper = WordDbHelper(activity)
        val db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 1, 2)

        val inStream = activity.assets.open(wordCSVFilename)
        val buffer = BufferedReader(InputStreamReader(inStream))

        buffer.readLine() // column names are ignored

        var line: String? = buffer.readLine()

        db.beginTransaction()

        while (line != null) {
            val columns = line.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            dbHelper.addFilledWord(db, java.lang.Long.parseLong(columns[0].trim({ it <= ' ' })),
                    columns[1].trim({ it <= ' ' }),
                    columns[2].trim({ it <= ' ' }),
                    columns[3].trim({ it <= ' ' }),
                    columns[4].trim({ it <= ' ' }),
                    columns[5].trim({ it <= ' ' }),
                    columns[6].trim({ it <= ' ' }),
                    Integer.parseInt(columns[7].trim({ it <= ' ' })),
                    Conversion.stringNumberToBoolean(columns[8].trim({ it <= ' ' }))
            )
            line = buffer.readLine()
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    override fun onPostExecute(aVoid: Void?) {
        val prefs = Util.getSharedPreferences(activity)
        prefs.edit().putBoolean(IS_FILLED, true).apply()
        dialog.dismiss()
    }

}
