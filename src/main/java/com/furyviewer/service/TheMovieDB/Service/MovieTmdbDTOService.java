package com.furyviewer.service.TheMovieDB.Service;

import com.furyviewer.domain.Movie;
import com.furyviewer.repository.MovieRepository;
import com.furyviewer.service.TheMovieDB.Repository.MovieTmdbDTORepository;
import com.furyviewer.service.dto.TheMovieDB.Episode.Cast;
import com.furyviewer.service.dto.TheMovieDB.Episode.EpisodeCastingDTO;
import com.furyviewer.service.dto.TheMovieDB.Movie.SimpleMovieTmdbDTO;
import com.furyviewer.service.util.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

/**
 * Servicio encargado de recuperar informacion de una Movie desde MovieTmdbDTORepository.
 *
 * @author IFriedkin
 * @see com.furyviewer.service.TheMovieDB.Repository.MovieTmdbDTORepository
 */
@Service
public class MovieTmdbDTOService {
    /**
     * Key proporcionada por la api de TheMovieDB para poder hacer peticiones.
     */
    private final String apikey = "08526181d206d48ab49b3fa0be7ad1bf";

    /**
     * Se establece conexion para poder hacer peticiones a la api.
     */
    private final MovieTmdbDTORepository apiTMDB = MovieTmdbDTORepository.retrofit.create(MovieTmdbDTORepository.class);

    @Autowired
    private ArtistService artistService;

    @Autowired
    private FindTmdbDTOService findTmdbDTOService;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Devuelve el id de la api de TMDB a partir del nombre de la movie.
     *
     * @param movieTitle String | Nombre de la movie a buscar.
     * @return int | id de la movie.
     * @deprecated
     */
    public int getIdTmdbMovie(String movieTitle) {
        int id = -1;
        SimpleMovieTmdbDTO movie;
        Call<SimpleMovieTmdbDTO> callMovie = apiTMDB.getSimpleMovie(apikey, movieTitle);

        try {
            Response<SimpleMovieTmdbDTO> response = callMovie.execute();

            if (response.isSuccessful()) {
                movie = response.body();
                System.out.println(movie);

                if (movie.getTotalResults() != 0) {
                    id = movie.getResults().get(0).getId();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * Busca los artist de una movie en la api y los vuelca en la base de datos.
     * @param movie Movie | Movie de la que se quiere conocer los artist.
     */
    public void importActors(Movie movie) {
        int idTmdb = -1;

        idTmdb = findTmdbDTOService.getIdTmdbMovieByImdbId(movie.getImdbIdExternalApi());

        if (idTmdb != -1) {
            Call<EpisodeCastingDTO> callCredits = apiTMDB.getCredits(idTmdb, apikey);

            try {
                Response<EpisodeCastingDTO> response = callCredits.execute();

                if (response.isSuccessful()) {
                    List<Cast> casting = response.body().getCast();

                    movie.setActorMains(artistService.importActorsTMdb(casting));

                    movieRepository.save(movie);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
