package com.example.bookaholic;

import static android.content.ContentValues.TAG;

import static com.example.bookaholic.Tools.showToast;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookaholic.databinding.ActivityUpdateBookBinding;
import com.example.bookaholic.details.Book;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateBook extends AppCompatActivity {
    private ActivityUpdateBookBinding binding;
    private DatabaseReference database;
    private Uri imageUri;

    private final int pickImage = 100;
    private ImageButton imageBtn;

    private TextView publicationDateBook;
    private String image, name, author,
            category, desciption, publicationDate,
            publisher, size, typeOfCover ;
    String temp1, temp2, temp3;

    private int quantity, price, numberOfPages;

    //    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private TextView tvDate;

    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedBook = (Book) getIntent().getSerializableExtra("selectedBook");
        System.out.println(selectedBook.getCategory());
        binding = ActivityUpdateBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance().getReference("Books");

        // category
        Spinner spinner = binding.categoryBook;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int position = adapter.getPosition(selectedBook.getCategory());
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tvDate = binding.publicationDateBook;
        imageBtn = binding.bGallery;

        //setText
        Glide.with(UpdateBook.this)
                .load(selectedBook.getImages().get(0))
                .into(binding.bGallery);
        binding.nameBook.setText(selectedBook.getTitle());
        binding.nameAuthor.setText(selectedBook.getAuthor());
        binding.priceBook.setText(selectedBook.getPrice().toString());
        binding.descriptionBook.setText(selectedBook.getDescription());
        binding.quantityBook.setText(selectedBook.getQuantity().toString());
        binding.publicationDateBook.setText(selectedBook.getPublicationDate());
        binding.publisherBook.setText(selectedBook.getPublisher());
        binding.sizeBook.setText(selectedBook.getSize());
        binding.numberOfPagesBook.setText(selectedBook.getNumberOfPages().toString());
        binding.typeOfCoverBook.setText(selectedBook.getTypeOfCover());


        binding.bGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, pickImage);
            }
        });

        // Show DatePicker when TextView is clicked
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog và thiết lập sự kiện chọn ngày
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateBook.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // Thiết lập ngày được chọn vào TextView
                        publicationDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear+1, year);
                        tvDate.setText(publicationDate);
                    }
                }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;
                // title
                if (binding.nameBook.getText().toString().trim().isEmpty()) {
                    binding.nameBook.setError("Please enter a value");
                    check = false;
                }
                else
                    name = binding.nameBook.getText().toString();
                //author
                if (binding.nameAuthor.getText().toString().trim().isEmpty()) {
                    binding.nameAuthor.setError("Please enter a value");
                    check = false;
                }
                else
                    author = binding.nameAuthor.getText().toString();
                //desciption
                if (binding.descriptionBook.getText().toString().trim().isEmpty()){
                    binding.descriptionBook.setError("Please enter a value");
                    check = false;
                }
                else
                    desciption = binding.descriptionBook.getText().toString();
                //quantity
                if (binding.quantityBook.getText().toString().trim().isEmpty()){
                    binding.quantityBook.setError("Please enter a value");
                    check = false;
                }
                else {
                    temp1 = binding.quantityBook.getText().toString();
                    quantity = Integer.parseInt(temp1);
                }
                // price
                if (binding.priceBook.getText().toString().trim().isEmpty()) {
                    binding.priceBook.setError("Please enter a value");
                    check = false;
                }
                else {
                    temp2 = binding.priceBook.getText().toString();
                    price = Integer.parseInt(temp2);
                }
                // publisher
                if (binding.publisherBook.getText().toString().trim().isEmpty()){
                    binding.publisherBook.setError("Please enter a value");
                    check = false;
                }
                else
                    publisher = binding.publisherBook.getText().toString();
                //size
                if (binding.sizeBook.getText().toString().trim().isEmpty()){
                    binding.sizeBook.setError("Please enter a value");
                    check = false;
                }
                else
                    size = binding.sizeBook.getText().toString();
                //numberOfPages
                if (binding.numberOfPagesBook.getText().toString().trim().isEmpty()) {
                    binding.numberOfPagesBook.setError("Please enter a value");
                    check = false;
                }
                else {
                    temp3 = binding.numberOfPagesBook.getText().toString();
                    numberOfPages = Integer.parseInt(temp3);
                }
                //Type
                if (binding.typeOfCoverBook.getText().toString().trim().isEmpty()){
                    binding.typeOfCoverBook.setError("Please enter a value");
                    check = false;
                }
                else
                    typeOfCover = binding.typeOfCoverBook.getText().toString();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data.getData();
            imageBtn.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date now = new Date();
        String fileName = formatter.format(now);
        StorageReference storage = FirebaseStorage.getInstance().getReference("image/" + fileName);

        storage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageBtn.setImageURI(null);
                Toast.makeText(getApplicationContext(), "Successful Uploaded", Toast.LENGTH_SHORT).show();

                File localFile;
                try {
                    localFile = File.createTempFile(fileName, "");
                    storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageBtn.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image = uri.toString();
                        images.add(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void uploadData() {
        if(imageUri == null){
            image = selectedBook.getImageUrl();
            images = selectedBook.getImages();
        }
        Book book = new Book(image, name, author, category, desciption, quantity, price
                , publicationDate, publisher, size, numberOfPages, typeOfCover, images, null, selectedBook.getBuyer(), selectedBook.getRecentlyDate());
        String bookId = selectedBook.getId(); // Xác định ID của selectedBook
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put(bookId, book.toMap()); // Chuyển selectedBook thành Map và thêm vào một đối tượng Map mới
        database.updateChildren(updateValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successful Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateBook.this, ManageBook.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateBook.this, ManageBook.class));
                    }
                });
    }
}