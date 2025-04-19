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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adicionar PDFs locais
        linkItemList = new ArrayList<>();
        linkItemList.add(new LinkItem("Estilo de Vida Saudável", R.raw.estilo_vida));
        linkItemList.add(new LinkItem("Como Medir Corretamente a Tensão Arterial", R.raw.como_medir));
        linkItemList.add(new LinkItem("Hipertensão Arterial - importância e consequências", R.raw.importancia));
        linkItemList.add(new LinkItem("Política de Privacidade", R.raw.privacidade));

        linksAdapter = new LinksAdapter(getContext(), linkItemList);
        recyclerView.setAdapter(linksAdapter);

        return view;
    }
}
