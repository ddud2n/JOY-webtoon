package com.example.webtoonservice.repository;

import com.example.webtoonservice.model.ToonThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ToonThumbnailRepository extends JpaRepository<ToonThumbnail, Integer> {
    @Modifying(clearAutomatically = true) // 해당 쿼리 메서드 실행 직 후, 역속성 컨텍스트를 clear
    @Transactional
    @Query(value="Delete from toon_thumbnail where toon_no = ?1", nativeQuery = true)
    void deleteToonThumbnail(Integer id);
    
    @Query(value="Select * from toon_thumbnail t where t.toon_no = ?1", nativeQuery = true)
    Optional<ToonThumbnail> getToonThumbnailByID(int id);

    @Query(value="Select toon_no, file_uri from toon_thumbnail", nativeQuery=true)
    Optional<ToonThumbnail> getToonThumbnail();
}