package com.furyviewer.repository;

import com.furyviewer.domain.Artist;
import com.furyviewer.domain.FavouriteMovie;
import com.furyviewer.domain.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Optional;
import com.furyviewer.domain.User;

/**
 * Spring Data JPA repository for the FavouriteMovie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteMovieRepository extends JpaRepository<FavouriteMovie, Long> {

    @Query("select favourite_movie from FavouriteMovie favourite_movie where favourite_movie.user.login = ?#{principal.username}")
    List<FavouriteMovie> findByUserIsCurrentUser();

    Optional<FavouriteMovie> findByMovieAndUserLogin(Movie movie, String login);

    @Query("select count(favourite_movie) from FavouriteMovie favourite_movie where favourite_movie.movie.id=:MovieId")
    Long NumFavsMovie(@Param("MovieId") Long id);

    FavouriteMovie findByUserAndMovieId(User u, Long id);

    @Query("select m.liked from FavouriteMovie m where m.movie.id=:id")
    Boolean selectFavouriteMovie(@Param("id") Long id);

    @Query("select fm.movie from FavouriteMovie fm where fm.liked=true and fm.user.login=:login")
    List<Movie> findFavoriteMovieUserLogin(@Param("login") String login, Pageable pageable);

    @Query("SELECT COUNT(f.liked) FROM FavouriteMovie f WHERE f.liked=true AND f.movie.id=:id")
    Integer countLikedMovie(@Param("id") Long id);

    @Query("SELECT COUNT(f.liked) FROM FavouriteMovie f WHERE f.liked=true AND f.user.login= :login")
    Integer countLikesUser(@Param("login") String login);

    @Query("SELECT COUNT(f) FROM FavouriteMovie f WHERE f.liked=true")
    Integer countTotalMovieFav();
}
