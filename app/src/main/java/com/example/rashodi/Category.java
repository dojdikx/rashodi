package com.example.rashodi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

// Класс для представления категории расходов
@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int color;
    private String iconName;
    private boolean isDefault;

    @Ignore
    public Category(String name, int color, String iconName) {
        this.name = name;
        this.color = color;
        this.iconName = iconName;
        this.isDefault = false;
    }

    // Конструктор для предустановленных категорий
    public Category(String name, int color, String iconName, boolean isDefault) {
        this.name = name;
        this.color = color;
        this.iconName = iconName;
        this.isDefault = isDefault;
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
} 