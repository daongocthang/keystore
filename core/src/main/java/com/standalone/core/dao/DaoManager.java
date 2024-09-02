package com.standalone.core.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.standalone.core.App;
import com.standalone.core.utils.NumberUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class DaoManager extends SQLiteOpenHelper {
    static DaoManager instance;

    DaoManager(Context context, String dbName, int version) {
        super(context, dbName, null, version);
    }

    public static DaoManager getInstance() {
        if (instance == null) {
            Properties config = App.loadEnv();
            String dbName = config.getProperty("DB_NAME");
            if (dbName == null) {
                throw new IllegalArgumentException("Not found name of database ");
            }
            int dbVersion = NumberUtil.toInt(config.getProperty("DB_VERSION"), 1);
            instance = new DaoManager(App.getContext(), dbName, dbVersion);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTables(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    @NotNull
    public SQLiteDatabase getDb() {
        return getWritableDatabase();
    }

    @SuppressLint("Range")
    void dropTables(SQLiteDatabase db) {
        db.beginTransaction();
        try (Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type IS 'table'", null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String tableName = cursor.getString(cursor.getColumnIndex("name"));
                        db.execSQL("DROP TABLE IF EXISTS " + tableName);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
        }
    }

}
