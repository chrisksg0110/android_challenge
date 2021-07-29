package com.example.androidchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CharacterArrayAdapter extends ArrayAdapter<Character> {
    private FavoriteSetListener listener;

    public interface FavoriteSetListener {
        void setFavoriteCharacterID(int characterID, boolean state);
    }

    public CharacterArrayAdapter(Context context, ArrayList<Character> userArrayList, FavoriteSetListener listener){
        super(context,R.layout.row_character,userArrayList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Character character = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_character,parent,false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.row_img_icon);
        TextView textName = convertView.findViewById(R.id.row_text_name);
        TextView textNickname = convertView.findViewById(R.id.row_text_nickname);
        ToggleButton toggleFavorite = convertView.findViewById(R.id.row_toggle_favorite);

        toggleFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            character.setFavorite(isChecked);
            listener.setFavoriteCharacterID(character.getId(), isChecked);
        });


        textName.setText(character.getName());
        textNickname.setText(character.getNickname());
        toggleFavorite.setChecked(character.isFavorite());
        imageViewIcon.setImageBitmap(character.getImgBitmap());


        return convertView;
    }


}

