package com.example.webtoonservice.repository;

import com.example.webtoonservice.model.Toon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface ToonRepository extends JpaRepository<Toon, Integer>{
    
    @Query(value="Select t.tno , t.title from toon t", nativeQuery = true)
    List<Map<String, Object>> getToonIdAndName();

    @Query(value="Select t.tno , t.title from toon t where t.user_no = ?1", nativeQuery = true)
    List<Map<String, Object>> getToonIdAndNameByUser(Long user_no);

    @Query(value="Select * from toon t where t.user_no = ?1", nativeQuery = true)
    Collection<Toon> findByUser(Long user_no);
}