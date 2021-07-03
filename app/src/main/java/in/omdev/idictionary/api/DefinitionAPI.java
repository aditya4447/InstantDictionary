package in.omdev.idictionary.api;

import java.util.List;

import in.omdev.idictionary.entity.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DefinitionAPI {

    String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/";

    @GET("{language}/{word}")
    Call<List<Result>> getDefinitions(@Path("word") String word, @Path("language") String language);
}
