package com.example.rashodi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;
    public static final int EDIT_EXPENSE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Применение сохраненной темы при запуске приложения
        AppCompatDelegate.setDefaultNightMode(ThemeSettingsActivity.getSavedThemeMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация RecyclerView
        RecyclerView recyclerView = findViewById(R.id.expensesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this);
        recyclerView.setAdapter(adapter);

        // Инициализация ViewModel
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        
        // Наблюдаем за изменениями в списке расходов
        expenseViewModel.getAllExpenses().observe(this, expenses -> {
            adapter.setExpenses(expenses);
        });

        // Обработка нажатия на кнопку добавления
        FloatingActionButton addButton = findViewById(R.id.addExpenseButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reports) {
            Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_theme_settings) {
            Intent intent = new Intent(MainActivity.this, ThemeSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_manage_categories) {
            Intent intent = new Intent(MainActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Expense expense) {
        // Открываем экран редактирования
        Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
        intent.putExtra("expense_id", expense.getId());
        intent.putExtra("expense_amount", expense.getAmount());
        intent.putExtra("expense_description", expense.getDescription());
        intent.putExtra("expense_category", expense.getCategory());
        startActivityForResult(intent, EDIT_EXPENSE_REQUEST);
    }

    @Override
    public void onItemLongClick(Expense expense) {
        // Показываем диалог подтверждения удаления
        new AlertDialog.Builder(this)
                .setTitle("Удаление расхода")
                .setMessage("Вы уверены, что хотите удалить этот расход?")
                .setPositiveButton("Да", (dialog, which) -> {
                    expenseViewModel.delete(expense);
                    Toast.makeText(MainActivity.this, "Расход удален", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EXPENSE_REQUEST && resultCode == RESULT_OK && data != null) {
            long id = data.getLongExtra("expense_id", -1);
            if (id != -1) {
                double amount = data.getDoubleExtra("expense_amount", 0);
                String description = data.getStringExtra("expense_description");
                String category = data.getStringExtra("expense_category");
                
                Expense expense = new Expense(amount, description, category);
                expense.setId(id);
                expenseViewModel.update(expense);
                Toast.makeText(this, "Расход обновлен", Toast.LENGTH_SHORT).show();
            }
        }
    }
}