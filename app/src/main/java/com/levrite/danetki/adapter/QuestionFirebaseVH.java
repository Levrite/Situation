package com.levrite.danetki.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.levrite.danetki.R;
import com.levrite.danetki.model.QuestionFirebase;

/**
 * Created by Michael Zaytsev on 21.09.2016.
 */

public class QuestionFirebaseVH extends RecyclerView.ViewHolder {

    public TextView textArticle;
    public TextView textDescFire;
    public TextView textAuthor;
    public TextView textDate;

    public QuestionFirebaseVH(View itemView) {
        super(itemView);

        textArticle = (TextView) itemView.findViewById(R.id.textArticleFire);
        textDescFire = (TextView) itemView.findViewById(R.id.textDescFire);
        textAuthor = (TextView) itemView.findViewById(R.id.textAuthor);
        textDate = (TextView) itemView.findViewById(R.id.textDate);
    }

    public void bindToListFire(QuestionFirebase questionFirebase){

        textArticle.setText(questionFirebase.article);
        textDescFire.setText(questionFirebase.question);
        textAuthor.setText(questionFirebase.author);
        textDate.setText(questionFirebase.date);

    }


}
