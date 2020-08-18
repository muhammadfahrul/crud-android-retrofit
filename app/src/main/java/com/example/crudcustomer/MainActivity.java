package com.example.crudcustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.crudcustomer.network.ServiceGenerator;
import com.example.crudcustomer.network.response.BaseResponse;
import com.example.crudcustomer.network.service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DataService service;

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnSubmit;
    private MaterialButton btnViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();

        service = ServiceGenerator.createBaseService(this, DataService.class);
    }

    boolean isEmail(TextInputEditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void initListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                Log.e("name", name);
                Log.e("email", email);
                Log.e("password", password);

                if(isEmpty(name)){
                    etName.setError("Name must not be empty");
                    etName.requestFocus();
                }else if(isEmpty(email)){
                    etEmail.setError("Email must not be empty");
                    etEmail.requestFocus();
                }else if (!isEmail(etEmail)) {
                    etEmail.setError("Enter valid email");
                    etEmail.requestFocus();
                }else if(isEmpty(password)){
                    etPassword.setError("Password must not be empty");
                    etPassword.requestFocus();
                }else if (password.trim().length()<8){
                    etPassword.setError("Password must be more than 8 Character");
                    etPassword.requestFocus();
                }else if (!isValidPassword(password)) {
                    etPassword.setError("Password must be alphanumeric");
                    etPassword.requestFocus();
                }else{
                    addData(name, email, password);
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataActivity.newInstance(MainActivity.this);
            }
        });
    }

    private void addData(String name, String email, String password) {
        Call<BaseResponse> call = service.apiCreate(name, email, password);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etEmail.setText("");
                    etPassword.setText("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        etName = (TextInputEditText) findViewById(R.id.etName);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        btnViewData = (MaterialButton) findViewById(R.id.btnViewData);
        btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit);
    }
}
