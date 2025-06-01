package com.example.tfg_alquilerherramientas.retrofit;

import com.example.tfg_alquilerherramientas.modelos.Herramienta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface HerramientaApiService {
    @GET("/api/herramientas/findById")
    public Call<Herramienta> getHerramientaById(@Query("id") Long id);

    @GET("/api/herramientas/findAll")
    public Call<List<Herramienta>> getAllHerramientas();

    @POST("/api/herramientas/create")
    public Call<Boolean> postHerramienta(@Body Herramienta herramienta);

    @DELETE("/api/herramientas/delete")
    public Call<Boolean> deleteHerramienta(@Query("id") Long id);

    @PUT("/api/herramientas/update")
    public Call<Boolean> updateHerramienta(@Query("id") Long id, @Body Herramienta herramienta);

    @GET("/api/herramientas/findAllActivas")
    public Call<List<Herramienta>> getAllHerramientasActivas();

    @GET("/api/herramientas/findAllInactivas")
    public Call<List<Herramienta>> getAllHerramientasInactivas();

    @PUT("/api/herramientas/darDeBaja")
    public Call<Boolean> darDeBajaHerramienta(@Query("id") Long id);

    @PUT("/api/herramientas/reactivar")
    public Call<Boolean> reactivarHerramienta(@Query("id") Long id);
}
