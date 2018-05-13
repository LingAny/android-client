package ru.tp.lingany.lingany.fragments.fragmentData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tp.lingany.lingany.sdk.api.trainings.Training;

public class TranslationData extends FragmentData implements Serializable {
    private int currentTrainingNumber;
    private boolean isFilled;

    private HashMap<Integer, String> randomWords;
    private int answerPosition;

    public TranslationData(List<Training> trainings) {
        super(trainings);
        isFilled = false;
        randomWords = new HashMap<>();
        currentTrainingNumber = -1;
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

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public Map<Integer, String> getRandomWords() {
        return randomWords;
    }

    public int getAnswerPosition() {
        return answerPosition;
    }

    public void setAnswerPosition(int answerPosition) {
        this.answerPosition = answerPosition;
    }

    public void clearRandomWords() {
        randomWords.clear();
    }
}
