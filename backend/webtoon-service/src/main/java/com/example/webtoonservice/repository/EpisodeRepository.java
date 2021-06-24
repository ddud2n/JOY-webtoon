package com.example.webtoonservice.repository;

import com.example.webtoonservice.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    

    @Query(value="Select * from episode e where e.toon_id = ?1", nativeQuery = true)
    Collection<Episode> getEpi(Integer id); // 웹툰 id -> 전체 에피소드 조회

    @Query(value="Select * from episode e where e.eno = ?1", nativeQuery = true)
    Episode getEpiById(Integer id); // 에피소드 id 로 특정 에피소드 조회
}