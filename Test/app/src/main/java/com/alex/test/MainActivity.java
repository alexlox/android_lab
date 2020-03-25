package com.alex.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = findViewById(R.id.mainList);
        String[] products = {"Product1", "Product2"};

        ArrayAdapter<String> items = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, products);


        list.setAdapter(items);
        list.setOnItemClickListener(this.itemClickedHandler);

        // Storage
        //File file = new File(this.getFilesDir(), "storage");
        StringBuilder storeBuilder = new StringBuilder();
        for (String product: products) {
            storeBuilder.append(product).append(",");
        }

        String store = storeBuilder.toString();

        try {
            FileOutputStream fos = this.openFileOutput("storage", Context.MODE_PRIVATE);
            fos.write(store.getBytes());
        } catch (IOException e) {
            Log.d("storage_write", "File exception.");
        }

        this.accessStorage("storage");
    }

    private AdapterView.OnItemClickListener itemClickedHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView productInfo = findViewById(R.id.productInfo);
            productInfo.setText("You clicked " + id + ".");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("start", "on Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume", "on Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pause", "on Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("stop", "on Stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("restart", "on Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("destroy", "on Destroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView productInfo = findViewById(R.id.productInfo);
        outState.putCharSequence("selectedItemDescription", productInfo.getText());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView productInfo = findViewById(R.id.productInfo);
        productInfo.setText(savedInstanceState.getCharSequence("selectedItemDescription"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.cart:
                String message = "Sent message";
                String key = "com.alex.test.message";

                Intent intent = new Intent(this, CartActivity.class);
                intent.putExtra(key, message);
                startActivity(intent);
                break;
            case R.id.help:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

                alertBuilder.setTitle("Help")
                        .setMessage("Press product to see product details.\nPress the cart button from menu to go to cart.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                            }
                        });
                alertBuilder.create().show();
                break;
            case R.id.settings:
                Intent i = new Intent(this, PrefActivity.class);
                startActivity(i);
            default:
        }

        return true;
    }

    public void accessStorage(String filename) {
        try {
            FileInputStream fis = this.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.d("storageRead", "Error in using file reader.");
            } finally {
                String contents = stringBuilder.toString();
                Log.d("storageRead", "File read: " + contents);
                Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Log.d("storageRead", "File not found in storage.");
        }
    }
}
