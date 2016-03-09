package com.example.clc.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_INDEX = "index";
    private static final String SPIN_CHEAT = "quiz_is_cheat";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private boolean mIsCheater;

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.qu_a, true),
            new TrueFalse(R.string.qu_b, false),
            new TrueFalse(R.string.qu_c, false),
            new TrueFalse(R.string.qu_d, true),
            new TrueFalse(R.string.qu_e, true),
    };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            if (savedInstanceState.getBoolean(SPIN_CHEAT, false)) {
                mIsCheater = savedInstanceState.getBoolean(SPIN_CHEAT);
            }
        }
        updateQuestion();
        mNextButton = (Button) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        //点击
        mTrueButton.setOnClickListener(this);
        mFalseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mCheatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
            case R.id.next_button:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                break;
            case R.id.cheat_button:
                Intent i = new Intent(this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
        }
    }

    //保存设备旋转前的数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(SPIN_CHEAT, mIsCheater);
    }

    //更新问题
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mIsCheater = false;
        mQuestionTextView.setText(question);
    }

    //核实问题
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    //onActivityResult()方法的实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }
}
