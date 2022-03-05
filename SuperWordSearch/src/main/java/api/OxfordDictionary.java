package src.main.java.model;

import  javax.net.ssl.HttpsURLConnection;
import  java.io.BufferedReader;
import  java.io.IOException;
import  java.io.InputStreamReader;
import  java.net.URL;

public class OxfordDictionary {

    public static boolean isWord(String s) {
        boolean result = false;
        final String language = "en-gb";
        final String word = "word";
        final String fields = "pronunciations";
        final String strictMatch = "false";
        final String word_id = s.toLowerCase();
        final String restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
        //TODO: replace with your own app id and app key
        final String app_id = "0fb240ea";
        final String app_key = "5464027afa34085ad5b0f1b5596f8ea5";
        try {
            URL url = new URL(restUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            System.out.println(stringBuilder.toString());
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
