package com.example.exercise2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ArrayList<String> list = new ArrayList<String>();
    int index = 0;
    Button addUrl;
    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        url = findViewById(R.id.url);

        addUrl = findViewById(R.id.addUrl);
        addUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_image = url.getText().toString().trim();
                if(url_image.isEmpty()){
                    Toast.makeText(MainActivity.this, "Required Url", Toast.LENGTH_SHORT).show();
                }
                else{
                    list.add(url_image);
                    Toast.makeText(MainActivity.this, "Add url image successfully", Toast.LENGTH_SHORT).show();
                    loadImage();
                    url.setText("");
                }
            }
        });
//        list.add(0, "https://i.imgur.com/Q9WPlWA.jpeg");
//        list.add(1, "https://i.imgur.com/oPj4A8u.jpeg");
//        list.add(2, "https://i.imgur.com/MWAcQRM.jpeg");
//        list.add(3, "https://i.imgur.com/Lnt9K7l.jpeg");
//        list.add(4, "https://i.imgur.com/Gg6BpGn.jpeg");
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