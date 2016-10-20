package com.levrite.danetki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levrite.danetki.R;
import com.levrite.danetki.model.QuestionFirebase;
import com.levrite.danetki.model.Test;
import com.levrite.danetki.model.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Zaytsev on 26.09.2016.
 */

public class NewQuestionFragment extends Fragment implements View.OnClickListener {

    EditText fieldArticle;
    EditText fieldQuestion;
    EditText fieldAnswer;
    Button buttonSendQuestion;

    private DatabaseReference mDatabase;
    ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newquestion, container, false);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LogInFragment())
                    .commit();
        }

        fieldArticle = (EditText) v.findViewById(R.id.field_article);
        fieldQuestion = (EditText) v.findViewById(R.id.field_question);
        fieldAnswer = (EditText) v.findViewById(R.id.field_answer);
        buttonSendQuestion = (Button) v.findViewById(R.id.button_sendquestion);
        buttonSendQuestion.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return v;
    }


    @Override
    public void onClick(View v) {

        if(!validateData()){
            return;
        }
        final String article = fieldArticle.getText().toString();
        final String question = fieldQuestion.getText().toString();
        final String answer = fieldAnswer.getText().toString();
        final String date = DateFormat.getDateInstance().format(new Date());

        setEditingEnabled(false);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);

                    writeNewQuestion(userId, user.username, article, question, answer, date);

                setEditingEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void writeNewQuestion(String userId, String username, String article, String question, String answer, String date ){

        QuestionFirebase questionFirebase = new QuestionFirebase(userId, username, article, question, answer, date);
        String key = mDatabase.child("questions").push().getKey();

        Map<String, Object> questionValues = questionFirebase.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/questions/" + key, questionValues);
        childUpdates.put("/user-questions/" + userId + "/" + key, questionValues);

        mDatabase.updateChildren(childUpdates);

    }

    private void setEditingEnabled(boolean enabled) {
        fieldArticle.setEnabled(enabled);
        fieldAnswer.setEnabled(enabled);
        fieldQuestion.setEnabled(enabled);

        if (enabled) {
            buttonSendQuestion.setVisibility(View.VISIBLE);
        } else {
            buttonSendQuestion.setVisibility(View.GONE);
        }
    }

    private boolean validateData(){

        boolean isValidate = true;
        if(TextUtils.isEmpty(fieldArticle.getText().toString())){
            fieldArticle.setError("Введите название данетки");
            isValidate = false;
        }else {
            fieldArticle.setError(null);
        }

        if(TextUtils.isEmpty(fieldAnswer.getText().toString())){
            fieldAnswer.setError("Введите ответ");
            isValidate = false;
        }else {
            fieldAnswer.setError(null);
        }

        if(TextUtils.isEmpty(fieldQuestion.getText().toString())){
            fieldQuestion.setError("Введите вопрос");
            isValidate = false;
        }else {
            fieldQuestion.setError(null);
        }

        return isValidate;
    }

}
