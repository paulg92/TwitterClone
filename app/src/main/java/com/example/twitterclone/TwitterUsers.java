package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<String> tUsers;
    private ArrayAdapter mAdapter;
    private String followedUsers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        FancyToast.makeText(this, "welcome " + ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

        mListView = findViewById(R.id.viewTweetsListView);
        tUsers = new ArrayList<>();
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser twitterUsers : objects) {
                            tUsers.add(twitterUsers.getUsername());
                        }
                        mListView.setAdapter(mAdapter);

                        for (String twitterUsers : tUsers) {
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUsers)) {
                                    followedUsers = followedUsers + twitterUsers + "\n";
                                    mListView.setItemChecked(tUsers.indexOf(twitterUsers), true);
                                    FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername() +
                                                    " is following" + followedUsers, Toast.LENGTH_SHORT,
                                            FancyToast.INFO, true).show();
                                }
                            }
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.SendTweet:
                Intent intent = new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()) {
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) +
                    " is followed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));

        } else {
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) +
                    " is not followed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this, "saved",
                            Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
            }
        });
    }
}
