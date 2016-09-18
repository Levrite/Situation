package com.levrite.danetki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levrite.danetki.R;
import com.levrite.danetki.adapter.CategoryAdapter;
import com.levrite.danetki.model.Category;
import com.levrite.danetki.util.DataSource;

import java.util.List;

/**
 * Created by Michael Zaytsev on 09.09.2016.
 */
public class CategoryListFragment extends Fragment{

    private DataSource mDataSource;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycleCategory);

        mDataSource = new DataSource(getActivity());
        mDataSource.open();
        mCategory = mDataSource.getAllCategory();
        mDataSource.close();

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mCategoryAdapter = new CategoryAdapter(mCategory, getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mCategoryAdapter);

    }

}
