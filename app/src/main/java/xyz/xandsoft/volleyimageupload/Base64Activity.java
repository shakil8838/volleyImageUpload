package xyz.xandsoft.volleyimageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Base64Activity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 100;

    private static final String TAG = "Base64Activity";

    private Bitmap mBitmap;

    private Button containerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base64);

        containerBtn = findViewById(R.id.base_container_btn);
        containerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), IMAGE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && data != null) {

            Uri path = data.getData();

            try {

                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                containerBtn.setText("Upload");
                containerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImage(imageIntoString(mBitmap));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized String imageIntoString(Bitmap bitmap) {

        ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mByteArrayOutputStream);

        return Base64.encodeToString(mByteArrayOutputStream.toByteArray(), Base64.DEFAULT);

    }

    private synchronized void uploadImage(final String imageData) {

        StringRequest mStringRequest = new StringRequest(
                Request.Method.POST,
                "Url",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Base64Activity.this, response,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> mMap = new HashMap<>();
                mMap.put("image", imageData);

                return mMap;
            }
        };
        Volley.newRequestQueue(Base64Activity.this)
                .add(mStringRequest);
    }
}
