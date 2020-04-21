package com.littleit.whatsappclone.view.starup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.littleit.whatsappclone.view.MainActivity;
import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.view.auth.PhoneLoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Button btnAgree = findViewById(R.id.btn_agree);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeScreenActivity.this, PhoneLoginActivity.class));
                finish();
            }
        });
    }
}
