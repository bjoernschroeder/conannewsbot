package de.karasuma.discordbot.conannews.util;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPUtil {

    public JSONObject getRequest(URL url) {
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder response = new StringBuilder();
            while (bufferedReader.ready()) {
                response.append(bufferedReader.readLine());
            }
            urlConnection.disconnect();
            jsonObject = new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
