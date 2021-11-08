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

public class ShopActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button btnBuy, btnBack;
    TextView txtKort;
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop2);

        btnBuy = (Button) findViewById(R.id.Buy);
        btnBuy.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.Back);
        btnBack.setOnClickListener(this);
        txtKort = (TextView) findViewById(R.id.txtSum);
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

                Button buyBtn = new Button(this);
                buyBtn.setOnClickListener(this);
                params.weight=1.0f;
                buyBtn.setLayoutParams(params);
                buyBtn.setText("Купить");
                buyBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(buyBtn);
                dbOutput.addView(dbOutputRow);

            }while(cursor.moveToNext());

        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        switch (v.getId()) {
            case R.id.Buy:

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вы купили товаров на " + txtKort.getText().toString()+" у.е.д", Toast.LENGTH_SHORT);
                toast.show();
                txtKort.setText("0");
                break;
            case R.id.Back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                if(((Button) v).getText() == "Купить")
                {
                    Cursor cursorFind = database.query(DBHelper.TABLE_PRODUCTS,null,DBHelper.KEY_ID + "=?",new String[]{String.valueOf(v.getId())}, null, null, null);
                    float sumBuy = Float.valueOf(txtKort.getText().toString());
                    float sum = 0;
                    if(cursorFind.moveToFirst())
                    {
                        int pricer = cursorFind.getColumnIndex(DBHelper.KEY_PRICE);
                        do{
                            sum = cursorFind.getFloat(pricer);
                        }while(cursorFind.moveToNext());
                    }
                    cursorFind.close();
                    sumBuy +=sum;
                    txtKort.setText(String.valueOf(sumBuy));
                }
                break;
        }
        dbHelper.close();
    }
}