package com.levrite.danetki.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.levrite.danetki.R;
import com.levrite.danetki.fragment.QuestionFragment;

import com.levrite.danetki.model.Question;
import com.levrite.danetki.util.DataSource;

import java.util.List;

/**
 * Created by Michael Zaytsev on 08.09.2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    List<Question> questionList;
    Context mContext;
    DataSource mDataSource;

    public QuestionAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.mContext = context;
        mDataSource = new DataSource(context);
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.question_card, null);
        QuestionHolder questionHolder = new QuestionHolder(view);
        return questionHolder;
    }

    @Override
    public void onBindViewHolder(final QuestionHolder holder, final int position) {
        holder.textArticle.setText(questionList.get(position).getArticle());
        if (questionList.get(position).getDone().equals("1")) {
            holder.checkGuess.setChecked(true);
            holder.textArticle.setTextColor(mContext.getResources().getColor(R.color.colorCheck));
        }else{
            holder.checkGuess.setChecked(false);
            holder.textArticle.setTextColor(mContext.getResources().getColor(R.color.colorListGrey));
        }

        if(questionList.get(position).getFavorite().equals("1")){
            holder.checkFavorite.setChecked(true);
        }else{
            holder.checkFavorite.setChecked(false);
        }

        holder.cardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("article", questionList.get(position).getArticle());
                bundle.putString("question", questionList.get(position).getQuestion());
                bundle.putString("answer", questionList.get(position).getAnswer());
                bundle.putString("id", questionList.get(position).getId() + "");
                bundle.putString("done", questionList.get(position).getDone());

                QuestionFragment questionFragment = new QuestionFragment();
                questionFragment.setArguments(bundle);

                AppCompatActivity appCompatActivity = (AppCompatActivity) mContext;
                appCompatActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, questionFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        holder.checkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataSource.open();
                ContentValues contentValues = new ContentValues();
                if (holder.checkFavorite.isChecked()) {
                    contentValues.put("favorit", "1");
                    mDataSource.writeFavorite(contentValues, questionList.get(position).getId() + "");
                    questionList.get(position).setFavorite("1");
                    Toast.makeText(mContext, "Добавлено в избранное", Toast.LENGTH_LONG).show();
                } else {
                    contentValues.put("favorit", "0");
                    mDataSource.writeFavorite(contentValues, questionList.get(position).getId() + "");
                    questionList.get(position).setFavorite("0");
                }
                mDataSource.close();
            }
        });

        holder.checkGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataSource.open();
                ContentValues contentValues = new ContentValues();
                if (holder.checkGuess.isChecked()) {
                    contentValues.put("done", "1");
                    mDataSource.writeDone(contentValues, questionList.get(position).getId() + "");
                    questionList.get(position).setDone("1");
                    Toast.makeText(mContext, "Отгадано", Toast.LENGTH_LONG).show();
                    holder.textArticle.setTextColor(mContext.getResources().getColor(R.color.colorCheck));

                } else {
                    contentValues.put("done", "0");
                    mDataSource.writeDone(contentValues, questionList.get(position).getId() + "");
                    questionList.get(position).setDone("0");
                    holder.textArticle.setTextColor(mContext.getResources().getColor(R.color.colorListGrey));
                }
                mDataSource.close();
            }
        });
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        CardView cardQuestion;
        TextView textArticle;
        CheckBox checkGuess;
        CheckBox checkFavorite;

        public QuestionHolder(View itemView) {

            super(itemView);
            cardQuestion = (CardView) itemView.findViewById(R.id.cardQuestion);
            textArticle = (TextView) itemView.findViewById(R.id.textQuestionArticle);
            checkFavorite = (CheckBox) itemView.findViewById(R.id.checkFavorite);
            checkGuess = (CheckBox) itemView.findViewById(R.id.checkGuess);

        }


    }


}
