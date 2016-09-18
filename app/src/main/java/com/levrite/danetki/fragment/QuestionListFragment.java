package com.levrite.danetki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.levrite.danetki.R;
import com.levrite.danetki.adapter.QuestionAdapter;
import com.levrite.danetki.model.Question;
import com.levrite.danetki.util.DataSource;

import java.util.List;

/**
 * Created by Michael Zaytsev on 07.09.2016.
 */
public class QuestionListFragment extends Fragment {

    RecyclerView recyclerView;
    DataSource mDataSource;
    List<Question> questionList;
    QuestionAdapter questionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleQuestion);
        mDataSource = new DataSource(getActivity());
        mDataSource.open();
        if(getArguments().getString("idCategory").equals("3")){
            questionList = mDataSource.getFavorite("1");
        }else {
            questionList = mDataSource.getAllQuestion(getArguments().getString("idCategory"));
        }
        questionAdapter = new QuestionAdapter(questionList, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(questionAdapter);

        mDataSource.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
