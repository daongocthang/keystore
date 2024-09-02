package com.standalone.core.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.standalone.core.R;

public class DialogUtil {
    public static void showAlertDialog(Context context, String msg) {
        new MaterialAlertDialogBuilder(context)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        return progressDialog;
    }

    public static class ProgressDialog extends Dialog {

        public ProgressDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_progress);

            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(false);
        }
    }
}
