package com.standalone.passwordkeeper.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.core.adapter.AbsAdapter;
import com.standalone.passwordkeeper.FormDialog;
import com.standalone.passwordkeeper.databinding.ItemUserBinding;

public class UserAdapter extends AbsAdapter<User, UserAdapter.ViewHolder> {

    final AppCompatActivity activity;

    public UserAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemUserBinding itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), activity);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding itemBinding;

        public ViewHolder(@NonNull ItemUserBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }


        public void bind(User user, AppCompatActivity parent) {
            itemBinding.title.setText(user.username);
            itemBinding.subtitle.setText(user.title);

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", user.getId());
                    FormDialog formDialog = new FormDialog();
                    formDialog.setArguments(bundle);
                    formDialog.show(parent.getSupportFragmentManager(), FormDialog.TAG);
                }
            });
        }
    }
}
