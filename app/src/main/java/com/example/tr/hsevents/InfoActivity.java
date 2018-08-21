package com.example.tr.hsevents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity
{
    private String locUrl;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        String message = intent.getStringExtra("info");
        locUrl = intent.getStringExtra("locUrl");
        imageUrl = intent.getStringExtra("imageUrl");
        System.out.println(imageUrl);

        TextView textView = (TextView)findViewById(R.id.textView);

        textView.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
    }

    public void ShowMap(View view)
    {
        Intent intent = new Intent(InfoActivity.this, MapActivity.class);
        intent.putExtra("locUrl", locUrl);
        startActivity(intent);
    }
    public void ShowImage(View view)
    {
        Intent intent = new Intent(InfoActivity.this, ImageActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }
}

