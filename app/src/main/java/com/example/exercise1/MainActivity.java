package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private String[] imageList;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        imageList = getResources().getStringArray(R.array.images);

        String url = "https://i.imgur.com/Gg6BpGn.jpeg";

        loadImage();
    }

    public void loadImage(){
        Glide.with(MainActivity.this)
                .load(imageList[index])
                .centerCrop()
                .into(imageView);
    }

    public void nextImage(View view){
        index++;
        if(index >= imageList.length)
            index = 0;
        loadImage();
    }

    public void previousImage(View view){
        index--;
        if(index <= -1)
            index = imageList.length -1;
        loadImage();
    }
}