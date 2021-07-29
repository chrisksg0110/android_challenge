package com.example.androidchallenge;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GETCharactersAsyncTask extends AsyncTask<String, Void, ArrayList<Character>> {

    private final String URL = "https://www.breakingbadapi.com/api/characters";
    private final GETCharacterListener listener;

    public interface GETCharacterListener {
        void GETCharacterCallback(ArrayList<Character> characters);
    }

    public GETCharactersAsyncTask(GETCharacterListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Character> doInBackground(String... args) {
        ArrayList<Character> characters = null;
        try {
            URL url = new URL(this.URL + "?limit=" + args[0] + "&offset=" + args[1]);

            String jsonResponse = makeHttpRequest(url);
            characters = extractCharactersFromJSON(jsonResponse);
        } catch (MalformedURLException e) {
            Log.e("GET CHARACTER", "Error creating URL", e);
        } catch (IOException e) {
            Log.e("GET CHARACTER", "Error with http request", e);
        }

        return characters;
    }

    @Override
    protected void onPostExecute(ArrayList<Character> characters) {
        if(listener != null){
            listener.GETCharacterCallback(characters);
        }
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(2500);
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

            if (urlConnection.getResponseCode() == 200) {
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e("makeHttpRequest", "IOException", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public ArrayList<Character> extractCharactersFromJSON(String jsonResponse){
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        try {
            JSONArray charactersJSON = new JSONArray(jsonResponse);
            ArrayList<Character> characters = new ArrayList<>();
            if (charactersJSON.length() > 0) {
                for (int i = 0; i < charactersJSON.length(); i++) {
                    JSONObject characterJSON = charactersJSON.getJSONObject(i);
                    int id = characterJSON.getInt("char_id");
                    String name = characterJSON.getString("name");
                    String nickname = characterJSON.getString("nickname");
                    String portrayed = characterJSON.getString("portrayed");
                    String status = characterJSON.getString("status");
                    String imgURL = characterJSON.getString("img");
                    JSONArray occupationsJSON = characterJSON.getJSONArray("occupation");
                    ArrayList<String> occupations = new ArrayList<>();
                    if (occupationsJSON.length() > 0) {
                        for (int j = 0; j < occupationsJSON.length(); j++) {
                            String occupation = occupationsJSON.getString(j);
                            occupations.add(occupation);
                        }
                    }
                    characters.add(new Character(id,name,nickname,status,portrayed,imgURL,occupations));
                }
            }
            return characters;

        } catch (JSONException e) {
            Log.e("GET CHARACTERS", "Error parsing json", e);
            return null;
        }
    }
}