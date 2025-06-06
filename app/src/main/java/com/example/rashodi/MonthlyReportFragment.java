package com.example.rashodi;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.components.Legend;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MonthlyReportFragment extends Fragment {

    private BarChart barChart;
    private ExpenseViewModel expenseViewModel;

    public MonthlyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_report, container, false);
        barChart = view.findViewById(R.id.barChart);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getMonthSpending().observe(getViewLifecycleOwner(), monthSpendings -> {
            if (monthSpendings != null) {
                Log.d("MonthlyReport", "Month spendings: " + monthSpendings.size());
                for (MonthSpending ms : monthSpendings) {
                    Log.d("MonthlyReport", "Year: " + ms.year + ", Month: " + ms.month + ", Total Amount: " + ms.totalAmount);
                }
                setupBarChart(monthSpendings);
            }
        });
    }

    private void setupBarChart(List<MonthSpending> monthSpendings) {
        if (monthSpendings == null || monthSpendings.isEmpty()) {
            barChart.clear();
            barChart.invalidate(); // Refresh the chart to show empty state
            return;
        }

        // Сортировка по году и месяцу
        Collections.sort(monthSpendings, new Comparator<MonthSpending>() {
            @Override
            public int compare(MonthSpending o1, MonthSpending o2) {
                int yearComparison = Integer.compare(o1.year, o2.year);
                if (yearComparison != 0) {
                    return yearComparison;
                }
                return Integer.compare(o1.month, o2.month);
            }
        });

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Для корректного отображения месяцев на оси X
        String[] monthNames = new String[]{"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"};

        for (int i = 0; i < monthSpendings.size(); i++) {
            MonthSpending ms = monthSpendings.get(i);
            entries.add(new BarEntry(i, (float) ms.totalAmount));
            
            String monthLabel = "Неизвестно"; // Default label for invalid month
            if (ms.month >= 1 && ms.month <= 12) {
                monthLabel = monthNames[ms.month - 1];
            }
            
            // Безопасное получение последних двух цифр года
            String yearSuffix = String.format(Locale.getDefault(), "%02d", ms.year % 100);
            labels.add(monthLabel + " '" + yearSuffix);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Ежемесячные расходы");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new ValueFormatter() {
            private final DecimalFormat mFormat = new DecimalFormat("#,##0.00");

            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value);
            }
        });

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);

        // Получаем цвет текста из атрибута темы colorOnSurface
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true);
        int textColor = typedValue.data;

        // Настройка оси X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(labels.size());
        xAxis.setLabelRotationAngle(-45f); // Поворот меток для лучшей читаемости
        xAxis.setTextColor(textColor); // Устанавливаем цвет текста меток оси X
        xAxis.setTextSize(10f); // Устанавливаем размер текста меток оси X

        // Настройка легенды
        Legend legend = barChart.getLegend();
        legend.setTextColor(textColor); // Устанавливаем цвет текста легенды
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // Выравнивание по нижнему краю
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT); // Выравнивание по левому краю
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL); // Горизонтальная ориентация легенды
        legend.setDrawInside(false); // Легенда вне графика
        legend.setXOffset(5f); // Небольшой отступ по X, чтобы не прилипать к краю
        legend.setYOffset(5f); // Небольшой отступ по Y

        barChart.getAxisLeft().setAxisMinimum(0f); // Начать ось Y с нуля
        barChart.getAxisRight().setEnabled(false); // Отключить правую ось Y

        barChart.animateY(1500);
        barChart.invalidate(); // refresh

        // Увеличиваем нижний отступ, чтобы легенда не наезжала на метки оси X
        barChart.setExtraBottomOffset(20f); 
    }
} 