package com.standalone.passwordkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.standalone.core.utils.StorageUtil;
import com.standalone.passwordkeeper.databinding.ActivityMainBinding;
import com.standalone.passwordkeeper.user.UserAdapter;
import com.standalone.passwordkeeper.user.UserDao;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements FormDialog.OnDismissListener {

    ActivityMainBinding binding;
    String cipherText;
    SecretKey secretKey;
    UserAdapter adapter;
    UserDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        StorageUtil.requirePermission(this);

        dao = new UserDao();

        RecyclerView recyclerView = binding.recycler;
        adapter = new UserAdapter(this);
        recyclerView.setAdapter(adapter);

        if (dao.getCount() > 1) {
            binding.tvWarning.setVisibility(View.GONE);
            adapter.setItemList(dao.getWithoutOwner());
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FormDialog().show(getSupportFragmentManager(), FormDialog.TAG);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dao.getCount() == 0) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            adapter.setItemList(dao.getWithoutOwner());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemSettings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        adapter.setItemList(dao.getWithoutOwner());
        if (adapter.getItemCount() > 0) {
            binding.tvWarning.setVisibility(View.GONE);
        } else {
            binding.tvWarning.setVisibility(View.VISIBLE);
        }
    }
}