package com.standalone.passwordkeeper;

import android.database.Cursor;

import com.standalone.core.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class SecretDao extends Dao<Secret> {
    public Secret getMasterSecret() {
        Secret secret = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE master = 1", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                secret = getValues(cursor);
            }
            cursor.close();
        }

        return secret;
    }

    public List<Secret> getWithoutMaster() {
        List<Secret> secretList = new ArrayList<>();
        Cursor curs = db.rawQuery("SELECT * FROM " + tableName + " WHERE master != 1", null);
        if (curs != null) {
            if (curs.moveToFirst()) {
                do {
                    secretList.add(getValues(curs));
                } while (curs.moveToNext());
            }

            curs.close();
        }

        return secretList;
    }
}
