package com.example.exercise3;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    DatabaseHelper db;
    EditText url;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    int index = 0;
    Button addUrl, btn_next, btn_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        url = findViewById(R.id.url);
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);

        db = new DatabaseHelper(MainActivity.this);
        storeUrl();

        Glide.with(MainActivity.this)
                .load(list.get(index))
                .centerCrop()
                .into(imageView);

        addUrl = findViewById(R.id.addUrl);
        addUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                String url_image = url.getText().toString().trim();
                if(url_image.isEmpty()){
                    Toast.makeText(MainActivity.this, "Required Url", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.addUrl(url_image);
                    Toast.makeText(MainActivity.this, "Add url image successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


//        list.add(0, "https://i.imgur.com/Q9WPlWA.jpeg");
//        list.add(1, "https://i.imgur.com/oPj4A8u.jpeg");
//        list.add(2, "https://i.imgur.com/MWAcQRM.jpeg");
//        list.add(3, "https://i.imgur.com/Lnt9K7l.jpeg");
//        list.add(4, "https://i.imgur.com/Gg6BpGn.jpeg");
    }

    void storeUrl()
    {
        Cursor cursor = db.showImage();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                id.add(cursor.getString(0));
                list.add(cursor.getString(1));
            }
        }
    }

}