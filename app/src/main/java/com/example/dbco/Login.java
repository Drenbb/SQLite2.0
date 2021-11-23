package com.example.dbco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btnLog,btnSign;
    EditText etLog,etPass;
    DBHelper dbHelper;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLog = (Button) findViewById(R.id.btnLog);
        btnLog.setOnClickListener(this);
        btnSign = (Button) findViewById(R.id.btnSign);
        btnSign.setOnClickListener(this);
        etLog = (EditText) findViewById(R.id.etLog);
        etPass = (EditText) findViewById(R.id.etPass);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_LOGIN,"Admin");
        contentValues.put(DBHelper.KEY_PASSWORD,"Admin");
        database.insert(DBHelper.TABLE_USERS,null,contentValues);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLog:
                Cursor findUser = database.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);

                boolean check = false;

                if(findUser.moveToNext())
                {
                    int userlogInd = findUser.getColumnIndex(DBHelper.KEY_LOGIN);
                    int userPass = findUser.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do
                        {
                            if(etLog.getText().toString().equals(findUser.getString(userlogInd)) && etPass.getText().toString().equals(findUser.getString(userPass)))
                            {
                                if(etLog.getText().toString().equals("Admin") &&  etPass.getText().toString().equals("Admin"))
                                {
                                    startActivity(new Intent(this, MainActivity.class));
                                    check = true;
                                    break;
                                }
                                else {
                                    startActivity(new Intent(this, ShopActivity2.class));
                                    check = true;
                                    break;
                                }
                            }
                        }while (findUser.moveToNext());

                }
                findUser.close();
                if(!check)
                    Toast.makeText(this,"Данного пользователя нет в системе",Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSign:
                Cursor find = database.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);

                boolean logged = false;

                if(find.moveToNext()) {
                    int userlogInd = find.getColumnIndex(DBHelper.KEY_LOGIN);
                    int userPass = find.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do {
                        if (etLog.getText().toString().equals(find.getString(userlogInd)) && etPass.getText().toString().equals(find.getString(userPass))) {
                            Toast.makeText(this, "Данный пользователь уже есть в системе", Toast.LENGTH_LONG).show();
                            logged = true;
                            break;
                        }
                    } while (find.moveToNext());

                }
                if(!logged)
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN,etLog.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD,etPass.getText().toString());
                    database.insert(DBHelper.TABLE_USERS,null,contentValues);
                    Toast.makeText(this, "Успешная регистрация", Toast.LENGTH_LONG).show();
                }
                find.close();
                break;

        }
    }
}