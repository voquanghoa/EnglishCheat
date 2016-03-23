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

    private void addWord(String word, String pronounce){
        Word wordObj = new Word();
        wordObj.setDisplay(word);
        wordObj.setPronounce(pronounce);
        words.add(wordObj);
    }

    private synchronized Word getWord(String word){
        for(int i=0;i<words.size();i++){
            Word wordObj = words.get(i);
            if(wordObj.getDisplay().equals(word)) {
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
            String word = words[i].trim();
            if(word!=null && word.length()>0){
                word = word.trim().toLowerCase();

                if(!isWord(word)){
                    sb.append(word+" ");
                }else{
                    Word wordObj = getWord(word);
                    if(wordObj!=null){
                        sb.append(" "+wordObj.getPronounce());
                    }else{
                        String pronounce = wordTranslate.translate(word);
                        addWord(word, pronounce);
                        sb.append(" " + pronounce);
                    }
                }

                translate.onUpdate(sb.toString());
            }
        }
    }
}
