package cz.muni.fi.umimecesky.roboti.pojo;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.utils.Utils;

public class RaceConcept implements Serializable {

    private String name;
    private List<Integer> categoryIDs;
    private int numberOfLevels;
    private int currentLevel;

    public RaceConcept(String name, List<Integer> categoryIDs, int numberOfLevels) {
        this.name = name;
        this.categoryIDs = categoryIDs;
        this.numberOfLevels = numberOfLevels;
        currentLevel = 1;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCategoryIDs() {
        return categoryIDs;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    private void setCurrentLevel(int currentLevel) {
        if (currentLevel <= numberOfLevels) {
            this.currentLevel = currentLevel;
        }
    }

    public void increaseLevel(Context context) {
        setCurrentLevel(currentLevel+1);
        Utils.updateConcept(context, this);
    }

    @Override
    public String toString() {
        return "RaceConcept{" +
                "name='" + name + '\'' +
                ", categoryIDs=" + categoryIDs +
                ", numberOfLevels=" + numberOfLevels +
                ", currentLevel=" + currentLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceConcept that = (RaceConcept) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
