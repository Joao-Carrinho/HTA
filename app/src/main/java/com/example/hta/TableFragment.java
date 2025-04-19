package com.example.hta;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TableFragment extends Fragment {

    private AddNumberViewModel addNumberViewModel;
    private TableAdapter tableAdapter;
    private HorizontalScrollView headerScroll, bodyScroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        headerScroll = view.findViewById(R.id.headerScroll);
        bodyScroll = view.findViewById(R.id.bodyScroll);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tableAdapter = new TableAdapter();
        recyclerView.setAdapter(tableAdapter);

        // Add dividers between rows
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(divider);

        // Synchronize horizontal scroll between header and table
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bodyScroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                headerScroll.scrollTo(scrollX, scrollY);
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            headerScroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                bodyScroll.scrollTo(scrollX, scrollY);
            });
        }

        // Observe changes in ViewModel and update table
        addNumberViewModel = new ViewModelProvider(requireActivity()).get(AddNumberViewModel.class);
        addNumberViewModel.getAllNumbers().observe(getViewLifecycleOwner(), tableAdapter::setNumbers);

        return view;
    }
}
