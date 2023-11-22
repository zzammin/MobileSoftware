package com.example.mobilesoftware;

import android.net.Uri;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<Uri> selectedImageUri = new MutableLiveData<>();
    private final MutableLiveData<Bundle> formData = new MutableLiveData<>(); // 추가

    public void setSelectedLocation(String location) {
        selectedLocation.setValue(location);
    }

    public LiveData<String> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedImageUri(Uri uri) {
        selectedImageUri.setValue(uri);
    }

    public LiveData<Uri> getSelectedImageUri() {
        return selectedImageUri;
    }

    // 추가 시작
    public LiveData<Bundle> getFormData() {
        return formData;
    }

    public void setFormData(Bundle data) {
        formData.setValue(data);
    }
    // 추가 끝
}
