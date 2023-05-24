package com.ffsns.sns.repository;

import com.ffsns.sns.model.entity.LikeEntity;
import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserEntityAndPostEntity(UserEntity userEntity, PostEntity postEntity);

    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.postEntity =:post ")
    Integer countByPostEntity(@Param("post") PostEntity post);
    Page<LikeEntity> findByPostEntity(PostEntity entity, Pageable pageable);
}
