package com.quanghoa.englishcheat;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by voqua on 3/21/2016.
 */
public class TranslateController {

    public interface Translate{
        void onUpdate(String text);
        void onFail();
    }

    private ArrayList<Word> words;
    private WordTranslate wordTranslate;

    public TranslateController(){
        words = new ArrayList<>();
        wordTranslate = new WordTranslate();
    }

    private String[] analyst(String sentence){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<sentence.length();i++){
            if(Character.isLetter(sentence.charAt(i))){
                sb.append(sentence.charAt(i));
            }else{
                sb.append(' ' + sentence.charAt(i)+ ' ');
            }
        }

        while (sentence.contains("  ")){
            sentence.replaceAll("  "," ");
        }

        return sentence.split(" ");
    }

    private synchronized void addWord(Word word){
        words.add(word);
    }

    public synchronized Word getWord(String word){
        word = word.trim();
        for(int i=0;i<words.size();i++){
            Word wordObj = words.get(i);
            if(wordObj.getDisplay().equalsIgnoreCase(word)) {
                return wordObj;
            }
        }
        return null;
    }

    public boolean isWord(String word){
        final Pattern p = Pattern.compile("[^a-zA-Z]");
        return !p.matcher(word).find();
    }


    public void translate(final String sentence, final Translate translate){
        new Thread(new Runnable() {
            public void run() {
                try {
                    doTranslate(sentence, translate);
                } catch (IOException e) {
                    e.printStackTrace();
                    translate.onFail();
                }
            }
        }).start();
    }



    private void doTranslate(String sentence, final Translate translate) throws IOException {
        String[] words = analyst(sentence);
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<words.length;i++){
            String textWord = words[i].trim();
            if(textWord!=null && textWord.length()>0){
                textWord = textWord.trim().toLowerCase();

                if(!isWord(textWord)){
                    sb.append(textWord+" ");
                }else{
                    Word wordObj = getWord(textWord);

                    if(wordObj==null){
                        wordObj = wordTranslate.translate(textWord);
                        addWord(wordObj);
                    }
                    sb.append(" " + wordObj.getPronounce());
                }

                translate.onUpdate(sb.toString());
            }
        }
    }
}
