package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddIdeaActivity extends AppCompatActivity {

    private EditText newTitle, newDesc;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idea);

        newTitle = findViewById(R.id.newTitleText);
        newDesc = findViewById(R.id.newDescText);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = newTitle.getText().toString();
                String desc = newDesc.getText().toString();
                if(TextUtils.isEmpty(title)){
                    Snackbar.make(v, "Please insert a title" + desc,Snackbar.LENGTH_LONG);
                } else {
                    Intent intentResult = new Intent();
                    intentResult.putExtra(MainActivity.EXTRATEXT_TITLE, title);
                    intentResult.putExtra(MainActivity.EXTRATEXT_DESC, desc);
                    setResult(Activity.RESULT_OK, intentResult);
                    finish();
                }
            }
        });

    }
}
