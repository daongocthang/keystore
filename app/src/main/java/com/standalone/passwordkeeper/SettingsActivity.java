package com.standalone.passwordkeeper;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.standalone.core.dao.DatabaseManager;
import com.standalone.core.utils.DialogUtil;
import com.standalone.passwordkeeper.databinding.ActivitySettingsBinding;
import com.standalone.passwordkeeper.user.UserDao;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        final UserDao dao = new UserDao();
        binding.btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showConfirm(view.getContext(), getString(R.string.confirm_delete), new DialogUtil.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        dao.clear();
                    }
                });
            }
        });

        binding.btRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showConfirm(view.getContext(), getString(R.string.confirm_restore), new DialogUtil.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        DatabaseManager.getInstance().restore();
                    }
                });
            }
        });

        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showConfirm(view.getContext(), getString(R.string.confirm_backup), new DialogUtil.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        DatabaseManager.getInstance().backup();
                    }
                });
            }
        });
    }
}