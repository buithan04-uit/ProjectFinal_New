package com.example.projectfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfinal.API.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailText , passwordText;
    private TextView errorText , errorText1;
    private Boolean error , error1;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);

        errorText = findViewById(R.id.error);
        errorText1 = findViewById(R.id.error1);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void SignUpBtn (View vIew){
        Intent intent = new Intent(this , SignUp.class);
        startActivity(intent);
    }
    public void SignInBtn (View view){
        // Do something
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        boolean hasError = false;

        if (email.isEmpty()) {
            errorText.setText("Email is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }

        if (password.isEmpty()) {
            errorText1.setText("Password is required.");
            hasError = true;
        } else {
            errorText1.setText("");
        }


        if (hasError) {
            return; // Dừng lại nếu có lỗi
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(email,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error");
                        error1 = responseBodyString.contains("Err1");
                        if (error){
                            Toast.makeText(Login.this, "Email not found !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email not found !");
                        }
                        else if(error1){
                            Toast.makeText(Login.this, "Password is not correct !", Toast.LENGTH_LONG).show();
                            errorText1.setText("Password is not correct !");
                        }
                        else {
                            Toast.makeText(Login.this, "Login successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            errorText1.setText("");
                            startActivity(new Intent(Login.this , DashBoard.class));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(Login.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(Login.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void ForgetBtn (View view){
        //do sonmething

        Intent intent = new Intent(this , ForgotPassword.class);
        startActivity(intent);
    }
}