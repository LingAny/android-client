package ru.tp.lingany.lingany.fragments.fragmentData;

import java.io.Serializable;
import java.util.List;

import ru.tp.lingany.lingany.sdk.api.trainings.Training;

public class SprintData extends FragmentData implements Serializable {
    private int currentTrainingNumber;
    private boolean isFilled;
    private boolean victories = false;
    private int markAndCrossLength = 0;
    private String visibleTranslation;

    public SprintData(List<Training> trainings) {
        super(trainings);
        this.currentTrainingNumber = -1;
        isFilled = false;
    }

    public int getCurrentTrainingNumber() {
        return currentTrainingNumber;
    }

    public void setCurrentTrainingNumber(int currentTrainingNumber) {
        this.currentTrainingNumber = currentTrainingNumber;
        isFilled = true;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isVictories() {
        return victories;
    }

    public void setVictories(boolean victories) {
        this.victories = victories;
    }

    public int getMarkAndCrossLength() {
        return markAndCrossLength;
    }

    public void setMarkAndCrossLength(int markAndCrossLength) {
        this.markAndCrossLength = markAndCrossLength;
    }

    public String getVisibleTranslation() {
        return visibleTranslation;
    }

    public void setVisibleTranslation(String visibleTranslation) {
        this.visibleTranslation = visibleTranslation;
    }
}
