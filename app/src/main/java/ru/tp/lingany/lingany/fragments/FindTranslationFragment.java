package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.fragmentData.TranslationData;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.RandArray;

import static android.view.View.INVISIBLE;

public class FindTranslationFragment extends Fragment {
    private List<TextView> buttons = new ArrayList<>();

    public interface FindTranslationListener {
        void onFindTranslationFinished();
    }

    private FindTranslationListener findTranslationFinished;
    private ViewGroup markCrossContainer;
    private TextView wordToTranslate;
    private TextView wordCounter;
    private LayoutInflater inflater;
    private TranslationData translationData;

    private Integer currentTrainingNumber;
    private Integer maxTrainingNumber;
    private Integer traningScore = 0;


    private static final String TRANSLATION_DATA = "TRANSLATION_DATA";


    public static FindTranslationFragment newInstance(TranslationData translationData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSLATION_DATA, (Serializable) translationData);

        FindTranslationFragment fragment = new FindTranslationFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            translationData = (TranslationData) bundle.getSerializable(TRANSLATION_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_find_translations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        buttons.add((TextView) Objects.requireNonNull(getView()).findViewById(R.id.leftUpperButton));
        buttons.add((TextView) getView().findViewById(R.id.leftDownButton));
        buttons.add((TextView) getView().findViewById(R.id.rightUpperButton));
        buttons.add((TextView) getView().findViewById(R.id.rightDownButton));

        markCrossContainer = Objects.requireNonNull(getView()).findViewById(R.id.containerMarkAndCross);
        wordCounter = getView().findViewById(R.id.containerContextWordCounterFindTranslation);
        wordToTranslate = getView().findViewById(R.id.wordToTranslate);

        for (View button:buttons) {
            button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processAnswer(v);
                    }
                });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (translationData.isFilled()) {
            setTrainingAfterSaveInstance(translationData);
        } else {
            setNewTraining(translationData);
        }
    }

    private void setTrainingAfterSaveInstance(TranslationData translationData) {
        clearMarkAndCross();
        Training currentTraining = translationData.getTrainings().get(translationData.getCurrentTrainingNumber());
        setWordToTranslate(currentTraining.getForeignWord());
        setWordsOnButtons(currentTraining.getNativeWord(), translationData.getRandomWords());
    }

    private void setNewTraining(TranslationData translationData) {
        currentTrainingNumber = translationData.getCurrentTrainingNumber() + 1;
        maxTrainingNumber = translationData.getTrainings().size();

        if (currentTrainingNumber.equals(maxTrainingNumber)) {
            setScore();
            return;
        }

        translationData.clearRandomWords();
        translationData.setCurrentTrainingNumber(translationData.getCurrentTrainingNumber() + 1);
        if (translationData.getCurrentTrainingNumber() >= translationData.getTrainings().size()) {
            finish();
            return;
        }
        Training currentTraining = translationData.getTrainings().get(translationData.getCurrentTrainingNumber());

        clearMarkAndCross();
        setWordToTranslate(currentTraining.getForeignWord());
        setTranslationButtons(translationData);
        setWordCounter(currentTrainingNumber + 1, maxTrainingNumber);
        translationData.setFilled(true);
    }

    private void setScore() {
        Integer maxTrainingNumber = translationData.getTrainings().size();
        translationData.setCurrentTrainingNumber(translationData.getCurrentTrainingNumber() + 1);

        for (View button:buttons) {
            button.setVisibility(INVISIBLE);
        }

        String scoreData = getResources().getString(R.string.scoreString) + makeFractionString(traningScore, maxTrainingNumber);
        wordToTranslate.setText(scoreData);
        wordCounter.setText(null);
        processAnswer(getView());
    }

    private String makeFractionString(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        return currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
    }

    public void setWordCounter(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        String resultString = currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
        wordCounter.setText(resultString);
    }

    private void setTranslationButtons(TranslationData translationData) {
        Training currentTraining = translationData.getTrainings().get(translationData.getCurrentTrainingNumber());
        List<Integer> indexes = RandArray.getRandIndexes(3, 0, translationData.getTrainings().size() - 1, translationData.getCurrentTrainingNumber());

        Map<Integer, String> words = translationData.getRandomWords();
        for (Integer index: indexes) {
            words.put(index, translationData.getTrainings().get(index).getNativeWord());
        }

        int answerPosition = (int) (Math.random() * 3);
        translationData.setAnswerPosition(answerPosition);
        setWordsOnButtons(currentTraining.getNativeWord(), words);
    }

    public void setWordsOnButtons(String translationWord, Map<Integer, String> words) {

        Iterator iterator = words.entrySet().iterator();
        for (int i = 0; i < buttons.size(); ++i) {
            if (i == translationData.getAnswerPosition()) {
                buttons.get(i).setText(translationWord);
            } else {
                if (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    buttons.get(i).setText((String) pair.getValue());
                }
            }
        }
    }

    public void setWordToTranslate(String word) {
        wordToTranslate.setText(word);
    }

    public void setMark() {
        clearMarkAndCross();
        final View view = inflater.inflate(R.layout.item_mark, markCrossContainer, false);
        markCrossContainer.addView(view);
    }

    public void setCross() {
        clearMarkAndCross();
        final View view = inflater.inflate(R.layout.item_cross, markCrossContainer, false);
        markCrossContainer.addView(view);
    }

    public void clearMarkAndCross() {
        markCrossContainer.removeAllViews();
    }

    private void showTraningWords(Handler handler, Integer timeout) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNewTraining(translationData);
                enableButtons();
            }
        }, timeout);
    }

    private void finishTraningDelay(Handler handler, Integer timeout) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, timeout);
    }

    private void processAnswer(View view) {
        disableButtons();
        final Handler handler = new Handler();

        if (translationData.getCurrentTrainingNumber() + 1 <= translationData.getTrainings().size()) {
            Training currentTraining = translationData.getTrainings().get(translationData.getCurrentTrainingNumber());
            TextView textView = (TextView) view;

            if (currentTraining != null && currentTraining.getNativeWord() == textView.getText()) {
                setMark();
                traningScore++;
            } else {
                setCross();
            }

            showTraningWords(handler, getResources().getInteger(R.integer.delayNextTraining));
        } else {
            finishTraningDelay(handler, getResources().getInteger(R.integer.delayFinishTraining));
        }
    }

    private void disableButtons() {
        for (TextView button: buttons) {
            button.setClickable(false);
        }
    }

    private void enableButtons() {
        for (TextView button: buttons) {
            button.setClickable(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        findTranslationFinished = (FindTranslationListener) context;
    }

    private void finish() {
        findTranslationFinished.onFindTranslationFinished();
    }

    public TranslationData getTranslationData() {
        return translationData;
    }
}
