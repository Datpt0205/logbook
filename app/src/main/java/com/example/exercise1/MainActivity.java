package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ArrayList<String> list;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        list = new ArrayList<String>();
        list.add(0, "https://i.imgur.com/Q9WPlWA.jpeg");
        list.add(1, "https://i.imgur.com/oPj4A8u.jpeg");
        list.add(2, "https://i.imgur.com/MWAcQRM.jpeg");
        list.add(3, "https://i.imgur.com/Lnt9K7l.jpeg");
        list.add(4, "https://i.imgur.com/Gg6BpGn.jpeg");

        loadImage();
    }

    public void loadImage(){
        Glide.with(MainActivity.this)
                .load(list.get(index))
                .centerCrop()
                .into(imageView);
    }

    public void nextImage(View view){
        index++;
        if(index >= list.size())
            index = 0;
        loadImage();
    }

    public void previousImage(View view){
        index--;
        if(index <= -1)
            index = list.size() -1;
        loadImage();
    }
}