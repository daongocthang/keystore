package com.standalone.passwordkeeper.user;

import android.database.Cursor;

import com.standalone.core.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class UserDao extends Dao<User> {
    public User getOwner() {
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE username = ?", new String[]{"owner"});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = getValues(cursor);
            }
            cursor.close();
        }

        return user;
    }

    public List<User> getWithoutOwner() {
        List<User> userList = new ArrayList<>();
        Cursor curs = db.rawQuery("SELECT * FROM " + tableName + " WHERE username != ?", new String[]{"owner"});
        if (curs != null) {
            if (curs.moveToFirst()) {
                do {
                    userList.add(getValues(curs));
                } while (curs.moveToNext());
            }

            curs.close();
        }

        return userList;
    }
}
