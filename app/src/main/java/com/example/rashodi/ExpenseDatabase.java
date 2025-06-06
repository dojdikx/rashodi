package com.example.rashodi;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// @Database указывает Room, что этот класс является базой данных
@Database(entities = {Expense.class, Category.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class}) // Добавляем конвертер для работы с датами
public abstract class ExpenseDatabase extends RoomDatabase {
    // Синглтон для доступа к базе данных
    private static ExpenseDatabase instance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    // Получение DAO
    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();

    // Получение экземпляра базы данных (синглтон)
    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                ExpenseDatabase.class,
                "expense_database" // Имя файла базы данных
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
} 