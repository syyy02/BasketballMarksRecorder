package com.example.basketballmarksrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button BtnBasektball;
    ImageView IVBasketball;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IVBasketball = findViewById(R.id.IVBasketball);
        BtnBasektball = findViewById(R.id.BtnBasketball);
        BtnBasektball.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, BasketballActivity.class);
                startActivity(i);
            }
        });

    }
}