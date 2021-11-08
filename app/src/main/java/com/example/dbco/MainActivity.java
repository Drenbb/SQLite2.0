package com.example.dbco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnClear,btnGoPg;
    EditText  etName,etPrice;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);
        btnGoPg = (Button) findViewById(R.id.newPg);
        btnGoPg.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }

    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_PRODUCT);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputFam = new TextView(this);
                params.weight = 3.0f;
                outputFam.setLayoutParams(params);
                outputFam.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputFam);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);




                dbOutput.addView(dbOutputRow);

            }while(cursor.moveToNext());

        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);

        switch (v.getId()) {
            case R.id.Add:
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_PRODUCT, name);
                contentValues.put(DBHelper.KEY_PRICE, price);

                database.insert(DBHelper.TABLE_PRODUCTS, null, contentValues);
                etName.setText(null);
                etPrice.setText(null);
                UpdateTable();
                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_PRODUCTS,null,null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                etName.setText(null);
                etPrice.setText(null);

                UpdateTable();
                break;
            case R.id.newPg:
                Intent intent = new Intent(this, ShopActivity2.class);
                startActivity(intent);
                break;
            default:
                if(((Button) v).getText()== "Удалить") {
                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();
                    database.delete(DBHelper.TABLE_PRODUCTS, DBHelper.KEY_ID + "=?", new String[]{String.valueOf((v.getId()))});
                    contentValues = new ContentValues();
                    Cursor cursorUpdater = database.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
                    if (cursorUpdater.moveToFirst()) {
                        int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                        int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODUCT);
                        int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                        int realID = 1;
                        do {
                            if (cursorUpdater.getInt(idIndex) > realID) {
                                contentValues.put(DBHelper.KEY_ID, realID);
                                contentValues.put(DBHelper.KEY_PRODUCT, cursorUpdater.getString(nameIndex));
                                contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                                database.replace(DBHelper.TABLE_PRODUCTS, null, contentValues);

                            }
                            realID++;
                        } while (cursorUpdater.moveToNext());
                        if (cursorUpdater.moveToLast() && v.getId() != realID) {
                            database.delete(DBHelper.TABLE_PRODUCTS, DBHelper.KEY_ID + "=?", new String[]{cursorUpdater.getString(idIndex)});
                        }
                        UpdateTable();
                    }

                }
                break;
        }
        dbHelper.close();
    }
}