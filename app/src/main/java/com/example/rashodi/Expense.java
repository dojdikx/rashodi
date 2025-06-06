package com.example.rashodi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

// Аннотация @Entity указывает Room, что этот класс представляет таблицу в базе данных
@Entity(tableName = "expenses")
public class Expense {
    // @PrimaryKey указывает, что это поле является первичным ключом
    // autoGenerate = true означает, что значение будет генерироваться автоматически
    @PrimaryKey(autoGenerate = true)
    private long id;

    // Поля таблицы
    private double amount;
    private String description;
    private String category;
    private Date date;

    // Конструктор
    public Expense(double amount, String description, String category) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = new Date(); // Устанавливаем текущую дату при создании
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
} 