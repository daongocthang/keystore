package com.standalone.passwordkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.standalone.core.dao.Dao;
import com.standalone.core.utils.EncUtil;
import com.standalone.core.utils.InputValidator;
import com.standalone.core.utils.ViewUtil;
import com.standalone.passwordkeeper.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SecretDao dao = new SecretDao();
        Secret masterSecret = dao.getMasterSecret();
        if (masterSecret == null) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputValidator.getInstance().validate(binding.edPassword).notEmpty().password();
                    String pw = ViewUtil.getText(binding.edPassword);
                    assert masterSecret != null;
                    String hashedPw = masterSecret.getPassword();
                    if (EncUtil.check(pw, hashedPw)) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        TextInputLayout inputLayout = ViewUtil.findTextInputLayout(binding.edPassword);
                        if (inputLayout != null)
                            inputLayout.setError(getString(R.string.incorrect_password));
                    }

                } catch (InputValidator.ValidationError e) {
                    // Ignore
                }
            }
        });
    }


}