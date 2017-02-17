package com.javatar.speechtotext;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LanguageDetailsChecker.LanguageDetailsCheckResultListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected static final int SPEECH_REQUEST_CODE = 1001;

    private ImageButton speakButton;
    private TextView resultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //See the logs which languages are available
        checkLanguages();

        resultTextView = (TextView) findViewById(R.id.resultText);
        speakButton = (ImageButton) findViewById(R.id.speakButton);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(intent, SPEECH_REQUEST_CODE);
                    resultTextView.setText("");
                } catch (ActivityNotFoundException a) {
                    Log.e(TAG, "Does not support STT");
                }
            }
        });
    }

    private void checkLanguages() {
        LanguageDetailsChecker languageDetailsChecker = new LanguageDetailsChecker(this);
        Intent detailsIntent =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(detailsIntent, null, languageDetailsChecker, null, Activity.RESULT_OK, null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTextView.setText(text.get(0));
            }
        }
    }

    @Override
    public void onResult(String languagePreference, List<String> supportedLanguages) {
        if(languagePreference != null) {
            Log.i(TAG, "languagePreference: "+  languagePreference);
        }

        if(supportedLanguages!= null) {
            for(String language : supportedLanguages) {
                Log.i(TAG, "Supoorted Language: " + language );
            }
        }
    }
}
