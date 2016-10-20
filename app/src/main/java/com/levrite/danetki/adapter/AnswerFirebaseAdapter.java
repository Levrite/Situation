package com.levrite.danetki.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.levrite.danetki.R;
import com.levrite.danetki.model.AnswerQuestion;

import java.util.ArrayList;


public class AnswerFirebaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRefAnswer;
    private ChildEventListener mChildEventListener;
    private String mCurrentUid;
    private String mQuestionUid;
    private String mQuestion;
    private String mUsername;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final String STATUS_YES = "1";
    private static final String STATUS_NO = "2";
    private static final String STATUS_DMATTER = "3";
    private static final String STATUS_CORRECT = "4";

    private ArrayList<String> mAnswerIds = new ArrayList<>();
    private ArrayList<AnswerQuestion> mAnswerQuestion = new ArrayList<>();
    private AnswerQuestion mAnswerQuestionItem;
    private AnswerFirebaseVH holderAnswer;

    public AnswerFirebaseAdapter(Context mContext, DatabaseReference mDatabaseRef, String uid, String qUid, Bundle bundle) {
        this.mContext = mContext;
        this.mDatabaseRef = mDatabaseRef;
        mCurrentUid = uid;
        mQuestionUid = qUid;
        this.mQuestion = bundle.getString("question_text");
        mUsername = bundle.getString("username");

        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AnswerQuestion answerQuestion = dataSnapshot.getValue(AnswerQuestion.class);

                mAnswerIds.add(dataSnapshot.getKey());
                mAnswerQuestion.add(answerQuestion);
                notifyItemInserted(mAnswerQuestion.size());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                AnswerQuestion answerQuestion = dataSnapshot.getValue(AnswerQuestion.class);
                String answerKey = dataSnapshot.getKey();
                int answerIndex = mAnswerIds.indexOf(answerKey);
                mAnswerQuestion.set(answerIndex, answerQuestion);
                notifyDataSetChanged();
                notifyItemChanged(answerIndex);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseRef.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if(viewType == TYPE_HEADER){
            View view = layoutInflater.inflate(R.layout.header_question_firebase, parent, false);
            return new AnswerHeader(view);
        }else{
            View view = layoutInflater.inflate(R.layout.card_firebase_answer, parent, false);
            return new AnswerFirebaseVH(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final int positionKey = position;

        if(holder instanceof AnswerHeader){

            AnswerHeader holderHeader = (AnswerHeader) holder;
            holderHeader.textQuestion.setText(mQuestion);
            holderHeader.textUsername.setText(mUsername);

        }else if(holder instanceof AnswerFirebaseVH){

            mAnswerQuestionItem = getItem(position);
            holderAnswer = (AnswerFirebaseVH) holder;
            holderAnswer.textUsername.setText(mAnswerQuestionItem.author);
            holderAnswer.textAnswer.setText(mAnswerQuestionItem.answer);

            switch (mAnswerQuestionItem.status){
                case STATUS_YES:
                    holderAnswer.textSolutions.setText("Да");
                    holderAnswer.textSolutions.setTextColor(ContextCompat.getColor(mContext, R.color.yes));
                    break;
                case STATUS_NO:
                    holderAnswer.textSolutions.setText("Нет");
                    holderAnswer.textSolutions.setTextColor(ContextCompat.getColor(mContext, R.color.no));
                    break;
                case STATUS_DMATTER:
                    holderAnswer.textSolutions.setText("Не имеет значения");
                    holderAnswer.textSolutions.setTextColor(ContextCompat.getColor(mContext, R.color.dmatter));
                    break;
                case STATUS_CORRECT:
                    holderAnswer.textAnswer.setText("Правильный ответ");
                    holderAnswer.textAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.correct));
                    holderAnswer.linearLayoutAnswer.setVisibility(View.GONE);
                    break;
            }

            if (!mQuestionUid.equals(mCurrentUid)) {
                holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
            }

            if(mAnswerQuestionItem.status.equals("0")) {
                final String keyAnswer = mAnswerIds.get(positionKey - 1);
                holderAnswer.yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerQuestionItem.status = STATUS_YES;
                        holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
                        mDatabaseRef.child(keyAnswer).setValue(mAnswerQuestionItem);
                    }
                });
                holderAnswer.noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerQuestionItem.status = STATUS_NO;
                        holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
                        mDatabaseRef.child(keyAnswer).setValue(mAnswerQuestionItem);
                    }
                });
                holderAnswer.dmatterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerQuestionItem.status = STATUS_DMATTER;
                        holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
                        mDatabaseRef.child(keyAnswer).setValue(mAnswerQuestionItem);
                    }
                });
                holderAnswer.correctButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerQuestionItem.status = STATUS_CORRECT;
                        holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
                        mDatabaseRef.child(keyAnswer).setValue(mAnswerQuestionItem);
                    }
                });
            }else{
                holderAnswer.linearLayoutChooser.setVisibility(View.GONE);
            }
        }


    }

    private AnswerQuestion getItem(int position){
        return mAnswerQuestion.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mAnswerQuestion.size() + 1;
    }

    static class AnswerFirebaseVH extends RecyclerView.ViewHolder {

        public TextView textUsername;
        public TextView textAnswer;
        public TextView textSolutions;
        /** TextView as button START**/
        public TextView yesButton;
        public TextView noButton;
        public TextView dmatterButton;
        public TextView correctButton;
        /** TextView as button END**/
        public LinearLayout linearLayoutChooser;
        public LinearLayout linearLayoutAnswer;

        public AnswerFirebaseVH(View itemView) {
            super(itemView);

            textUsername = (TextView) itemView.findViewById(R.id.text_username);
            textAnswer = (TextView) itemView.findViewById(R.id.text_answer);
            textSolutions = (TextView) itemView.findViewById(R.id.text_solutions);
            yesButton = (TextView) itemView.findViewById(R.id.yesButton);
            noButton = (TextView) itemView.findViewById(R.id.noButton);
            dmatterButton = (TextView) itemView.findViewById(R.id.dMetterButton);
            correctButton = (TextView) itemView.findViewById(R.id.correctButton);
            linearLayoutChooser = (LinearLayout) itemView.findViewById(R.id.chooser_linear_layout);
            linearLayoutAnswer = (LinearLayout) itemView.findViewById(R.id.answer_solutions_linear_layout);

        }
    }

    static class AnswerHeader extends RecyclerView.ViewHolder {

        public TextView textQuestion;
        public TextView textUsername;

        public AnswerHeader(View itemView) {
            super(itemView);
            textUsername = (TextView) itemView.findViewById(R.id.text_username_header);
            textQuestion = (TextView) itemView.findViewById(R.id.text_question_header);
        }
    }

}
