package ru.tp.lingany.lingany.fragments.fragmentData;

import java.util.List;

import ru.tp.lingany.lingany.sdk.api.trainings.Training;

public class FragmentData {
    private List<Training> trainings;

    public FragmentData(List<Training> trainings) {
        this.trainings = trainings;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
