package com.standalone.passwordkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.standalone.core.utils.EncUtil;
import com.standalone.core.utils.InputValidator;
import com.standalone.core.utils.ViewUtil;
import com.standalone.passwordkeeper.databinding.ActivityMainBinding;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    static final String PASSWORD = "N0Syst3m1nS@f3";
    ActivityMainBinding binding;
    String cipherText;
    SecretKey secretKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        try {
            secretKey = EncUtil.getAESKey(PASSWORD.toCharArray());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }


        binding.btEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputValidator.getInstance().validate(binding.edInput).notEmpty();
                    cipherText = EncUtil.encrypt(ViewUtil.getText(binding.edInput), secretKey);
                    binding.tvPreview.setText(cipherText);
                } catch (
                        NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                        BadPaddingException | NoSuchAlgorithmException |
                        InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                } catch (InputValidator.ValidationError e) {
                    // Ignore
                }
            }
        });

        binding.btDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(cipherText)) return;

                try {
                    String res = EncUtil.decrypt(cipherText, secretKey);
                    binding.tvPreview.setText(res);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                         IllegalBlockSizeException | BadPaddingException |
                         InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}