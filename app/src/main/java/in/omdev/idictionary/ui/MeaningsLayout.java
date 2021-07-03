package in.omdev.idictionary.ui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import in.omdev.idictionary.Const;
import in.omdev.idictionary.Util;
import in.omdev.idictionary.api.DefinitionAPI;
import in.omdev.idictionary.databinding.ItemDefinitionBinding;
import in.omdev.idictionary.databinding.ItemMeaningBinding;
import in.omdev.idictionary.databinding.ItemPhoneticBinding;
import in.omdev.idictionary.databinding.ItemResultBinding;
import in.omdev.idictionary.databinding.ItemSynonymBinding;
import in.omdev.idictionary.databinding.WindowMeaningsBinding;
import in.omdev.idictionary.entity.Definition;
import in.omdev.idictionary.entity.Meaning;
import in.omdev.idictionary.entity.Phonetic;
import in.omdev.idictionary.entity.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeaningsLayout extends LinearLayout implements Callback<List<Result>> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final ArrayList<Result> results = new ArrayList<>();
    private WindowMeaningsBinding binding;

    public MeaningsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setOrientation(VERTICAL);
    }

    public void init(String word, WindowMeaningsBinding binding) {
        this.binding = binding;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        int margin = (int) Util.convertDpToPixel(8, context);
        layoutParams.setMargins(margin,margin,margin,margin);
//        addViewInLayout(btnClose, 0, layoutParams);
        DefinitionAPI api = new Retrofit.Builder()
                .baseUrl(DefinitionAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DefinitionAPI.class);
        api.getDefinitions(word, "en_US")
                .enqueue(this);
        api.getDefinitions(word, "en_GB")
                .enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<Result>> call,
                           @NonNull Response<List<Result>> response) {
        binding.pbLoading.setVisibility(View.GONE);
        if (response.body() == null || response.body().size() == 0) {
            if (results.size() == 0) {
                binding.tvWordNotFound.setVisibility(View.VISIBLE);
            }
            return;
        }
        binding.tvWordNotFound.setVisibility(View.GONE);
        List<Result> results = response.body();
        this.results.addAll(results);
        for (Result result : results) {
            ItemResultBinding resultBinding = ItemResultBinding.inflate(layoutInflater);

            String word = result.getWord();
            if (call.request().url().toString().contains("US")) {
                word = "US english: " + word;
            } else if (call.request().url().toString().contains("GB")) {
                word = "British english: " + word;
            }
            resultBinding.resultWord.setText(word);

            for (Phonetic phonetic : result.getPhonetics()) {
                ItemPhoneticBinding phoneticBinding = ItemPhoneticBinding.inflate(layoutInflater);
                phoneticBinding.btnPronounce.setOnClickListener(v -> AsyncTask.execute(() -> {
                    MediaPlayer mp = new MediaPlayer();
                    mp.setOnCompletionListener(MediaPlayer::release);
                    try {
                        mp.setDataSource(phonetic.getAudio());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                phoneticBinding.tvPhonetic.setText(phonetic.getText());
                resultBinding.layoutPhonetics.addView(phoneticBinding.getRoot(),
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }

            for (Meaning meaning : result.getMeanings()) {
                ItemMeaningBinding meaningBinding = ItemMeaningBinding.inflate(layoutInflater);
                meaningBinding.tvPartOfSpeech.setText(meaning.getPartOfSpeech());
                for (Definition definition : meaning.getDefinitions()) {
                    ItemDefinitionBinding definitionBinding =
                            ItemDefinitionBinding.inflate(layoutInflater);
                    definitionBinding.tvDefinition.setText(definition.getDefinition());
                    definitionBinding.tvExample.setText(definition.getExample());
                    for (String synonym : definition.getSynonyms()) {
                        ItemSynonymBinding synonymBinding =
                                ItemSynonymBinding.inflate(layoutInflater);
                        synonymBinding.getRoot().setText(synonym);
                        synonymBinding.getRoot().setOnClickListener(v -> {
                            Intent i = new Intent(context, DictionaryActivity.class);
                            i.putExtra(Intent.EXTRA_PROCESS_TEXT, synonym);
                            context.startActivity(i);
                        });
                        definitionBinding.layoutSynonyms.addView(synonymBinding.getRoot(),
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    }
                    meaningBinding.layoutDefinitions.addView(definitionBinding.getRoot(),
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                }
                resultBinding.layoutMeanings.addView(meaningBinding.getRoot(),
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }

            binding.listResult.addView(resultBinding.getRoot(),
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Result>> call, @NonNull Throwable t) {
        Log.d(Const.LOG_TAG, "onFailure: " + t.getLocalizedMessage());
        binding.pbLoading.setVisibility(View.GONE);
        if (results.size() == 0) {
            binding.tvWordNotFound.setVisibility(View.VISIBLE);
        }
    }
}