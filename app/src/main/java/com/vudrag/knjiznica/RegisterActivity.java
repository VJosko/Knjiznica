package com.vudrag.knjiznica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vudrag.knjiznica.search.SearchActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String REPEAT_PASSWORD = "repeatPassword";

    private TextView titleTv;
    private TextView loginTv;
    private TextView registerTv;
    private TextInputLayout userNameTxt;
    private TextInputLayout emailTxt;
    private TextInputLayout passwordTxt;
    private TextInputLayout repeatPasswordTxt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        setOnclickListeners();
    }

    private void findViews(){
        titleTv = findViewById(R.id.register_title_tv);
        loginTv = findViewById(R.id.register_login_tv);
        registerTv = findViewById(R.id.register_register_tv);
        userNameTxt = findViewById(R.id.register_user_name_txt);
        emailTxt = findViewById(R.id.register_email_txt);
        passwordTxt = findViewById(R.id.register_password_txt);
        repeatPasswordTxt = findViewById(R.id.register_repeat_password_txt);
        registerBtn = findViewById(R.id.register_register_btn);
    }

    private void setOnclickListeners(){
        switchToLoginOnClickListener();
        registerOnClickListener();
    }

    private void switchToLoginOnClickListener(){
        loginTv.setOnClickListener(view -> {
            finish();
        });
    }

    private void registerOnClickListener(){
        registerBtn.setOnClickListener(view -> {
            if(!isUserInputValid()){
                return;
            }
            Intent intent = new Intent(RegisterActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private boolean isUserInputValid(){
        userNameTxt.setErrorEnabled(false);
        emailTxt.setErrorEnabled(false);
        passwordTxt.setErrorEnabled(false);
        repeatPasswordTxt.setErrorEnabled(false);

        Map<String, String> userInput = getUserInput();
        boolean isValid = true;
        if(userInput.get(USERNAME).isEmpty()){
            userNameTxt.setError("Required!");
            isValid = false;
        }
        if(userInput.get(EMAIL).isEmpty()){
            emailTxt.setError("Required!");
            isValid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userInput.get(EMAIL)).matches()){
            emailTxt.setError("Email is not valid!");
            isValid = false;
        }
        if(userInput.get(PASSWORD).isEmpty()){
            passwordTxt.setError("Required!");
            isValid = false;
        }
        if(userInput.get(REPEAT_PASSWORD).isEmpty()){
            repeatPasswordTxt.setError("Required!");
            isValid = false;
        }
        else if(userInput.get(PASSWORD).isEmpty() || !userInput.get(PASSWORD).equals(userInput.get(REPEAT_PASSWORD))){
            repeatPasswordTxt.setError("Password doesn't match!");
            isValid = false;
        }
        return isValid;
    }

    private Map<String, String> getUserInput(){
        Map<String, String> userInput = new HashMap<>();
        String userName = userNameTxt.getEditText().getText().toString().trim();
        userInput.put(USERNAME, userName);
        String email = emailTxt.getEditText().getText().toString().trim();
        userInput.put(EMAIL, email);
        String password = passwordTxt.getEditText().getText().toString().trim();
        userInput.put(PASSWORD, password);
        String repeatPassword = repeatPasswordTxt.getEditText().getText().toString().trim();
        userInput.put(REPEAT_PASSWORD, repeatPassword);
        return userInput;
    }
}