package com.example.androidchallenge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;


public class CharacterListFragment extends ListFragment implements GETCharactersAsyncTask.GETCharacterListener, DownloadImagesAsyncTask.DownloadImagesListener, CharacterArrayAdapter.FavoriteSetListener {
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private ArrayList<Integer> favoriteCharactersIDs;
    private int offset = 0;
    private final int pagingSize = 12;
    private boolean gettingCharacters = false;
    private boolean finishedGettingCharacters = false;

    private SharedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Breaking Bad Characters");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        progressBar = view.findViewById(R.id.main_progress);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (getListView().getLastVisiblePosition() - getListView().getHeaderViewsCount() -
                        getListView().getFooterViewsCount()) >= (getListAdapter().getCount() - 1)) {

                    if(!gettingCharacters && !finishedGettingCharacters){
                        new GETCharactersAsyncTask(CharacterListFragment.this).execute(String.valueOf(pagingSize),String.valueOf(offset));
                        gettingCharacters = true;
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d("CURRENT OFFSET", String.valueOf(offset));
                    }
                    Log.d("ListView", "Reached Bottom");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model.select((Character)getListAdapter().getItem(position));
                Navigation.findNavController(view).navigate(R.id.navigate_to_detail_fragment);
            }
        });

        if(getListAdapter() == null) {
            setListAdapter(new CharacterArrayAdapter(getContext(), new ArrayList<>(), this));

            new GETCharactersAsyncTask(CharacterListFragment.this).execute(String.valueOf(pagingSize),String.valueOf(offset));
            progressBar.setVisibility(View.VISIBLE);

            dbHelper = new DatabaseHelper(getContext());
            favoriteCharactersIDs = dbHelper.getFavoriteCharactersIDs();
        }

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void setFavoriteCharacterID(int characterID, boolean state){
        if(state){
            favoriteCharactersIDs.add(characterID);
            dbHelper.addFavoriteCharacterID(characterID);
        }else{
            favoriteCharactersIDs.remove(Integer.valueOf(characterID));
            dbHelper.removeFavoriteCharacterID(characterID);
        }
    }

    @Override
    public void GETCharacterCallback(ArrayList<Character> characters) {
        if(characters.size() > 0){
            for (Character character : characters){
                if(favoriteCharactersIDs.contains(character.getId())){
                    character.setFavorite(true);
                }
            }

            new DownloadImagesAsyncTask(this, characters).execute();
            offset += characters.size();
        }else{
            progressBar.setVisibility(View.GONE);
            finishedGettingCharacters = true;
        }

    }

    @Override
    public void DownloadImagesCallback(ArrayList<Character> characters) {
        ((CharacterArrayAdapter)getListAdapter()).addAll(characters);
        ((CharacterArrayAdapter)getListAdapter()).notifyDataSetChanged();
        gettingCharacters = false;
        progressBar.setVisibility(View.GONE);
    }
}