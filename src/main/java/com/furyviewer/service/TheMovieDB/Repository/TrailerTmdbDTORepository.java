package com.furyviewer.service.TheMovieDB.Repository;

import com.furyviewer.service.dto.TheMovieDB.Trailer.TrailerTmdbDTO;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Repositorio encargado de conectar con la api de TheMovieDB para recuperar la informacion del trailer de Movie y
 * Series.
 * @author IFriedkin
 * @see com.furyviewer.service.dto.TheMovieDB.Trailer.TrailerTmdbDTO
 */
public interface TrailerTmdbDTORepository {
    /**
     * Peticion a la api de TMDB para recibir el trailer de una Movie.
     * @param id int | id utilizado para identificar la Movie en la api.
     * @param apikey String | Key requerida por la api para poder hacer la peticion.
     * @return Call | Contiene toda la informacion del trailer proporcionada por la api.
     */
    @GET("/3/movie/{id}/videos")
    Call<TrailerTmdbDTO> getMovieTrailer(@Path("id") int id, @Query("api_key") String apikey);

    /**
     * Peticion a la api de TMDB para recibir el trailer de Series.
     * @param id int | id utilizado para identificar la Series en la api.
     * @param apikey String | Key requerida por la api para poder hacer la peticion.
     * @return Call | Contiene toda la informacion del trailer proporcionada por la api.
     */
    @GET("/3/tv/{id}/videos")
    Call<TrailerTmdbDTO> getSeriesTrailer(@Path("id") int id, @Query("api_key") String apikey);

    public static String url = " https://api.themoviedb.org/";
    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
}
