package com.quanghoa.englishcheat;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by voqua on 3/21/2016.
 */
public class WordTranslate {
    private static String wordPattern = "<span class=\"ipa\">([^<]*)</span>";
    private static Pattern pattern = Pattern.compile(wordPattern);

    public String translate(String word) throws IOException {
        return download(word, "http://dictionary.cambridge.org/search/english/direct/?q="+word);
    }

    private String download(String word, String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        int httpCode = httpConn.getResponseCode();
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

            Matcher m = pattern.matcher(webContent);
            if(m.find()) {
                return m.group(1);
            }
        }

        return "("+word+ "??)";
    }

    public  static void Log(String text){
        Log.i("VOQUANGHOA",text);
    }
}
