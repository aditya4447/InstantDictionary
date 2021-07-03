package in.omdev.idictionary.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import in.omdev.idictionary.databinding.ActivityMainBinding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkOverLayPermission();
    }

    private void checkOverLayPermission() {
        if (Settings.canDrawOverlays(this)) {
            binding.groupPermissionGranted.setVisibility(View.VISIBLE);
            binding.groupGetPermission.setVisibility(View.GONE);
        } else {
            binding.groupGetPermission.setVisibility(View.VISIBLE);
            binding.groupPermissionGranted.setVisibility(View.GONE);
        }
    }

    public void getPermission(View v) {
        Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        myIntent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(myIntent);
    }

    public void search(View v) {
        Intent i = new Intent(this, DictionaryActivity.class);
        i.putExtra(Intent.EXTRA_PROCESS_TEXT, Objects.requireNonNull(
                binding.etWord.getText()).toString());
        startActivity(i);
    }
}