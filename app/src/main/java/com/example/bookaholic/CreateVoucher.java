package com.example.bookaholic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookaholic.databinding.ActivityCreateVoucherBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class CreateVoucher extends AppCompatActivity {
    private ActivityCreateVoucherBinding binding;
    private DatabaseReference database;

    private String name, id, type, start, end;
    private int discount, minimum, quantity, limit;
    String temp1, temp2, temp3;

    private TextView startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateVoucherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance().getReference("Vouchers");

        // type
        Spinner spinner = binding.typeVoucher;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.typeVoucher_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = parent.getItemAtPosition(0).toString();
            }
        });
        startDate = binding.startVoucher;
        endDate = binding.endVoucher;

        // Show DatePicker when TextView is clicked
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog và thiết lập sự kiện chọn ngày
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateVoucher.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // Thiết lập ngày được chọn vào TextView
                        start = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear+1, year);
                        startDate.setText(start);
                    }
                }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog và thiết lập sự kiện chọn ngày
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateVoucher.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // Thiết lập ngày được chọn vào TextView
                        end = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear+1, year);
                        endDate.setText(end);
                    }
                }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;
                // title
                if (binding.nameVoucher.getText().toString().trim().isEmpty()) {
                    binding.nameVoucher.setError("Please enter a value");
                    check = false;
                }
                else
                    name = binding.nameVoucher.getText().toString();
                //author
                if (binding.idVoucher.getText().toString().trim().isEmpty()) {
                    binding.idVoucher.setError("Please enter a value");
                    check = false;
                }
                else
                    id = binding.idVoucher.getText().toString();
                //desciption
                if (binding.discountVoucher.getText().toString().trim().isEmpty()){
                    binding.discountVoucher.setError("Please enter a value");
                    check = false;
                }
                else
                    discount = Integer.parseInt(binding.discountVoucher.getText().toString());
                //quantity
                if (binding.minimumVoucher.getText().toString().trim().isEmpty()){
                    binding.minimumVoucher.setError("Please enter a value");
                    check = false;
                }
                else {
                    minimum = Integer.parseInt(binding.minimumVoucher.getText().toString());
                }
                // price
                if (binding.quantityVoucher.getText().toString().trim().isEmpty()) {
                    binding.quantityVoucher.setError("Please enter a value");
                    check = false;
                }
                else {
                    quantity = Integer.parseInt(binding.quantityVoucher.getText().toString());
                }
                // publisher
                if (binding.limitVoucher.getText().toString().trim().isEmpty()){
                    binding.limitVoucher.setError("Please enter a value");
                    check = false;
                }
                else
                    limit = Integer.parseInt(binding.limitVoucher.getText().toString());
                if(check == true)
                    uploadData();
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void uploadData() {
        Voucher voucher = new Voucher(name, id, type, start, end,
        discount, minimum, quantity, limit);
        String key = database.child("Books").push().getKey();
        voucher.setId(key);
        database.child(key).setValue(voucher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successful Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateVoucher.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateVoucher.this, MainActivity.class));
                    }
                });
    }
}