package com.example.greenpulse.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivityCreatePostBinding;
import com.example.greenpulse.responses.PostResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends OtherActivity {

    private final int REQUEST_STORAGE_PERMISSION = 1000;
    private final int REQUEST_IMAGE_PICK = 10000;
    ActivityCreatePostBinding binding;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.pickerCreatePost.setOnClickListener((v)->{
            askPermission();


        });
        binding.askBtn.setOnClickListener((v)->{
            String des = binding.descriptionEditText.getText().toString();
            String title = binding.titleEditText.getText().toString();
            // Validation
            if (des.isEmpty()) {
                binding.descriptionEditText.setError("Enter a valid description!");
                return;
            }
            if (title.isEmpty()) {
                binding.titleEditText.setError("Enter a valid title!");
                return;
            }
            // Make the POST request to the server
            if (selectedImageUri != null) {
                try {
                    createPost(title, des, selectedImageUri);
                    Toast.makeText(this, "Post Created Successfully!!!",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                } catch (IOException e) {
                    Toast.makeText(this, "Error creating post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_LONG).show();
            }

        });

    }
    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
        else{
            callImagePicker();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void callImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.pickerCreatePost.setImageURI(selectedImageUri);
                binding.pickerCreatePost.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }
        }
    }
    private void createPost(String title, String description, Uri imageUri) throws IOException {
        // Prepare Retrofit API instance
        GPApi gpApi = RetrofitInstance.gpApi();

        // Prepare the image file
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Prepare text fields as RequestBody
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

        // Make the API call
        Call<PostResponse> call = gpApi.createPost(1L, titlePart, descriptionPart, body);  // Assume 1L is the userId, update accordingly
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreatePostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreatePostActivity.this, "Error creating post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return uri.getPath(); // Fallback if cursor is null
        }
    }

}