package com.example.bookaholic;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookaholic.details.Book;

public class ManageBook extends AppCompatActivity {
    RecyclerView recyclerView;
    private ImageView returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_view);
        returnBtn = findViewById(R.id.returnManage);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBackPressed();
            }
        });
        initManageBook();
    }

    public void initManageBook(){
        ManageBookAdapter adapter = new ManageBookAdapter(ManageBook.this, Book.allBooks);
        recyclerView = findViewById(R.id.manageLayout);
        recyclerView.setAdapter(adapter);
    }
}
