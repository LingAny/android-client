package ru.tp.lingany.lingany.fragments.fragmentData;

import java.io.Serializable;
import java.util.List;

import ru.tp.lingany.lingany.sdk.api.trainings.Training;

public class TranslationData extends FragmentData implements Serializable {
    private Training currentTraining;
    private int currentTrainingNumber;
    private boolean isFilled;


    public TranslationData(List<Training> trainings) {
        super(trainings);
        isFilled = false;
    }

    public Training getCurrentTraining() {
        return currentTraining;
    }

    public void setCurrentTraining(Training currentTraining) {
        this.currentTraining = currentTraining;
    }

    public int getCurrentTrainingNumber() {
        return currentTrainingNumber;
    }

    public void setCurrentTrainingNumber(int currentTrainingNumber) {
        this.currentTrainingNumber = currentTrainingNumber;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }
}
