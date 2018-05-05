package ru.tp.lingany.lingany.fragments.fragmentData;

import java.io.Serializable;
import java.util.List;

import ru.tp.lingany.lingany.sdk.api.trainings.Training;

public class TypingData extends FragmentData implements Serializable {

    private int trainingNumber;
    private boolean isFilled;

    public TypingData(List<Training> trainings) {
        super(trainings);
        isFilled = false;
        trainingNumber = -1;
    }

    public int getTrainingNumber() {
        return trainingNumber;
    }

    public void setTrainingNumber(int trainingNumber) {
        this.trainingNumber = trainingNumber;
        isFilled = true;
    }

    public boolean isFilled() {
        return isFilled;
    }
}
