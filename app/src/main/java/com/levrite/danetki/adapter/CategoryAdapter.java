package com.levrite.danetki.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levrite.danetki.R;
import com.levrite.danetki.fragment.QuestionListFragment;
import com.levrite.danetki.model.Category;
import com.levrite.danetki.util.DataSource;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    private List<Category> categoryList;
    private Context mContext;
    private DataSource mDataSource;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.mContext = context;
        mDataSource = new DataSource(mContext);
    }

    @Override
    public int getItemCount() {
        return  categoryList.size();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        final Category category = categoryList.get(position);
        int idImage = mContext.getResources().getIdentifier(category.getImage(), "drawable", mContext.getPackageName());

        holder.titleText.setText(category.getName());
        mDataSource.open();
        holder.countText.setText("Данеток в категории: " + mDataSource.getCount(category.getId()+""));
        mDataSource.close();
        holder.imageView.setImageResource(idImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idCategory", category.getId()+"");
                QuestionListFragment questionListFragment = new QuestionListFragment();
                questionListFragment.setArguments(bundle);
                AppCompatActivity appCompatActivity = (AppCompatActivity) mContext;
                appCompatActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, questionListFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView titleText;
        TextView countText;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardCategory);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            titleText = (TextView) itemView.findViewById(R.id.titles);
            countText = (TextView) itemView.findViewById(R.id.count);
        }

    }


}
