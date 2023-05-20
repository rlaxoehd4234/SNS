package com.ffsns.sns.repository;

import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {


    Page<PostEntity> findAllByUserEntity(UserEntity entity , Pageable pageable);
}
