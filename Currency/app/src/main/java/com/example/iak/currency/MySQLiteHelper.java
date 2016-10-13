package com.example.iak.currency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	public MySQLiteHelper(Context context) {
		super(context, "currency.db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//
		onUpgrade(db, 0, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion==0) {
			db.execSQL("create table currency(rowid integer PRIMARY KEY, quote, price, last_update)");
			return;
		}
		if (oldVersion != newVersion) {

		}
	}

}