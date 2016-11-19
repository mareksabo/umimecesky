package cz.muni.fi.umimecesky.roboti.pojo;


public class FillWord {

    private long id;
    private String wordMissing;
    private String wordFilled;
    private String variant1;
    private String variant2;
    private int correctVariant;

    public FillWord(long id, String wordMissing, String wordFilled, String variant1, String variant2, int correctVariant) {
        this.id = id;
        this.wordMissing = wordMissing;
        this.wordFilled = wordFilled;
        this.variant1 = variant1;
        this.variant2 = variant2;
        this.correctVariant = correctVariant;
    }

    @Override
    public String toString() {
        return "FillWord{" +
                "id=" + id +
                ", wordMissing='" + wordMissing + '\'' +
                ", wordFilled='" + wordFilled + '\'' +
                ", variant1='" + variant1 + '\'' +
                ", variant2='" + variant2 + '\'' +
                ", correctVariant=" + correctVariant +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getWordMissing() {
        return wordMissing;
    }

    public String getWordFilled() {
        return wordFilled;
    }

    public String getVariant1() {
        return variant1;
    }

    public String getVariant2() {
        return variant2;
    }

    public int getCorrectVariant() {
        return correctVariant;
    }
}
