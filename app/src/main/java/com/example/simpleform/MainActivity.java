package com.example.simpleform;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import java.io.*;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView date, expenseAmount,expenseDescription;
    private String expenseType;
    private Spinner spinner;
    private static final String[] paths = {"Health", "Groceries", "HouseExpenses"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner)findViewById(R.id.static_spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        expenseAmount = findViewById(R.id.expenseamount);
        expenseDescription = findViewById(R.id.expensedescription);


        Button b = findViewById(R.id.signUpButtonId);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat(pattern, new Locale("da", "DK"));

                String strDate = simpleDateFormat.format(new Date());

                final String urlAddress = "https://achuth.requestcatcher.com/test";
                sendPost(urlAddress,strDate,expenseAmount.getText().toString(),expenseDescription.getText().toString(),expenseType);

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                expenseType = "Health";
                break;
            case 1:
                expenseType = "Groceries";
                break;
            case 2:
                expenseType = "HouseExpenses";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void sendPost(final String urlAddress, final String date, final String expenseAmount, final String description, final String category) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("date", date);
                    jsonParam.put("expenseAmount",expenseAmount);
                    jsonParam.put("expenseDescription", description);
                    jsonParam.put("category", category);


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

