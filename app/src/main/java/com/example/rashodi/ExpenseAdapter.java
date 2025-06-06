package com.example.rashodi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Адаптер для отображения списка расходов
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    private OnExpenseClickListener listener;

    // Интерфейс для обработки нажатий
    public interface OnExpenseClickListener {
        void onItemClick(Expense expense);
        void onItemLongClick(Expense expense);
    }

    // Конструктор с listener
    public ExpenseAdapter(OnExpenseClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder для элемента списка
    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView amountTextView;
        private final TextView descriptionTextView;
        private final TextView categoryTextView;
        private final TextView dateTextView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            // Обработка обычного нажатия
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(expenses.get(position));
                }
            });

            // Обработка долгого нажатия
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemLongClick(expenses.get(position));
                    return true;
                }
                return false;
            });
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense currentExpense = expenses.get(position);
        holder.amountTextView.setText(String.format(Locale.getDefault(), "%.2f ₽", currentExpense.getAmount()));
        holder.descriptionTextView.setText(currentExpense.getDescription());
        holder.categoryTextView.setText(currentExpense.getCategory());
        holder.dateTextView.setText(dateFormat.format(currentExpense.getDate()));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    // Метод для обновления списка расходов
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }
} 