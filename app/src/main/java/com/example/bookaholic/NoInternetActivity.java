package com.example.bookaholic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.bookaholic.Tools.showToast;

import com.google.android.material.button.MaterialButton;

public class NoInternetActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        MaterialButton buttonCheckAgain = findViewById(R.id.buttonTryConnectAgain);
        buttonCheckAgain.setOnClickListener(v -> checkConnection());
    }

    private void checkConnection() {
        if (Tools.isOnline(this))
            NoInternetActivity.this.finish();
        else
            showToast(this, R.string.still_no_internet);
    }
}
