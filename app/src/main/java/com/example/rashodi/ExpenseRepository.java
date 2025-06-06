package com.example.rashodi;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.rashodi.MonthSpending;

// Репозиторий для работы с данными
public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalAmount;
    private ExecutorService executorService;

    // Конструктор
    public ExpenseRepository(Application application) {
        // Получаем экземпляр базы данных
        ExpenseDatabase database = ExpenseDatabase.getInstance(application);
        // Получаем DAO
        expenseDao = database.expenseDao();
        // Получаем все расходы
        allExpenses = expenseDao.getAllExpenses();
        // Получаем общую сумму
        totalAmount = expenseDao.getTotalAmount();
        // Создаем пул потоков для асинхронных операций
        executorService = Executors.newSingleThreadExecutor();
    }

    // Методы для работы с данными
    public void insert(Expense expense) {
        executorService.execute(() -> expenseDao.insert(expense));
    }

    public void update(Expense expense) {
        executorService.execute(() -> expenseDao.update(expense));
    }

    public void delete(Expense expense) {
        executorService.execute(() -> expenseDao.delete(expense));
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseDao.getExpensesByCategory(category);
    }

    public LiveData<List<CategorySpending>> getCategorySpending() {
        return expenseDao.getCategorySpending();
    }

    public LiveData<List<MonthSpending>> getMonthSpending() {
        return expenseDao.getMonthSpending();
    }
} 