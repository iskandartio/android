package com.example.iak.currency;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class db {
	MySQLiteHelper conn;
	SQLiteDatabase db;

	public db(Context context) {
		conn = new MySQLiteHelper(context);
		db = conn.getWritableDatabase();

	}

	public void execMe(String sql) {
		db.execSQL(sql);

	}
	public void del(String tbl, String where, String[] params) {
		int i=db.delete(tbl,  where, params);

	}
	public void execMe(String sql, ArrayList<String> params) {
		SQLiteStatement st = db.compileStatement(sql);
		int i=1;
		for (String param : params) {
			st.bindString(i++, param);
		}
		st.executeInsert();
	}
	public Cursor query(String sql) {
		return conn.getReadableDatabase().rawQuery(sql, null);
	}
	public Cursor query(String sql, ArrayList<String> params) {
		return db.rawQuery(sql, params.toArray(new String[params.size()]));

	}
	public void close() {
		conn.close();
	}

	public String select_single(String sql, ArrayList<String> params) {
		Cursor c=query(sql, params);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			return c.getString(0);

		}
		return "";
	}
	public void begin() {
		db.beginTransaction();
	}
	public void commit() {
		db.endTransaction();
	}
}
