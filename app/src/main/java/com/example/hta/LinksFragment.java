package com.example.hta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LinksFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinksAdapter linksAdapter;
    private List<LinkItem> linkItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_links, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            String lang = LanguageHelper.getLanguage(requireContext());

            linkItemList = new ArrayList<>();
            linkItemList.add(new LinkItem(
                    getString(R.string.link_healthy_lifestyle),
                    getPdfRes(lang, "estilo_vida")
            ));
            linkItemList.add(new LinkItem(
                    getString(R.string.link_how_to_measure),
                    getPdfRes(lang, "como_medir")
            ));
            linkItemList.add(new LinkItem(
                    getString(R.string.link_importance),
                    getPdfRes(lang, "importancia")
            ));
            linkItemList.add(new LinkItem(
                    getString(R.string.link_privacy_policy),
                    getPdfRes(lang, "privacidade")
            ));

            linksAdapter = new LinksAdapter(getContext(), linkItemList);
            recyclerView.setAdapter(linksAdapter);
        }

        return view;
    }

    private int getPdfRes(String lang, String baseName) {
        String resName = baseName + (lang.equals("en") ? "_en" : "");
        int id = getResources().getIdentifier(resName, "raw", requireContext().getPackageName());

        if (id == 0) {
            id = getResources().getIdentifier(baseName, "raw", requireContext().getPackageName());
        }

        return id;
    }
}