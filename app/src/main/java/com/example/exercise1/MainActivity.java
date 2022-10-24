package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ArrayList<String> list;
    private int index = 0;
    private Button btn_next, btn_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        list = new ArrayList<String>();
        list.add( "https://i.imgur.com/Q9WPlWA.jpeg");
        list.add( "https://i.imgur.com/oPj4A8u.jpeg");
        list.add( "https://i.imgur.com/MWAcQRM.jpeg");
        list.add( "https://i.imgur.com/Lnt9K7l.jpeg");
        list.add( "https://i.imgur.com/Gg6BpGn.jpeg");

        Glide.with(MainActivity.this)
                .load(list.get(index))
                .centerCrop()
                .into(imageView);

        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);

        if(index == 0)
        {
            btn_previous.setEnabled(false);
        }

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index --;
                Glide.with(MainActivity.this)
                        .load(list.get(index))
                        .centerCrop()
                        .into(imageView);
                if(index == 0){
                    btn_previous.setEnabled(false);
                }
                if(index > 0){
                    btn_previous.setEnabled(true);
                }
                if(index == Integer.valueOf(list.size()-1))
                {
                    btn_next.setEnabled(false);
                }else
                {
                    btn_next.setEnabled(true);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index ++;
                Glide.with(MainActivity.this)
                        .load(list.get(index))
                        .centerCrop()
                        .into(imageView);
                if(index == 0){
                    btn_previous.setEnabled(false);
                }
                if(index > 0){
                    btn_previous.setEnabled(true);
                }
                if(index == Integer.valueOf(list.size()-1))
                {
                    btn_next.setEnabled(false);
                }else
                {
                    btn_next.setEnabled(true);
                }

            }
        });

    }

//    public void nextImage(View view){
//        index++;
//        if(index >= list.size())
//            index = 0;
//        loadImage();
//    }
//
//    public void previousImage(View view){
//        index--;
//        if(index <= -1)
//            index = list.size() -1;
//        loadImage();
//    }
}