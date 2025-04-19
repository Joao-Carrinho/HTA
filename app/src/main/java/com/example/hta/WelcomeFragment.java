package com.example.hta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button btnPT = view.findViewById(R.id.btnPortuguese);
        Button btnEN = view.findViewById(R.id.btnEnglish);

        btnPT.setOnClickListener(v -> {
            LanguageHelper.setLocale(requireContext(), "pt");
            restartApp();
        });

        btnEN.setOnClickListener(v -> {
            LanguageHelper.setLocale(requireContext(), "en");
            restartApp();
        });

        return view;
    }

    private void restartApp() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
