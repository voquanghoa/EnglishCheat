package com.quanghoa.englishcheat;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by voqua on 3/21/2016.
 */
public class WordTranslate {
    private static Pattern pronouncePattern = Pattern.compile("<span class=\"ipa\">([^<]*)</span>");
    private static Pattern audioPattern = Pattern.compile("data-src-mp3=\\\"([^\\\"]*)");

    public Word translate(String word) throws IOException {
        return download(word, "http://dictionary.cambridge.org/search/english/direct/?q="+word);
    }

    private void putHeader(HttpURLConnection httpConn){
        httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
    }

    private Word download(String word, String stringUrl) throws IOException {
        Word wordObj = new Word(word);

        URL url = new URL(stringUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        putHeader(httpConn);
        httpConn.connect();

        int httpCode = 0;
        try {
            httpCode = httpConn.getResponseCode();
        }catch (ProtocolException ex){
            Log("Translating "+word + " error");
            ex.printStackTrace();
            httpCode = HttpURLConnection.HTTP_BAD_REQUEST;
        }

        if(httpCode == HttpURLConnection.HTTP_MOVED_PERM){
            String nextPage = httpConn.getHeaderField("Location");
            Log.i("VOQUANGHOA", "Next page = " + nextPage);
            return download(word, nextPage);
        }else if(httpCode==HttpURLConnection.HTTP_OK){
            InputStream inputStream = httpConn.getInputStream();

            String encodingHeader = httpConn.getHeaderField("Content-Encoding");
            if (encodingHeader != null && encodingHeader.toLowerCase().contains("gzip"))
            {
                inputStream = new GZIPInputStream(inputStream);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bufferLength);
            }
            inputStream.close();
            byteArrayOutputStream.close();
            String webContent = new String(byteArrayOutputStream.toByteArray());

            Matcher pronundMatcher = pronouncePattern.matcher(webContent);
            if(pronundMatcher.find()) {
                wordObj.setPronounce(pronundMatcher.group(1));
            }

            Matcher audioMatcher = audioPattern.matcher(webContent);
            if(audioMatcher.find()) {
                wordObj.setAudioLink(audioMatcher.group(1));
            }
        }
        Log(wordObj.toString());
        return wordObj;
    }

    public  static void Log(String text){
        Log.i("VOQUANGHOA",text);
    }
}
