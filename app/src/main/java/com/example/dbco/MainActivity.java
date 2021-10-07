package com.example.dbco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnRead, btnClear;
    EditText etEmail, etName;
    TextView Output;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.Read);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);
        Output = (TextView) findViewById(R.id.Output);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String output ="";
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {
            case R.id.Add:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                etName.setText(null);
                etEmail.setText(null);
                break;
            case R.id.Read:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursor.moveToFirst())
                {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do {
                        output += ("ID = " + cursor.getInt(idIndex) + ", name = " + cursor.getString(nameIndex) + ", email = " + cursor.getString(emailIndex)+"\n");
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) + ", name = " + cursor.getString(nameIndex) + ", email = " + cursor.getString(emailIndex));
                    }
                    while (cursor.moveToNext());
                    Output.setText(output.toString());

                }
                else {
                    Log.d("mLog", "0 rows");
                    Output.setText("Список пуст ");
                }
                cursor.close();
                break;
            case R.id.Clear:
                database.delete(DBHelper.TABLE_CONTACTS,null,null);
                etName.setText(null);
                etEmail.setText(null);
                output = null;
                Output.setText(null);
                break;
        }
        dbHelper.close();
    }
}