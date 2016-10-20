package com.levrite.danetki.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.levrite.danetki.R;
import com.levrite.danetki.activity.MainActivity;
import com.levrite.danetki.model.User;

/**
 * Created by Michael Zaytsev on 01.10.2016.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignUpButton;
    private Button mSignInButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public RegistrationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mUsernameField = (EditText) v.findViewById(R.id.field_usernameReg);
        mEmailField = (EditText) v.findViewById(R.id.field_emailReg);
        mPasswordField = (EditText) v.findViewById(R.id.field_passwordReg);
        mSignInButton = (Button) v.findViewById(R.id.button_sign_in_reg);
        mSignUpButton = (Button) v.findViewById(R.id.button_sign_up_reg);
        mSignUpButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Регистрация");

        return v;
    }

    public void onAuthSuccess(FirebaseUser firebaseUser){

        String userName = mUsernameField.getText().toString();
        String email = mEmailField.getText().toString();
        User user = new User(userName, email);

        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new OnlineQuestionListFragment())
                .commit();

    }

    private boolean validateFormRegistration(){

        boolean result = true;

        if(TextUtils.isEmpty(mUsernameField.getText().toString())){
            mUsernameField.setError("Введите имя пользователя");
            result = false;
        }else {
            mUsernameField.setError(null);
        }

        if(TextUtils.isEmpty(mEmailField.getText().toString())){
            mEmailField.setError("Введите почту");
            result = false;
        }else{
            mEmailField.setError(null);
        }

        if(TextUtils.isEmpty(mPasswordField.getText().toString())){
            mPasswordField.setError("Введите пароль");
            result = false;
        }else {
            mPasswordField.setError(null);
        }

        return result;

    }

    public void signUp() {
        if (!validateFormRegistration()) {
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                } else {
                    Toast.makeText(getActivity(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                }
            }
        }
        );
    }

    public void signIn(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LogInFragment())
                .commit();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_sign_in_reg:
                signIn();
                break;
            case R.id.button_sign_up_reg:
                signUp();
                break;
        }

    }


}
