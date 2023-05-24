package com.ffsns.sns.repository;

import com.ffsns.sns.model.entity.AlarmEntity;
import com.ffsns.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    Page<AlarmEntity> findAllByUserEntity(UserEntity user, Pageable pageable);
}
