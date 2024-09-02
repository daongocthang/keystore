package com.standalone.passwordkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.standalone.core.dao.Dao;
import com.standalone.core.utils.EncUtil;
import com.standalone.core.utils.InputValidator;
import com.standalone.core.utils.ViewUtil;
import com.standalone.passwordkeeper.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SecretDao dao = new SecretDao();

        binding.btNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputValidator.getInstance().validate(binding.edPassword).notEmpty().password();
                    String pw = ViewUtil.getText(binding.edPassword);

                    InputValidator.getInstance().validate(binding.edRePassword).confirmPassword(pw);

                    Secret secret = new Secret();
                    secret.setMaster(true);
                    secret.setPassword(EncUtil.hash(pw));

                    dao.create(secret);

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } catch (InputValidator.ValidationError e) {
                    // Ignore
                }
            }
        });
    }


}