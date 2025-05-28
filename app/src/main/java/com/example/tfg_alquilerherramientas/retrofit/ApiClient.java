package com.example.tfg_alquilerherramientas.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // 1. Formatter que coincida con lo que envía el servidor (ISO o tu SQL‑friendly)
            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            // ó DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") si tu backend envía con espacio

            // 2. Crea el Gson global
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class,
                            (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                                    new JsonPrimitive(src.format(fmt)))
                    .registerTypeAdapter(LocalDateTime.class,
                            (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                                    LocalDateTime.parse(json.getAsString(), fmt))
                    .create();

            // 3. Retrofit con este Gson
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
