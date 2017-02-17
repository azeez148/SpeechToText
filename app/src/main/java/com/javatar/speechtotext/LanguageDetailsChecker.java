package com.javatar.speechtotext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.List;

/**
 * Created by gokhan on 17-Feb-17.
 */

public class LanguageDetailsChecker extends BroadcastReceiver {

    private LanguageDetailsCheckResultListener listener;

    private List<String> supportedLanguages;
    private String languagePreference;

    public LanguageDetailsChecker(LanguageDetailsCheckResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle results = getResultExtras(true);

        if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
            languagePreference = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
        }
        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            supportedLanguages =  results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
        }
        listener.onResult(languagePreference, supportedLanguages);
    }

    public interface LanguageDetailsCheckResultListener {
        public void onResult(String languagePreference, List<String> supportedLanguages);
    }
}