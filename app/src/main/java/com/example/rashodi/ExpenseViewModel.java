package com.example.rashodi;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import com.example.rashodi.MonthSpending;

// ViewModel для работы с данными расходов
public class ExpenseViewModel extends AndroidViewModel {
    // Репозиторий для работы с данными
    private ExpenseRepository repository;
    // LiveData для наблюдения за списком расходов
    private LiveData<List<Expense>> allExpenses;
    // LiveData для наблюдения за общей суммой
    private LiveData<Double> totalAmount;
    private LiveData<List<CategorySpending>> categorySpending;
    private LiveData<List<MonthSpending>> monthSpending;

    // Конструктор
    public ExpenseViewModel(Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        totalAmount = repository.getTotalAmount();
        categorySpending = repository.getCategorySpending();
        monthSpending = repository.getMonthSpending();
    }

    // Методы для работы с данными
    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(category);
    }

    public LiveData<List<CategorySpending>> getCategorySpending() {
        return categorySpending;
    }

    public LiveData<List<MonthSpending>> getMonthSpending() {
        return monthSpending;
    }
} 