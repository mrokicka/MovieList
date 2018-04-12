package com.example.android.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by android on 4/3/18.
 */

public class AddActivity extends AppCompatActivity {

    private TextView movie;
    private EditText code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        movie = findViewById(R.id.movieTitle);
        code = findViewById(R.id.movieCode);
    }

    @Override
    public void onBackPressed() {
        if(!movie.getText().toString().trim().equals(null) && !code.getText().toString().trim().equals(null)) {
            Intent data = new Intent();
            data.putExtra("MOVIE", movie.getText().toString().trim());
            data.putExtra("CODE", code.getText().toString().trim());
            setResult(RESULT_OK, data);
            finish();
        }
        finish();
    }
}
