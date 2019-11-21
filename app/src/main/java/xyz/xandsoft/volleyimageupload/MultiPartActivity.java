package xyz.xandsoft.volleyimageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.xandsoft.volleyimageupload.uploader.VolleyMultipartRequest;

public class MultiPartActivity extends AppCompatActivity {

    private final static int IMAGE_REQUEST_CODE = 100;

    private static final String TAG = "MultiPartActivity";

    private boolean isImageAdded = false;

    private Bitmap mBitmap;

    private Button multiContainerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_part);

        multiContainerBtn = findViewById(R.id.multi_container_btn);

        multiContainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(
                                Intent.ACTION_GET_CONTENT).setType("image/*"),
                        IMAGE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && data != null) {

            Uri path = data.getData();

            try {

                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        path);

                ((CircleImageView) findViewById(R.id.multi_img_view))
                        .setImageBitmap(mBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            multiContainerBtn.setText("Upload");
            multiContainerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage(imageIntoBytes(mBitmap));
                }
            });
        }
    }

    private synchronized byte[] imageIntoBytes(Bitmap bitmap) {

        ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mByteArrayOutputStream);

        return mByteArrayOutputStream.toByteArray();
    }

    private synchronized void uploadImage(final byte[] imageData) {

        VolleyMultipartRequest mVolleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                "URL",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String theResponse = null;

                        try {

                            theResponse = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));

                            Toast.makeText(MultiPartActivity.this, theResponse,
                                    Toast.LENGTH_SHORT).show();

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {

                Map<String, DataPart> mMap = new HashMap<>();
                mMap.put("file", new DataPart("dummyName.jpg", imageData,
                        "image/jpeg"));

                return mMap;
            }
        };
        Volley.newRequestQueue(MultiPartActivity.this)
                .add(mVolleyMultipartRequest);
    }
}
