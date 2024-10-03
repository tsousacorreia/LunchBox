package br.com.etecia.lunchbox;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // Construtores
    @GET("Api.php?apicall=getConstrutores")
    Call<ConstrutoresResponse> getConstrutores();

    // Reguladores
    @GET("Api.php?apicall=getReguladores")
    Call<ReguladoresResponse> getReguladores();

    // Energéticos
    @GET("Api.php?apicall=getEnergeticos")
    Call<EnergeticosResponse> getEnergeticos();

    // Snacks
    @GET("Api.php?apicall=getSnacks")
    Call<SnacksResponse> getSnacks();
}