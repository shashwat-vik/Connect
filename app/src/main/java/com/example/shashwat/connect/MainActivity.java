package com.example.shashwat.connect;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button b1; TextView tv1;
    TextToSpeech tst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach elements by ids
        b1 = findViewById(R.id.but);
        tv1 = findViewById(R.id.tv1);

        // Attach Listeners
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoEvent();
            }
        });

        // Text-to-speech
        tst = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tst.setLanguage(Locale.forLanguageTag("bn_IN"));
                }
                Set<Locale> languages = tst.getAvailableLanguages();
                for(Locale lang : languages) {
                    tv1.append(lang.getDisplayName() + "\t\t" + lang.toString() + "\n\n");
                }
            }
        });

        // Consecutive calls to server
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoEvent();
                handler.postDelayed(this, 3000);
            }
        }, 5000);
    }

    public void autoEvent() {
        Ion.with(getApplicationContext())
                .load("http://192.168.0.106:5000").setTimeout(600)
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
                else {
                    Toast.makeText(getApplicationContext(), result.get("name").toString(),
                            Toast.LENGTH_SHORT).show();
                    tst.speak(result.get("name").toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        };
        return fc;
    }
}