package com.daclink.drew.sp22.cst438_project01_starter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.drew.sp22.cst438_project01_starter.adapters.SearchResultsAdapter;
import com.daclink.drew.sp22.cst438_project01_starter.databinding.FragmentSearchBinding;
import com.daclink.drew.sp22.cst438_project01_starter.viewModels.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private @NonNull
    FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchResultsAdapter adapter;

    private TextInputEditText keywordEditText, authorEditText;
    private Button searchButton, saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.fragment_search, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SearchResultsAdapter();

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.init();
        viewModel.getVolumesResponseLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                adapter.setResults(response);
            }
        });

        initialSearch();

        RecyclerView recyclerView = view.findViewById(R.id.fragment_search_searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        keywordEditText = view.findViewById(R.id.fragment_search_keyword);
        // if user is functional
        // user.addSearchHistory(keywordEditText.getEditableText().toString());
        searchButton = view.findViewById(R.id.fragment_search);
        saveButton = view.findViewById(R.id.fragment_search);

        searchButton.setOnClickListener(view3 -> performSearch());
        // commented out for future fixing
//        saveButton.setOnClickListener(view3 -> saveMovie(keywordEditText.getEditableText().toString()));
    }

    // this is supposed to save the movie when the save button is clicked
    public void saveMovie(String title) {
        // save movie clicked to the user's section in the db
        // user.addFavMovie(title);
    }

    public void initialSearch() {
        viewModel.searchVolumes("tron");
    }

    public void performSearch() {
        String title = keywordEditText.getEditableText().toString();
        viewModel.searchVolumes(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}