package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtEmail, mEdtUsername, mEdtPassword;
    private Button mButtonSignUp, mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        mEdtEmail = findViewById(R.id.editEmailMain);
        mEdtUsername = findViewById(R.id.editUsernameMain);
        mEdtPassword = findViewById(R.id.editPasswordMain);

        mEdtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(mButtonSignUp);
                }
                return false;
            }
        });

        mButtonSignUp = findViewById(R.id.SignUpButtonMain);
        mButtonLogin = findViewById(R.id.LoginButtonMain);

        mButtonSignUp.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser();
            //ParseUser.logOut();
            transitionToTwitterUsersActivity();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.SignUpButtonMain:
                if (mEdtEmail.getText().toString().equals("") || mEdtUsername.getText().toString()
                        .equals("") || mEdtPassword.getText().toString().equals("")) {
                    FancyToast.makeText(MainActivity.this, "You must enter an Email," +
                                    "Username and a Password", FancyToast.LENGTH_LONG,
                            FancyToast.INFO, true).show();
                    return;
                }
                final ParseUser appUser = new ParseUser();
                appUser.setEmail(mEdtEmail.getText().toString());
                appUser.setUsername(mEdtUsername.getText().toString());
                appUser.setPassword(mEdtPassword.getText().toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signin in " + mEdtUsername.getText().toString());
                progressDialog.show();

                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(MainActivity.this, appUser.getUsername()
                                            + " is signed up succesfully", FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS, true).show();
                            transitionToTwitterUsersActivity();
                        } else {
                            FancyToast.makeText(MainActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
            case R.id.LoginButtonMain:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void transitionToTwitterUsersActivity() {
        Intent intent = new Intent(MainActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}
