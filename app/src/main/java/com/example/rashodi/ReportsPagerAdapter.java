package com.example.rashodi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReportsPagerAdapter extends FragmentStateAdapter {

    public ReportsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CategoryReportFragment();
            case 1:
                return new MonthlyReportFragment();
            default:
                return new CategoryReportFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // We have two report tabs: Category and Monthly
    }
} 