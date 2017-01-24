package cz.muni.fi.umimecesky.roboti.pojo;

// TODO : implements Serializable

public class FillWord {

    private long id;
    private String wordMissing;
    private String wordFilled;
    private String variant1;
    private String variant2;
    private int correctVariant;
    private String explanation;
    private int grade;
    private boolean visibility;

    FillWord(long id, String wordMissing, String wordFilled, String variant1, String variant2,
             int correctVariant, String explanation, int grade, boolean visibility) {
        this.id = id;
        this.wordMissing = wordMissing;
        this.wordFilled = wordFilled;
        this.variant1 = variant1;
        this.variant2 = variant2;
        this.correctVariant = correctVariant;
        this.explanation = explanation;
        this.grade = grade;
        this.visibility = visibility;
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
                ", explanation='" + explanation + '\'' +
                ", grade=" + grade +
                ", visibility=" + visibility +
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

    public String getExplanation() {
        return explanation;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isVisible() {
        return visibility;
    }
}
