package xyz.xandsoft.volleyimageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check the device OS version for Read external data permission
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            Permissions.check(
                    MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    null,
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            // Nothing
                        }
                    }
            );

        findViewById(R.id.main_multi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        MultiPartActivity.class));
            }
        });

        findViewById(R.id.main_base_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        Base64Activity.class));
            }
        });
    }
}
