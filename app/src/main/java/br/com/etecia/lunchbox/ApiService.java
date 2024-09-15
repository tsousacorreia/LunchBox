package br.com.etecia.lunchbox;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;

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

    // Perfis de crianças
    @GET("Api.php?apicall=getPerfis")
    Call<PerfisResponse> getPerfis();

    // Adicionar novo perfil
    @FormUrlEncoded
    @POST("Api.php?apicall=addPerfil")
    Call<DefaultResponse> addPerfil(
            @Field("nome") String nome,
            @Field("idade") int idade,
            @Field("alergias") String alergias,
            @Field("preferencias") String preferencias
    );

    // Selecionar dia da semana
    @FormUrlEncoded
    @POST("Api.php?apicall=setDiaSemana")
    Call<DefaultResponse> setDiaSemana(
            @Field("perfilId") int perfilId,
            @Field("dia") String dia
    );
}