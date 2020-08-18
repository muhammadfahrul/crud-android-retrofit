package com.example.crudcustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.crudcustomer.model.Data;
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

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = UpdateActivity.class.getSimpleName();

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnSubmit;
    private DataService service;
    private Data data;

    public static void newInstance(Context context, Data data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG+".data", data);
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtras(bundle);

        context.startActivity(intent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        if(getIntent() != null) {
            data = getIntent().getParcelableExtra(TAG+".data");
        }

        initViews();

        service = ServiceGenerator.createBaseService(this, DataService.class);

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
                    updateData(name, email, password);
                }
            }
        });
    }

    private void updateData(String name, String email, String password) {
        Call<BaseResponse> call = service.apiUpdate(data.getId(), name, email, password);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(UpdateActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
        btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit);

        etName.setText(data.getName());
        etEmail.setText(data.getEmail());
        etPassword.setText(data.getPassword());
    }
}
