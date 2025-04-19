package com.example.hta;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WelcomeFragment(); // Welcome Fragment
            case 1:
                return new InputFragment(); // Input Fragment
            case 2:
                return new GraphFragment(); // Graph Fragment
            case 3:
                return new TableFragment(); // Table Fragment
            case 4:
                return new SettingsFragment(); // Notifications Fragment
            case 5:
                return new LinksFragment(); // Info Fragment
            default:
                return new WelcomeFragment(); // Default: Welcome Fragment
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
