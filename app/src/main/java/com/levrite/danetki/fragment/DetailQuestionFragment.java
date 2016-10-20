package com.levrite.danetki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levrite.danetki.R;
import com.levrite.danetki.activity.MainActivity;
import com.levrite.danetki.adapter.AnswerFirebaseAdapter;
import com.levrite.danetki.model.AnswerQuestion;
import com.levrite.danetki.model.User;

/**
 * Created by Michael Zaytsev on 01.10.2016.
 */

public class DetailQuestionFragment extends Fragment implements View.OnClickListener{

    private DatabaseReference mQuestionReference;
    private DatabaseReference mAnswerReference;
    private String mQuestionKey;
    private String mQuestionUid; //Question UID
    private String mUid; //Current UID
    private String mTextQuestion;


    private AnswerFirebaseAdapter mAnswerAdapter;
    private EditText mFieldQuestionAnswer;
    private ImageButton mButtonSendAnswer;
    private RecyclerView mRecyclerAnswer;
    private LinearLayout mLinearLayoutAnswer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_answer_firebase, null, false);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mQuestionKey = getArguments().getString("question_key");
        mQuestionUid = getArguments().getString("uid_key");
        Bundle bundle = getArguments();


        mFieldQuestionAnswer = (EditText) v.findViewById(R.id.field_question_answer);
        mButtonSendAnswer = (ImageButton) v.findViewById(R.id.button_send_answer);
        mRecyclerAnswer = (RecyclerView) v.findViewById(R.id.recycleAnswer);
        mLinearLayoutAnswer = (LinearLayout) v.findViewById(R.id.answer_linear_layout);

        if(mQuestionUid.equals(mUid)){
            mLinearLayoutAnswer.setVisibility(View.GONE);
        }

        mQuestionReference = FirebaseDatabase.getInstance().getReference().child("question_answer").child(mQuestionKey);

        mAnswerAdapter = new AnswerFirebaseAdapter(getActivity(), mQuestionReference, mUid, mQuestionUid, bundle);

        mRecyclerAnswer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAnswer.setAdapter(mAnswerAdapter);

        mButtonSendAnswer.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("question_name"));
    }

    @Override
    public void onClick(View v) {


        if(validateData()) {
            FirebaseDatabase.getInstance().getReference().child("users").child(mUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User user = dataSnapshot.getValue(User.class);
                            String username = user.username;
                            String answer = mFieldQuestionAnswer.getText().toString();

                            AnswerQuestion answerQuestion = new AnswerQuestion(username, answer, "0", mUid);
                            mQuestionReference.push().setValue(answerQuestion);

                            mFieldQuestionAnswer.setText(null);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    /**
     * Check text into mFieldQuestionAnswer
     * @retrun true if mFieldQuestionAnswer not empty
     */
    private boolean validateData(){
        if(TextUtils.isEmpty(mFieldQuestionAnswer.getText().toString())){
            mFieldQuestionAnswer.setError("Введите ответ");
            return false;
        }else {
            mFieldQuestionAnswer.setError(null);
        }
        return true;
    }

}
