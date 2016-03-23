package com.quanghoa.englishcheat;

/**
 * Created by voqua on 3/21/2016.
 */
public class Word {
    private String display;
    private String pronounce;
    private String audioLink;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public Word(String text){
        text = text.trim();
        setDisplay(text);
        setPronounce("(" + text + "??)");
        setAudioLink("");
    }

    public String toString(){
        return String.format("%s : pronounce %s url %s",display, pronounce, audioLink);
    }
}
