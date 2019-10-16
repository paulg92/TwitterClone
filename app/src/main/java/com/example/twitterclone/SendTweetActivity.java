package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity {

    private EditText mSendTweet;
    private ListView mListView;
    private Button mButtonSendTweet;
    private Button mButtonViewTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        mSendTweet = findViewById(R.id.TweetMessage);
        mButtonSendTweet = findViewById(R.id.buttonSendTweet);
        mListView = findViewById(R.id.viewTweetsListView);
        mButtonViewTweets = findViewById(R.id.btnviewOtherUsersTweets);

        mButtonViewTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
                final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this,
                        tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName",
                        "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
                try {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {
                                for (ParseObject tweetObjects : objects) {
                                    HashMap<String, String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUserName", tweetObjects.getString("user"));
                                    userTweet.put("tweetValue", tweetObjects.getString("tweet"));
                                    tweetList.add(userTweet);
                                }
                                mListView.setAdapter(adapter);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mButtonSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTweet(view);
            }
        });
    }

    public void sendTweet(View view) {
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", mSendTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(SendTweetActivity.this,
                            ParseUser.getCurrentUser().getUsername() + " 's tweet" + "("
                                    + mSendTweet.getText().toString() + ")" + " is saved!!",
                            Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                } else {
                    FancyToast.makeText(SendTweetActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
                progressDialog.dismiss();
            }
        });
    }


}
