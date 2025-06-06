package com.example.hta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class InputFragment extends Fragment {

    private AddNumberViewModel addNumberViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        EditText inputSystolic = view.findViewById(R.id.inputNumber2); // Sistólica (Máxima)
        EditText inputDiastolic = view.findViewById(R.id.inputNumber1); // Diastólica (Mínima)
        EditText inputBpm = view.findViewById(R.id.inputNumber3);           // Frequência Cardíaca
        Button addButton = view.findViewById(R.id.addButton);

        addNumberViewModel = new ViewModelProvider(requireActivity()).get(AddNumberViewModel.class);

        addButton.setOnClickListener(null);
        addButton.setOnClickListener(v -> {
            try {
                String systolicText = inputSystolic.getText().toString();
                String diastolicText = inputDiastolic.getText().toString();
                String bpmText = inputBpm.getText().toString();

                // Validações para campos vazios
                if (systolicText.isEmpty() || diastolicText.isEmpty() || bpmText.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Converter os valores para números
                int systolic = Integer.parseInt(systolicText);
                int diastolic = Integer.parseInt(diastolicText);
                int bpm = Integer.parseInt(bpmText);

                // Validações de intervalo
                if (systolic < 30.0 || systolic > 230.0) {
                    Toast.makeText(getContext(), getString(R.string.error_invalid_systolic), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (diastolic < 20.0 || diastolic > 230.0) {
                    Toast.makeText(getContext(), getString(R.string.error_invalid_diastolic), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bpm < 30.0 || bpm > 200.0) {
                    Toast.makeText(getContext(), getString(R.string.error_invalid_bpm), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Adicionar ao banco de dados
                addNumberViewModel.addNumber(systolic, diastolic, bpm);

                // Limpar os campos
                inputSystolic.setText("");
                inputDiastolic.setText("");
                inputBpm.setText("");

                Toast.makeText(getContext(), getString(R.string.success_values_added), Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), getString(R.string.error_invalid_number_format), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
