package com.standalone.passwordkeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.standalone.core.utils.DialogUtil;
import com.standalone.core.utils.EncUtil;
import com.standalone.core.utils.InputValidator;
import com.standalone.core.utils.ViewUtil;
import com.standalone.passwordkeeper.databinding.DialogFormBinding;
import com.standalone.passwordkeeper.user.User;
import com.standalone.passwordkeeper.user.UserDao;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FormDialog extends BottomSheetDialogFragment {
    public static final String TAG = FormDialog.class.getSimpleName();
    DialogFormBinding binding;
    UserDao dao;

    boolean canUpdate = false;
    long userId;

    int keySize;
    SecretKey secretKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, com.standalone.core.R.style.AppTheme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFormBinding.inflate(inflater, container, false);
        Dialog dialog = getDialog();
        assert dialog != null;
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        assert activity != null;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        keySize = sharedPref.getInt("size", 0);

        if (keySize == 0) {
            startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        }


        dao = new UserDao();

        final Bundle bundle = getArguments();
        canUpdate = bundle != null && bundle.containsKey("id");
        if (canUpdate) {
            userId = bundle.getLong("id");
            User user = dao.get(userId);
            binding.edTitle.setText(user.getTitle());
            binding.edUsername.setText(user.getUsername());
            binding.edPassword.setText(decrypt(user.getPassword()));
        } else {
            binding.btDelete.setVisibility(View.GONE);
        }

        binding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputValidator validator = InputValidator.getInstance();
                    validator.validate(binding.edTitle).notEmpty();
                    validator.validate(binding.edUsername).notEmpty();
                    validator.validate(binding.edPassword).notEmpty();

                    createOrUpdateUser();
                    dismiss();
                } catch (InputValidator.ValidationError e) {
                    //Ignore
                }
            }
        });

        binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showConfirm(view.getContext(), getString(R.string.confirm_delete), new DialogUtil.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        dao.delete(userId);
                        dismiss();
                    }
                });
            }
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDismissListener) {
            ((OnDismissListener) activity).onDismiss(dialog);
        }
    }

    void createOrUpdateUser() {

        String title = ViewUtil.getText(binding.edTitle);
        String username = ViewUtil.getText(binding.edUsername);
        String encryptedPw = encrypt(ViewUtil.getText(binding.edPassword));

        User user = new User.Builder().setTitle(title).setUsername(username).setPassword(encryptedPw).build();

        if (canUpdate) {
            dao.update(userId, user);
        } else {
            dao.create(user);
        }


    }

    String encrypt(String s) {
        try {
            return EncUtil.encrypt(s, getSecretKey());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException |
                 InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    String decrypt(String s) {
        try {
            return EncUtil.decrypt(s, getSecretKey());
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                 NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    SecretKey getSecretKey() {
        User owner = dao.getOwner();
        byte[] encodedWithSalt = Base64.decode(owner.getTitle(), Base64.DEFAULT);
        byte[] encoded = Arrays.copyOfRange(encodedWithSalt, keySize, encodedWithSalt.length);
        return new SecretKeySpec(encoded, "AES");
    }

    interface OnDismissListener {
        void onDismiss(DialogInterface dialogInterface);
    }
}