package com.example.rashodi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.rashodi.MonthSpending;

// @Dao указывает Room, что этот интерфейс является DAO
@Dao
public interface ExpenseDao {
    // Вставка нового расхода
    @Insert
    void insert(Expense expense);

    // Обновление существующего расхода
    @Update
    void update(Expense expense);

    // Удаление расхода
    @Delete
    void delete(Expense expense);

    // Получение всех расходов, отсортированных по дате (от новых к старым)
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    // Получение расходов по категории
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByCategory(String category);

    // Получение общей суммы расходов
    @Query("SELECT SUM(amount) FROM expenses")
    LiveData<Double> getTotalAmount();

    // Получение общей суммы расходов по каждой категории
    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses GROUP BY category")
    LiveData<List<CategorySpending>> getCategorySpending();

    // Получение общей суммы расходов по месяцам
    @Query("SELECT CAST(strftime('%Y', date / 1000, 'unixepoch') AS INTEGER) AS year, CAST(strftime('%m', date / 1000, 'unixepoch') AS INTEGER) AS month, SUM(amount) AS totalAmount FROM expenses GROUP BY year, month ORDER BY year, month")
    LiveData<List<MonthSpending>> getMonthSpending();
} 