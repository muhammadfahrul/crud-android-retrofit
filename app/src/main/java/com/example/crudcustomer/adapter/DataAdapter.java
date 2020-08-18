package com.example.crudcustomer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.crudcustomer.R;
import com.example.crudcustomer.listener.OnDeleteClickListener;
import com.example.crudcustomer.listener.OnUpdateClickListener;
import com.example.crudcustomer.model.Data;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    Context context;
    List<Data> data;

    OnDeleteClickListener onDeleteClickListener;
    OnUpdateClickListener onUpdateClickListener;

    public DataAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void add(Data item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<Data> items) {
        for (Data item : items) {
            add(item);
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public Data getData(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvNumber;
        MaterialTextView tvName;
        MaterialTextView tvEmail;
        MaterialTextView tvCreatedAt;
        MaterialTextView tvUpdatedAt;
        MaterialButton btnUpdate;
        MaterialButton btnDelete;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false));
            initViews();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUpdateClickListener.onUpdateClick(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void bind(Data item) {
            int number = getAdapterPosition() + 1;
            tvNumber.setText(String.valueOf(number));
            tvName.setText(item.getName());
            tvEmail.setText(item.getEmail());
            tvCreatedAt.setText(item.getCreatedAt());
            tvUpdatedAt.setText(item.getUpdatedAt());
        }

        public void initViews() {
            tvNumber = (MaterialTextView) itemView.findViewById(R.id.tvNumber);
            tvName = (MaterialTextView) itemView.findViewById(R.id.tvName);
            tvEmail = (MaterialTextView) itemView.findViewById(R.id.tvEmail);
            tvCreatedAt = (MaterialTextView) itemView.findViewById(R.id.tvCreatedAt);
            tvUpdatedAt = (MaterialTextView) itemView.findViewById(R.id.tvUpdatedAt);
            btnUpdate = (MaterialButton) itemView.findViewById(R.id.btnUpdate);
            btnDelete = (MaterialButton) itemView.findViewById(R.id.btnDelete);
        }
    }
}
