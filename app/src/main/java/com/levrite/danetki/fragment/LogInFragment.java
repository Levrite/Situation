package com.levrite.danetki.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.levrite.danetki.R;
import com.levrite.danetki.activity.MainActivity;
import com.levrite.danetki.model.User;


public class LogInFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;
    private SignInButton mSignInGoogleButton;


    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailField = (EditText) v.findViewById(R.id.field_email);
        mPasswordField = (EditText) v.findViewById(R.id.field_password);
        mSignInButton = (Button) v.findViewById(R.id.button_sign_in);
        mSignUpButton = (Button) v.findViewById(R.id.button_sign_up);
        mSignInGoogleButton = (SignInButton) v.findViewById(R.id.button_sign_in_google);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Вход");

        mSignUpButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mSignInGoogleButton.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            mGoogleApiClient.connect();
            onAuthSuccess();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void onAuthSuccess(){

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new OnlineQuestionListFragment())
                .commit();

    }

    private void onAuthSuccess(FirebaseUser firebaseUser){

        String username = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();


        User user = new User(username, email);
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new OnlineQuestionListFragment())
                .commit();

    }

    private void signUp(){

         getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .commit();

    }

    private void signInGoogle(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                Log.d("TAG_DAN", "ActivityResult Success");
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                firebaseAuthWithGoogle(googleSignInAccount);
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        }else{
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(){
        if(!validateFormLogin()){
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    onAuthSuccess();
                }else{
                    Toast.makeText(getActivity(), "Ошибка входа", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateFormLogin(){

        boolean result = true;
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_sign_up:
                signUp();
                break;
            case R.id.button_sign_in:
                signIn();
                break;
            case R.id.button_sign_in_google:
                signInGoogle();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
