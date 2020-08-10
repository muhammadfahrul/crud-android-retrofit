package com.example.crudcustomer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.crudcustomer.R;
import com.example.crudcustomer.listener.OnDeleteClickListener;
import com.example.crudcustomer.listener.OnUpdateClickListener;
import com.example.crudcustomer.model.Data;

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

        TextView tvNumber;
        TextView tvFullName;
        TextView tvUsername;
        TextView tvEmail;
        TextView tvPhoneNumber;
        TextView tvCreatedAt;
        TextView tvUpdatedAt;
        Button btnEdit;
        Button btnRemove;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false));
            initViews();

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUpdateClickListener.onUpdateClick(getAdapterPosition());
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void bind(Data item) {
            int number = getAdapterPosition() + 1;
            tvNumber.setText(String.valueOf(number));
            tvFullName.setText(item.getFullName());
            tvUsername.setText(item.getUsername());
            tvEmail.setText(item.getEmail());
            tvPhoneNumber.setText(item.getPhoneNumber());
            tvCreatedAt.setText(item.getCreatedAt());
            tvUpdatedAt.setText(item.getUpdatedAt());
        }

        public void initViews() {
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvFullName = (TextView) itemView.findViewById(R.id.tv_full_name);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tv_created_at);
            tvUpdatedAt = (TextView) itemView.findViewById(R.id.tv_updated_at);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
            btnRemove = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }
}
