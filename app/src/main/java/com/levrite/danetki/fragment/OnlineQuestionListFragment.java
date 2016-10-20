package com.levrite.danetki.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.levrite.danetki.R;
import com.levrite.danetki.activity.MainActivity;
import com.levrite.danetki.adapter.QuestionFirebaseVH;
import com.levrite.danetki.model.QuestionFirebase;

/**
 * Created by Michael Zaytsev on 23.09.2016.
 */

public class OnlineQuestionListFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<QuestionFirebase, QuestionFirebaseVH> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ProgressBar mProgressBar;

    public OnlineQuestionListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) view.findViewById(R.id.recycleQuestion);
        mRecycler.setHasFixedSize(true);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progBar);
        mProgressBar.setVisibility(View.VISIBLE);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Список данеток");

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mAdapter = new FirebaseRecyclerAdapter<QuestionFirebase, QuestionFirebaseVH>(QuestionFirebase.class, R.layout.card_firebasequestion, QuestionFirebaseVH.class, mDatabase.child("questions")) {

            @Override
            protected void populateViewHolder(QuestionFirebaseVH viewHolder, final QuestionFirebase model, int position) {

                DatabaseReference postRef = getRef(position);
                final String question_key = postRef.getKey();
                final String uid_key = model.uid;
                final String question_name = model.article;
                final String question_text = model.question;
                final String username = model.author;

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundleData = new Bundle();
                        bundleData.putString("question_key", question_key);
                        bundleData.putString("uid_key", uid_key);
                        bundleData.putString("question_name", question_name);
                        bundleData.putString("question_text", question_text);
                        bundleData.putString("username", username);

                        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
                        detailQuestionFragment.setArguments(bundleData);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, detailQuestionFragment)
                                .addToBackStack(null)
                                .commit();



                    }
                });

                mProgressBar.setVisibility(View.GONE);
                viewHolder.bindToListFire(model);

            }
        };

        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter.cleanup();
        }
    }


}
