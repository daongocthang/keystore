package com.standalone.passwordkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.standalone.core.utils.EncUtil;
import com.standalone.core.utils.InputValidator;
import com.standalone.core.utils.ViewUtil;
import com.standalone.passwordkeeper.databinding.ActivityRegisterBinding;
import com.standalone.passwordkeeper.user.User;
import com.standalone.passwordkeeper.user.UserDao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        UserDao dao = new UserDao();

        binding.btNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputValidator.getInstance().validate(binding.edPassword).notEmpty().password();
                    String pw = ViewUtil.getText(binding.edPassword);

                    InputValidator.getInstance().validate(binding.edRePassword).confirmPassword(pw);

                    String hashedPw = EncUtil.hash(pw);


                    User user = new User.Builder()
                            .setUsername("owner")
                            .setPassword(EncUtil.hash(pw))
                            .setTitle(genKey(pw.length()))
                            .build();

                    dao.create(user);

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } catch (InputValidator.ValidationError e) {
                    // Ignore
                }
            }
        });
    }

    String genKey(int size) {
        try {
            byte[] encoded = EncUtil.genAESKey().getEncoded();
            byte[] salt = new byte[size];
            byte[] encodedWithSalt = new byte[encoded.length + size];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            System.arraycopy(salt, 0, encodedWithSalt, 0, size);
            System.arraycopy(encoded, 0, encodedWithSalt, size, encoded.length);
            return Base64.encodeToString(encodedWithSalt, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}