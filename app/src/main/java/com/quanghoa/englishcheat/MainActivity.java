package com.quanghoa.englishcheat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TranslateController.Translate {

    private TranslateController translateController;
    private EditText inputText;
    private EditText outputText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        translateController = new TranslateController();

        inputText = (EditText)findViewById(R.id.textInput);
        outputText = (EditText)findViewById(R.id.textOutput);
    }

    protected void hideKeyboard(){
        runOnUiThread(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    public void translate(View view){
        hideKeyboard();
        translateController.translate(inputText.getText().toString(), this);
    }

    public void onUpdate(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                outputText.setText(text);
            }
        });
    }

    public void onFail() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this,"Network error !!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
