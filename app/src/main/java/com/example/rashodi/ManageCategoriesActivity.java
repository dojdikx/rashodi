package com.example.rashodi;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        setTitle("Управление категориями");

        RecyclerView recyclerView = findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryAdapter = new CategoryAdapter(this);
        recyclerView.setAdapter(categoryAdapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.setCategories(categories);
        });
    }

    @Override
    public void onDeleteClick(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Удаление категории")
                .setMessage("Вы уверены, что хотите удалить категорию \"" + category.getName() + "\"? Все расходы, связанные с этой категорией, останутся без категории.")
                .setPositiveButton("Да", (dialog, which) -> {
                    categoryViewModel.delete(category);
                    Toast.makeText(ManageCategoriesActivity.this, "Категория \"" + category.getName() + "\" удалена", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", null)
                .show();
    }
} 