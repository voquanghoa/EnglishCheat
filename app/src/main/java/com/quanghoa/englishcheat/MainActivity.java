package com.quanghoa.englishcheat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements TranslateController.Translate {

    private TranslateController translateController;
    private AudioPlayerManager audioPlayerManager;

    private EditText inputText;
    private EditText outputText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        translateController = new TranslateController();
        audioPlayerManager = new AudioPlayerManager();

        inputText = (EditText)findViewById(R.id.textInput);
        outputText = (EditText)findViewById(R.id.textOutput);
    }

    protected void hideKeyboard(){
        runOnUiThread(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText()){
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });
    }

    public void translate(View view){
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

    public void copyResult(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text", outputText.getText());
        clipboard.setPrimaryClip(clip);
    }

    public void listen(View view) {
        String textInputContent =" " + inputText.getText().toString() + " ";
        int startText = inputText.getSelectionStart();

        int startId = startText;
        int endId = startText;

        while (startId >0 && Character.isLetter(textInputContent.charAt(startId))){
            startId--;
        }

        while (endId < textInputContent.length() && Character.isLetter(textInputContent.charAt(endId))){
            endId++;
        }

        if(startId+1>=endId){
            Toast.makeText(this, "Please move the cursor inside a word !", Toast.LENGTH_SHORT).show();
            return;
        }

        String wordText = textInputContent.substring(startId+1, endId);

        Word wordObj = translateController.getWord(wordText);
        if(wordObj!=null){
            String audioLink = wordObj.getAudioLink();
            if(audioLink.length()>3){
                try {
                    audioPlayerManager.play(audioLink);
                } catch (IOException e) {
                    Toast.makeText(this, String.format("Could not play word %s !\n%s\n%s", wordText, e.getMessage(), audioLink), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "This word could not be played.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Should translate all first !", Toast.LENGTH_SHORT).show();
        }
    }
}
