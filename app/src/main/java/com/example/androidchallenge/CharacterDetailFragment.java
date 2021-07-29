package com.example.androidchallenge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class CharacterDetailFragment extends Fragment {

    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(getContext());

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView textName = view.findViewById(R.id.character_text_name);
        TextView textOccupation = view.findViewById(R.id.character_text_occupation);
        TextView textStatus = view.findViewById(R.id.character_text_status);
        TextView textPortrayed = view.findViewById(R.id.character_text_portrayed);

        ImageView imageView = view.findViewById(R.id.character_detail_img);
        ToggleButton toggleButton = view.findViewById(R.id.character_toggle_favorite);

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setFavoriteCharacterID(model.getSelected().getValue(), isChecked);
        });

        String occupationsString = "";
        ArrayList<String> occupations = model.getSelected().getValue().getOccupations();

        for (int i=0; i<occupations.size(); i++){
            occupationsString += occupations.get(i);
            if(i != occupations.size()-1){
                occupationsString += ", ";
            }
        }


        ((MainActivity)getActivity()).getSupportActionBar().setTitle(model.getSelected().getValue().getName());
        textName.setText(model.getSelected().getValue().getNickname());
        textOccupation.setText(occupationsString);
        textStatus.setText(model.getSelected().getValue().getStatus());
        textPortrayed.setText(model.getSelected().getValue().getPortrayed());
        imageView.setImageBitmap(model.getSelected().getValue().getImgBitmap());
        toggleButton.setChecked(model.getSelected().getValue().isFavorite());
    }


    public void setFavoriteCharacterID(Character character, boolean state){
        character.setFavorite(state);
        if(state){
            dbHelper.addFavoriteCharacterID(character.getId());
        }else{
            dbHelper.removeFavoriteCharacterID(character.getId());
        }
    }
}