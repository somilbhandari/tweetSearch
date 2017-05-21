package com.twtsearch.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twtsearch.domain.TweetMin;

@Repository
public interface TweetMinRepository extends JpaRepository<TweetMin, Long> 
{
	public TweetMin findTop1ByHashOrderByIdDesc(String hash);
	
	@Query("SELECT tm FROM TweetMin tm WHERE LOWER(tm.hash) = LOWER(:hashStr) ORDER BY (tm.id) DESC")
    public List<TweetMin> findByHash(@Param("hashStr") String hashStr, Pageable pageable);

	@Query("SELECT tm FROM TweetMin tm WHERE LOWER(tm.hash) = LOWER(:hashStr) AND (tm.id) < (:leastId) ORDER BY (tm.id) DESC")
    public List<TweetMin> findByHashAndIdLessThan(@Param("hashStr") String hashStr, @Param("leastId") Long leastId, Pageable pageable);

	@Query("SELECT tm FROM TweetMin tm WHERE LOWER(tm.hash) = LOWER(:hashStr) AND (tm.id) > (:leastId) ORDER BY (tm.id) DESC")
    public List<TweetMin> findByHashAndIdGreaterThan(@Param("hashStr") String hashStr, @Param("leastId") Long leastId);

}
