package com.example.crudcustomer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.crudcustomer.adapter.DataAdapter;
import com.example.crudcustomer.listener.OnDeleteClickListener;
import com.example.crudcustomer.listener.OnUpdateClickListener;
import com.example.crudcustomer.model.Data;
import com.example.crudcustomer.network.ServiceGenerator;
import com.example.crudcustomer.network.response.BaseResponse;
import com.example.crudcustomer.network.service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends AppCompatActivity implements OnDeleteClickListener, OnUpdateClickListener {

    private static final String TAG = DataActivity.class.getSimpleName();

    private RecyclerView rvData;
    private DataAdapter adapter;
    private DataService service;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, DataActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initViews();

        // Initialization adapter
        adapter = new DataAdapter(this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        service = ServiceGenerator.createBaseService(this, DataService.class);

        rvData.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        Call<BaseResponse<List<Data>>> call = service.apiRead();
        call.enqueue(new Callback<BaseResponse<List<Data>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Data>>> call, Response<BaseResponse<List<Data>>> response) {
                if(response.code() == 200) {
                    adapter.addAll(response.body().getData());
                    initListener();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Data>>> call, Throwable t) {
                Log.e(TAG+".error", t.toString());
            }
        });
    }

    private void initListener() {
        adapter.setOnDeleteClickListener(this);
        adapter.setOnUpdateClickListener(this);
    }

    private void initViews() {
        rvData = (RecyclerView) findViewById(R.id.rv_data);
    }

    private void doDelete(final int position, String id) {
        Call<BaseResponse> call = service.apiDelete(id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200)
                    adapter.remove(position);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG+".errorDelete", t.toString());
            }
        });
    }

    @Override
    public void onDeleteClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doDelete(position, adapter.getData(position).getId());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onUpdateClick(int position) {
        Data data = adapter.getData(position);
        UpdateActivity.newInstance(this, data);
    }
}
