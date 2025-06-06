package com.example.rashodi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class CategoryReportFragment extends Fragment {

    private PieChart pieChart;
    private ExpenseViewModel expenseViewModel;

    public CategoryReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_report, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getCategorySpending().observe(getViewLifecycleOwner(), categorySpendings -> {
            if (categorySpendings != null) {
                setupPieChart(categorySpendings);
            }
        });
    }

    private void setupPieChart(List<CategorySpending> categorySpendings) {
        List<PieEntry> entries = new ArrayList<>();
        for (CategorySpending cs : categorySpendings) {
            entries.add(new PieEntry((float) cs.totalAmount, cs.category));
        }

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true);
        int textColor = typedValue.data;

        PieDataSet dataSet = new PieDataSet(entries, "Расходы по категориям");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(textColor);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(textColor);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Расходы по категориям");
        pieChart.setCenterTextColor(textColor);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(textColor);

        pieChart.animateY(1000);
        pieChart.invalidate();
    }
} 