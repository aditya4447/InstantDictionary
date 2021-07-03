package in.omdev.idictionary.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import in.omdev.idictionary.R;
import in.omdev.idictionary.databinding.WindowMeaningsBinding;

public class DictionaryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CharSequence styledWord = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (styledWord == null || styledWord.toString().trim().isEmpty()) {
            finish();
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        String word = styledWord.toString().trim();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                0,
                PixelFormat.RGBA_8888);
        params.gravity = Gravity.CENTER;
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowMeaningsBinding binding = WindowMeaningsBinding.inflate(getLayoutInflater());

        binding.tvWord.setText(word);
        binding.btnClose.setOnClickListener(v -> {
            WindowManager windowManager1 = (WindowManager) v.getContext()
                    .getSystemService(WINDOW_SERVICE);
            windowManager1.removeView((ViewGroup) v.getParent());
        });
        binding.btnClose.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_baseline_close_24));
        windowManager.addView(binding.getRoot(), params);
        binding.listResult.init(word, binding);
        finish();
    }
}