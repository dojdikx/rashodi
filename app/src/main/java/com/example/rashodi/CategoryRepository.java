package com.example.rashodi;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> defaultCategories;
    private LiveData<List<Category>> userCategories;
    private ExecutorService executorService;

    public CategoryRepository(Application application) {
        ExpenseDatabase database = ExpenseDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
        defaultCategories = categoryDao.getDefaultCategories();
        userCategories = categoryDao.getUserCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Category category) {
        executorService.execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        executorService.execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.delete(category);
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getDefaultCategories() {
        return defaultCategories;
    }

    public LiveData<List<Category>> getUserCategories() {
        return userCategories;
    }
} 