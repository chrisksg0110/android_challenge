package com.example.androidchallenge;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Character> selected = new MutableLiveData<Character>();

    public void select(Character item) {
        selected.setValue(item);
    }

    public LiveData<Character> getSelected() {
        return selected;
    }
}