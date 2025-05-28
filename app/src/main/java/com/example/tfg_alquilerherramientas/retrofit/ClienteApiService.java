package com.example.tfg_alquilerherramientas.retrofit;

import com.example.tfg_alquilerherramientas.modelos.Cliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClienteApiService {
    @GET("/api/clientes/findById")
    public Call<Cliente> getClienteById(@Query("id") Long id);

    @GET("/api/clientes/findByEmail")
    public Call<Cliente> getClienteByEmail(@Query("email") String email);

    @GET("/api/clientes/findAll")
    public Call<List<Cliente>> getAllClientes();

    @POST("/api/clientes/create")
    public Call<Boolean> postCliente(@Body Cliente cliente);

    @DELETE("/api/clientes/delete")
    public Call<Boolean> deleteCliente(@Query("id") Long id);

}
