package com.example.rashodi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private static final String TAG = "AddExpenseActivity";
    private ExpenseViewModel expenseViewModel;
    private CategoryViewModel categoryViewModel;
    private TextInputEditText amountEditText;
    private TextInputEditText descriptionEditText;
    private AutoCompleteTextView categoryAutoCompleteTextView;
    private TextView dateTextView;
    private Calendar calendar;
    private long expenseId = -1;
    private List<Category> allCategories = new ArrayList<>();
    private boolean defaultCategoriesInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Инициализация ViewModels
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Инициализация полей ввода
        amountEditText = findViewById(R.id.amountEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categoryAutoCompleteTextView = findViewById(R.id.categoryAutoCompleteTextView);
        dateTextView = findViewById(R.id.dateTextView);
        Button saveButton = findViewById(R.id.saveButton);

        // Инициализация календаря и установка текущей даты
        calendar = Calendar.getInstance();
        updateDateInView();

        // Обработка нажатия на поле даты
        dateTextView.setOnClickListener(v -> {
            new DatePickerDialog(AddExpenseActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateInView();
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Наблюдаем за списком категорий из базы данных
        categoryViewModel.getAllCategories().observe(this, categories -> {
            Log.d(TAG, "Received categories from DB: " + categories.size());
            allCategories = categories; // Сохраняем список категорий

            // Если список категорий пуст и мы еще не добавляли предустановленные
            if (categories == null || categories.isEmpty() && !defaultCategoriesInserted) {
                Log.d(TAG, "Inserting default categories as list is empty");
                insertDefaultCategories();
                defaultCategoriesInserted = true; // Отмечаем, что попытка вставки была
            }

            List<String> categoryNames = new ArrayList<>();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, categoryNames);
            categoryAutoCompleteTextView.setAdapter(adapter);
            Log.d(TAG, "Adapter set with DB categories");
        });

        categoryAutoCompleteTextView.setThreshold(1); // Показывать предложения после ввода 1 символа

        // Добавляем слушатель нажатий для открытия выпадающего списка (пока оставляем для отладки)
        categoryAutoCompleteTextView.setOnClickListener(v -> categoryAutoCompleteTextView.showDropDown());

        // Проверяем, открыли ли мы активность для редактирования
        Intent intent = getIntent();
        if (intent.hasExtra("expense_id")) {
            expenseId = intent.getLongExtra("expense_id", -1);
            double amount = intent.getDoubleExtra("expense_amount", 0);
            String description = intent.getStringExtra("expense_description");
            String category = intent.getStringExtra("expense_category");
            Date date = (Date) intent.getSerializableExtra("expense_date"); // Получаем дату

            // Заполняем поля данными
            amountEditText.setText(String.valueOf(amount));
            descriptionEditText.setText(description);
            categoryAutoCompleteTextView.setText(category);
            if (date != null) {
                calendar.setTime(date);
                updateDateInView();
            }
            setTitle("Редактировать расход");
        } else {
            setTitle("Добавить расход");
        }

        // Обработка нажатия на кнопку сохранения
        saveButton.setOnClickListener(view -> saveExpense());
    }

    private void insertDefaultCategories() {
        categoryViewModel.insert(new Category("Продукты", 0xFF4CAF50, "ic_food", true));
        categoryViewModel.insert(new Category("Транспорт", 0xFF2196F3, "ic_transport", true));
        categoryViewModel.insert(new Category("Развлечения", 0xFF9C27B0, "ic_entertainment", true));
        categoryViewModel.insert(new Category("Жилье", 0xFFFF9800, "ic_home", true));
        categoryViewModel.insert(new Category("Здоровье", 0xFFE91E63, "ic_health", true));
        categoryViewModel.insert(new Category("Одежда", 0xFF795548, "ic_clothes", true));
        categoryViewModel.insert(new Category("Связь", 0xFF607D8B, "ic_communication", true));
        categoryViewModel.insert(new Category("Другое", 0xFF9E9E9E, "ic_other", true));
    }

    private void updateDateInView() {
        String myFormat = "dd.MM.yyyy"; // Формат даты
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateTextView.setText(sdf.format(calendar.getTime()));
    }

    private void saveExpense() {
        String amountStr = amountEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String categoryName = categoryAutoCompleteTextView.getText().toString();

        if (amountStr.isEmpty() || description.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);

            // Проверяем, существует ли введенная категория. Если нет, добавляем ее.
            boolean categoryExists = false;
            for (Category cat : allCategories) {
                if (cat.getName().equalsIgnoreCase(categoryName)) {
                    categoryExists = true;
                    break;
                }
            }

            if (!categoryExists) {
                // Добавляем новую категорию с дефолтными значениями цвета и иконки
                // Можно выбрать любой дефолтный цвет, например, серый (0xFF9E9E9E) и "ic_other"
                categoryViewModel.insert(new Category(categoryName, 0xFF9E9E9E, "ic_other", false));
                Log.d(TAG, "New category added: " + categoryName);
            }

            // Логика сохранения расхода остается без изменений, используя введенное название категории
            Expense expense = new Expense(amount, description, categoryName);
            expense.setDate(calendar.getTime()); // Устанавливаем выбранную дату

            if (expenseId != -1) {
                // Редактирование существующего расхода
                expense.setId(expenseId);
                expenseViewModel.update(expense);
                Toast.makeText(this, "Расход обновлен", Toast.LENGTH_SHORT).show();
            } else {
                // Добавление нового расхода
                expenseViewModel.insert(expense);
                Toast.makeText(this, "Расход добавлен", Toast.LENGTH_SHORT).show();
            }

            // Возвращаем результат
            Intent resultIntent = new Intent();
            resultIntent.putExtra("expense_id", expense.getId());
            resultIntent.putExtra("expense_amount", expense.getAmount());
            resultIntent.putExtra("expense_description", expense.getDescription());
            resultIntent.putExtra("expense_category", categoryName); // Сохраняем имя категории
            resultIntent.putExtra("expense_date", calendar.getTime()); // Сохраняем дату
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Пожалуйста, введите корректную сумму", Toast.LENGTH_SHORT).show();
        }
    }
} 