package com.levrite.danetki.activity;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levrite.danetki.R;
import com.levrite.danetki.fragment.CategoryListFragment;
import com.levrite.danetki.fragment.LogInFragment;
import com.levrite.danetki.fragment.NewQuestionFragment;
import com.levrite.danetki.fragment.RulesFragment;
import com.levrite.danetki.model.User;

public class MainActivity extends AppCompatActivity  {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mTextNavUsername;
    private TextView mTextNavEmail;
    private DatabaseReference mDatabaseUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nvView);

        View v = mNavigationView.getHeaderView(0);
        mTextNavEmail = (TextView) v.findViewById(R.id.text_nav_email);
        mTextNavUsername = (TextView) v.findViewById(R.id.text_nav_username);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            mDatabaseUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mDatabaseUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    mTextNavUsername.setText(user.username);
                    mTextNavEmail.setText(user.email);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            mTextNavEmail.setText("Добро пожаловать");
            mTextNavUsername.setText("Гость");
        }

        mToolbar.setTitle(R.string.offline_mode);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new CategoryListFragment())
                .commit();

        setupDrawerContent(mNavigationView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.nav_offline:
                fragment = new CategoryListFragment();
                break;
            case R.id.nav_online:
                fragment = new LogInFragment();
                break;
            case R.id.nav_send_situation:
                fragment = new NewQuestionFragment();
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                break;
            case R.id.nav_rules:
                fragment = new RulesFragment();
                break;
            default:
                fragment = new CategoryListFragment();
        }
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            menuItem.setChecked(true);
            mToolbar.setTitle(menuItem.getTitle());
        }
            mDrawerLayout.closeDrawers();

    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }
        else{
            super.onBackPressed();
        }
    }

}
