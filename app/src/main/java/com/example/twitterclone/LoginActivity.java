package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtEmail, mEdtPassword;
    private Button mLoginButton, mSignUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setTitle("Log In");

        mEdtEmail = findViewById(R.id.edtEmailLoginActivity);
        mEdtPassword = findViewById(R.id.edtPasswordLoginActivity);

        mLoginButton = findViewById(R.id.btnLoginActivityLogin);
        mSignUpButton = findViewById(R.id.btnSignUpLoginActivity);

        mLoginButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser();
            //ParseUser.logOut();
            transitionToTwitterUsersActivity();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginActivityLogin:
                ParseUser.logInInBackground(mEdtEmail.getText().toString(), mEdtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            FancyToast.makeText(LoginActivity.this,
                                    user.get("username") + "" +
                                            " is logged in succesfully", FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS, true).show();
                            transitionToTwitterUsersActivity();
                        } else {
                            FancyToast.makeText(LoginActivity.this,
                                    e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                    true).show();
                        }
                    }
                });
                break;
            case R.id.btnSignUpLoginActivity:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
        }
    }

    private void transitionToTwitterUsersActivity() {
        Intent intent = new Intent(LoginActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }

}
