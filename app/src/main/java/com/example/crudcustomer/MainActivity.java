package com.example.crudcustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crudcustomer.network.ServiceGenerator;
import com.example.crudcustomer.network.response.BaseResponse;
import com.example.crudcustomer.network.service.DataService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DataService service;

    private EditText etFullName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private Button btnSubmit;
    private Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();

        service = ServiceGenerator.createBaseService(this, DataService.class);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void initListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name = etFullName.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String phone_number = etPhoneNumber.getText().toString();
                Log.e("full_name", full_name);
                Log.e("username", username);
                Log.e("email", email);
                Log.e("phone_number", phone_number);

                if(isEmpty(full_name)){
                    etFullName.setError("Full Name must not be empty");
                    etFullName.requestFocus();
                }else if(isEmpty(username)){
                    etUsername.setError("Username must not be empty");
                    etUsername.requestFocus();
                }else if(isEmpty(email)){
                    etEmail.setError("Email must not be empty");
                    etEmail.requestFocus();
                }else if (!isEmail(etEmail)) {
                    etEmail.setError("Enter valid email");
                    etEmail.requestFocus();
                }else if(isEmpty(phone_number)){
                    etPhoneNumber.setError("Phone Number must not be empty");
                    etPhoneNumber.requestFocus();
                }else if (phone_number.trim().length()<11 && phone_number.trim().length()>13){
                    etPhoneNumber.setError("Phone Number must be more than 11 and less than 13");
                    etPhoneNumber.requestFocus();
                }else{
                    addData(full_name, username, email, phone_number);
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataActivity.newInstance(MainActivity.this);
            }
        });
    }

    private void addData(String full_name, String username, String email, String phone_number) {
        Call<BaseResponse> call = service.apiCreate(full_name, username, email, phone_number);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    etFullName.setText("");
                    etUsername.setText("");
                    etEmail.setText("");
                    etPhoneNumber.setText("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        etFullName = (EditText) findViewById(R.id.et_full_name);
        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        btnShow = (Button) findViewById(R.id.btn_show);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }
}
