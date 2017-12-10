package com.example.shashwat.connect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = findViewById(R.id.but);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEvent();
            }
        });
    }

    public void clickEvent() {
        Ion.with(getApplicationContext())
                .load("http://192.168.0.100:5000").setTimeout(600)
                .asJsonObject()
                .setCallback(responseCallBack());
    }

    public FutureCallback responseCallBack() {
        FutureCallback fc = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(result == null)
                    Toast.makeText(getApplicationContext(), "TIMEOUT",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), result.get("name").toString(),
                            Toast.LENGTH_SHORT).show();
            }
        };
        return fc;
    }
}