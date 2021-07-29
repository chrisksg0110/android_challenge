package com.example.androidchallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

public class DownloadImagesAsyncTask extends AsyncTask<String, Void, ArrayList<Character>> {
    ArrayList<Character> characters;
    DownloadImagesListener listener;

    public interface DownloadImagesListener {
        void DownloadImagesCallback(ArrayList<Character> characters);
    }

    public DownloadImagesAsyncTask(DownloadImagesListener listener, ArrayList<Character> characters) {
        this.listener = listener;
        this.characters = characters;
    }

    protected ArrayList<Character> doInBackground(String... urls) {
        for(int i=0; i<characters.size();i++){
            Character character = characters.get(i);
            String url = character.getImgUrl();
            try {
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                if(bitmap != null){
                    character.setImgBitmap(bitmap);
                }else{

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return characters;
    }

    protected void onPostExecute(ArrayList<Character> characters) {
        listener.DownloadImagesCallback(characters);
    }
}