package com.fanfixiv.auth.repository.jpa;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fanfixiv.auth.entity.NoticeEntity;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
  List<NoticeEntity> findByToAllTrue();

  boolean existsByUserSeqAndSeq(Long userSeq, Long seq);

  @Modifying
  @Transactional
  @Query(value = "update tb_notice set checked = true where seq = :seq", nativeQuery = true)
  void updateChecked(@Param("seq") Long seq);
}