package com.ffsns.sns.repository;

import com.ffsns.sns.model.entity.CommentEntity;
import com.ffsns.sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPostEntity(PostEntity post, Pageable pageable);
}
