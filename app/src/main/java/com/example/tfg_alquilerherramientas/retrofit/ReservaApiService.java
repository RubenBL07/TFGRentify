package com.example.tfg_alquilerherramientas.retrofit;

import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.modelos.Reserva;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ReservaApiService {
    @GET("/api/reservas/findById")
    public Call<Reserva> getReservaById(@Query("id") Long id);

    @GET("/api/reservas/findAll")
    public Call<List<Reserva>> getAllReservas();

    @GET("/api/reservas/findByClienteId")
    public Call<List<Reserva>> getAllReservasByClienteId(@Query("clienteId") Long id);

    @POST("/api/reservas/create")
    public Call<Boolean> postReserva(@Body Reserva reserva);

    @DELETE("/api/reservas/delete")
    public Call<Boolean> deleteReserva(@Query("id") Long id);

    @PUT("/api/reservas/update")
    public Call<Boolean> updateReserva(@Query("id") Long id, @Body Reserva reserva);

    @PUT("/api/reservas/finalizar")
    public Call<Boolean> finalizarReserva(@Query("id") Long id);
}
