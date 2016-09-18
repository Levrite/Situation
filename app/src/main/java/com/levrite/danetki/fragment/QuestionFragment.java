package com.levrite.danetki.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.levrite.danetki.R;
import com.levrite.danetki.util.DataSource;

/**
 * Created by Michael Zaytsev on 08.09.2016.
 */
public class QuestionFragment extends Fragment implements View.OnClickListener {

    private TextView mTextArticle;
    private TextView mTextQuestion;
    private Button mButtonShowAnswer;
    private ImageView mShare;
    private CheckBox mCheck;
    private Animation mAnimation;
    private boolean isAnswer = true;
    private DataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextArticle = (TextView) getView().findViewById(R.id.textArcticle);
        mTextQuestion = (TextView) getView().findViewById(R.id.textQuestion);
        mButtonShowAnswer = (Button) getView().findViewById(R.id.buttonAnswer);
        mShare = (ImageView) getView().findViewById(R.id.share);
        mCheck = (CheckBox) getView().findViewById(R.id.check);
        mDataSource = new DataSource(getActivity());

        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

        mButtonShowAnswer.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mCheck.setOnClickListener(this);

        mTextArticle.setText(getArguments().getString("article"));
        mTextQuestion.setText(getArguments().getString("question"));
        mTextQuestion.setAnimation(mAnimation);
        mButtonShowAnswer.setAnimation(mAnimation);
        if (getArguments().getString("done").equals("1")) {
            mCheck.setChecked(true);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAnswer:
                if (isAnswer) {
                    mTextQuestion.setText(getArguments().getString("answer"));
                    mButtonShowAnswer.setText("Показать вопрос");
                    isAnswer = false;
                } else {
                    mTextQuestion.setText(getArguments().getString("question"));
                    mButtonShowAnswer.setText("Показать ответ");
                    isAnswer = true;
                }
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getArguments().getString("article") + "\n" + getArguments().getString("question") + "\nНайти ответ и больше данеток можно в приложении");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
            case R.id.check:
                ContentValues contentValues = new ContentValues();
                mDataSource.open();
                if (mCheck.isChecked()) {
                    contentValues.put("done", "1");
                    if (getArguments().getString("id") != null) {
                        mDataSource.writeDone(contentValues, getArguments().getString("id"));
                    }
                    Toast.makeText(getActivity(), "Отгадано", Toast.LENGTH_LONG).show();

                } else {
                    contentValues.put("done", "0");
                    if (getArguments().getString("id") != null) {
                        mDataSource.writeDone(contentValues, getArguments().getString("id"));
                    }
                }
                mDataSource.close();
                break;
        }


    }
}
