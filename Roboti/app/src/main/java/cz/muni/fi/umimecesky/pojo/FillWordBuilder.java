package cz.muni.fi.umimecesky.pojo;

public class FillWordBuilder {
    private long id;
    private String wordMissing;
    private String wordFilled;
    private String variant1;
    private String variant2;
    private int correctVariant;
    private String explanation;
    private int grade;
    private boolean visibility;

    public FillWordBuilder id(long id) {
        this.id = id;
        return this;
    }

    public FillWordBuilder wordMissing(String wordMissing) {
        this.wordMissing = wordMissing;
        return this;
    }

    public FillWordBuilder wordFilled(String wordFilled) {
        this.wordFilled = wordFilled;
        return this;
    }

    public FillWordBuilder variant1(String variant1) {
        this.variant1 = variant1;
        return this;
    }

    public FillWordBuilder variant2(String variant2) {
        this.variant2 = variant2;
        return this;
    }

    public FillWordBuilder correctVariant(int correctVariant) {
        this.correctVariant = correctVariant;
        return this;
    }

    public FillWordBuilder explanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    public FillWordBuilder grade(int grade) {
        this.grade = grade;
        return this;
    }

    public FillWordBuilder visibility(boolean visibility) {
        this.visibility = visibility;
        return this;
    }

    public FillWord createFillWord() {
        return new FillWord(id, wordMissing, wordFilled, variant1, variant2, correctVariant, explanation, grade, visibility);
    }
}