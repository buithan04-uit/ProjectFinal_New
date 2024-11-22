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

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private EditText firstnameText, lastnameText, emailText, passwordText , confirmpasswordText;
    private TextView errorText , errorText1 , errorText2 ,errorText3 , errorText4;
    private Boolean error, error1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firstnameText = findViewById(R.id.firstname);
        lastnameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        confirmpasswordText = findViewById(R.id.confirmpassword);

        errorText = findViewById(R.id.errortext);
        errorText1 = findViewById(R.id.errortext1);
        errorText2 = findViewById(R.id.errortext2);
        errorText3 = findViewById(R.id.errortext3);
        errorText4 = findViewById(R.id.errortext4);
        errorText.setText("");
        errorText1.setText("");
        errorText2.setText("");
        errorText3.setText("");
        errorText4.setText("");


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void LoginBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
    }

    public void CreateAccountBtn (View view){
        //do sonmething
        Log.d(TAG, "SignUp button clicked");
        String firstName = firstnameText.getText().toString().trim();
        String lastName = lastnameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();
        String sdt = "";
        boolean hasError = false;

        if (firstName.isEmpty()) {
            errorText3.setText("First name is required.");
            hasError = true;
        } else {
            errorText3.setText("");
        }

        if (lastName.isEmpty()) {
            errorText4.setText("Last name is required.");
            hasError = true;
        } else {
            errorText4.setText("");
        }

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

        if (confirmpassword.isEmpty()) {
            errorText2.setText("Confirm password is required.");
            hasError = true;
        } else if (!password.equals(confirmpassword)) {
            errorText2.setText("* Confirm password is not correct.");
            hasError = true;
        } else {
            errorText2.setText("");
        }

        if (hasError) {
            return; // Dừng lại nếu có lỗi
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(firstName, lastName, email, sdt, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error1");
                        error1 = responseBodyString.contains("Error2");
                        Log.d(TAG, "Response: " + responseBodyString);
                        if (error){
                            Toast.makeText(SignUp.this, "Email is used !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email is used !");
                        } else if (error1) {
                            Toast.makeText(SignUp.this, "Email not exist !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email not exist !");
                        } else {
                            Toast.makeText(SignUp.this, "User created successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            errorText1.setText("");
                            errorText2.setText("");
                            startActivity(new Intent(SignUp.this , Login.class));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(SignUp.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(SignUp.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(SignUp.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}