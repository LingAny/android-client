package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.fragmentData.TypingData;

import static android.view.View.INVISIBLE;

public class TypingFragment extends Fragment {
    private TextView wordToTranslate;
    private EditText wordTranslation;
    private TypingData typingData;
    private TextView confirmButton;
    private TextView wordCounter;
    private static final String TYPING_DATA = "TYPING_DATA";

    private Integer currentTrainingNumber;
    private Integer maxTrainingNumber;
    private Integer traningScore = 0;

    public interface TypingListener {
        void onTypingFinished();
    }

    private TypingListener typingListener;

    public static TypingFragment newInstance(TypingData typingData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TYPING_DATA, (Serializable) typingData);

        TypingFragment fragment = new TypingFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            typingData = (TypingData) bundle.getSerializable(TYPING_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {;
        return inflater.inflate(R.layout.fragment_typing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        wordToTranslate = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.wordToTranslateTyping);
        wordTranslation = (EditText) getView().findViewById(R.id.typeTranslation);
        confirmButton = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.confirmButtonTyping);
        wordCounter = getView().findViewById(R.id.containerContextWordCounterTypingWord);
        confirmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processAnswer(view);
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        if (typingData.isFilled()) {
            setTrainingAfterSaveInstance(typingData);
        } else {
            setNewWords(typingData);
        }
    }

    private void setTrainingAfterSaveInstance(TypingData typingData) {
        wordToTranslate.setText(typingData.getTrainings().get(typingData.getTrainingNumber()).getForeignWord());
    }


    private void setNewWords(TypingData typingData) {
        currentTrainingNumber = typingData.getTrainingNumber() + 1;
        maxTrainingNumber = typingData.getTrainings().size();

        typingData.setTrainingNumber(typingData.getTrainingNumber() + 1);

        if (currentTrainingNumber.equals(maxTrainingNumber)) {
            setScore();
            return;
        }

        wordToTranslate.setText(typingData.getTrainings().get(typingData.getTrainingNumber()).getForeignWord());
        wordCounter.setText(makeFractionString(currentTrainingNumber + 1, maxTrainingNumber));
    }

    private void setScore() {
        Integer maxTrainingNumber = typingData.getTrainings().size();

        typingData.setTrainingNumber(typingData.getTrainingNumber() + 1);
        confirmButton.setVisibility(INVISIBLE);
        wordToTranslate.setText(getResources().getString(R.string.scoreString));
        wordTranslation.setText(makeFractionString(traningScore, maxTrainingNumber));
        wordCounter.setText(null);
        View my_view = getView();
        processAnswer(my_view);
    }

    private String makeFractionString(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        return currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
    }

    private void showTraningWords(Handler handler, Integer timeout) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordTranslation.setText("");
                wordTranslation.setTextColor(Color.GRAY);
                setNewWords(typingData);
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
//        TextView textView = (TextView) view;

        disableButtons();
        final Handler handler = new Handler();

        if (typingData.getTrainingNumber() + 1 <= typingData.getTrainings().size()) {

            if (typingData.getTrainings().get(typingData.getTrainingNumber()).getNativeWord().equals(wordTranslation.getText().toString())) {
                wordTranslation.setTextColor(Color.GREEN);
                traningScore++;

            } else {
                wordTranslation.setTextColor(Color.RED);
//            wordTranslation.setHintTextColor(getResources().getColor(R.color.gray));
            }

            showTraningWords(handler, getResources().getInteger(R.integer.delayNextTraining));
        } else {
            finishTraningDelay(handler, getResources().getInteger(R.integer.delayFinishTraining));
        }
    }

    private void disableButtons() {
        confirmButton.setClickable(false);
    }

    private void enableButtons() {
        confirmButton.setClickable(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        typingListener = (TypingListener) context;
    }

    private void finish() {
        typingListener.onTypingFinished();
    }

    public TypingData getTypingData() {
        return typingData;
    }
}
