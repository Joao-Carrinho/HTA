package com.example.hta;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String fragmentName = getIntent().getStringExtra("FRAGMENT_NAME");
        if (fragmentName != null) {
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentName).newInstance();
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
