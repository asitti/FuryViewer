package com.furyviewer.repository;

import com.furyviewer.domain.Series;
import com.furyviewer.domain.SeriesStats;
import com.furyviewer.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the SeriesStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeriesStatsRepository extends JpaRepository<SeriesStats, Long> {

    @Query("select series_stats from SeriesStats series_stats where series_stats.user.login = ?#{principal.username}")
    List<SeriesStats> findByUserIsCurrentUser();


    @Query("select count(seriesStats) from SeriesStats seriesStats where seriesStats.status = com.furyviewer.domain.enumeration.SeriesStatsEnum.FOLLOWING and seriesStats.id=:SeriesId")
    Long FollowingSeriesStats(@Param("SeriesId")Long id);

    @Query("select count(seriesStats) from SeriesStats seriesStats  where seriesStats.status= com.furyviewer.domain.enumeration.SeriesStatsEnum.PENDING and seriesStats.id=:SeriesId")
    Long PendingSeriesStats(@Param("SeriesId")Long id);

    @Query("select count(seriesStats) from SeriesStats seriesStats  where seriesStats.status= com.furyviewer.domain.enumeration.SeriesStatsEnum.SEEN and seriesStats.id=:SeriesId")
    Long SeenSeriesStats(@Param("SeriesId")Long id);

    @Query("select s.status from SeriesStats s where s.serie.id=:id and s.user.login= :login")
    String selectSeriesStatus(@Param("id") Long id, @Param("login") String login);

    @Query("select ss.serie from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.FOLLOWING and ss.user.login=:userLogin")
    List<Series>  followingSeriesUser(@Param("userLogin") String userLogin);

    @Query("select ss.serie from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.FOLLOWING and ss.user.login=:userLogin")
    List<Series>  followingSeriesUserPageable(@Param("userLogin") String userLogin, Pageable pageable);

    @Query("select ss.serie from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.SEEN and ss.user.login=:userLogin")
    List<Series>  seenSeriesUser(@Param("userLogin") String userLogin, Pageable pageable);

    @Query("select ss.serie from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.PENDING and ss.user.login=:userLogin")
    List<Series>  pendingSeriesUser(@Param("userLogin") String userLogin, Pageable pageable);

    @Query("select count(ss.serie) from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.FOLLOWING and ss.user.login=:userLogin")
    Integer  countFollowingSeriesUser(@Param("userLogin") String userLogin);

    @Query("select count(ss.serie) from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.SEEN and ss.user.login=:userLogin")
    Integer  countSeenSeriesUser(@Param("userLogin") String userLogin);

    @Query("select count(ss.serie) from SeriesStats ss where ss.status=com.furyviewer.domain.enumeration.SeriesStatsEnum.PENDING and ss.user.login=:userLogin")
    Integer  countPendingSeriesUser(@Param("userLogin") String userLogin);


    Optional<SeriesStats> findByUserAndSerieId(User user, Long id);

}
