package com.example.mobilesoftware;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ListFragment extends Fragment {
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        textView = rootView.findViewById(R.id.textView12);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedLocation) {
                textView.setText(selectedLocation);
            }
        });

        // Observe the selected image URI
        sharedViewModel.getSelectedImageUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri selectedImageUri) {
                // Do something with the selected image URI
            }
        });

        // ...

        return rootView;
    }
}
