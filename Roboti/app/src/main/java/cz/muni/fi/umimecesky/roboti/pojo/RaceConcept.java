package cz.muni.fi.umimecesky.roboti.pojo;

import java.util.List;

public class RaceConcept {

    private String name;
    private List<Integer> categoryIds;
    private int numberOfLevels;
    private int currentLevel;

    public RaceConcept(String name, List<Integer> categoryIds, int numberOfLevels) {
        this.name = name;
        this.categoryIds = categoryIds;
        this.numberOfLevels = numberOfLevels;
        currentLevel = 1;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public String toString() {
        return "RaceConcept{" +
                "name='" + name + '\'' +
                ", categoryIds=" + categoryIds +
                ", numberOfLevels=" + numberOfLevels +
                ", currentLevel=" + currentLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceConcept that = (RaceConcept) o;

        if (numberOfLevels != that.numberOfLevels) return false;
        if (currentLevel != that.currentLevel) return false;
        if (!name.equals(that.name)) return false;
        return categoryIds.equals(that.categoryIds);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + categoryIds.hashCode();
        result = 31 * result + numberOfLevels;
        return result;
    }
}
