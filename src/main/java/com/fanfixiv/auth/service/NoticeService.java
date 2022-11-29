package com.fanfixiv.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.dto.BaseResultDto;
import com.fanfixiv.auth.dto.notice.NoticeResultDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.exception.BadRequestException;
import com.fanfixiv.auth.repository.jpa.NoticeRepository;
import com.fanfixiv.auth.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;

  private final UserRepository userRepository;

  public List<NoticeResultDto> findNotice(User user) {
    return this.noticeRepository.findByToAllTrue().stream()
        .map(x -> new NoticeResultDto(x.getSeq(), x.getContent()))
        .toList();
  }

  public List<NoticeResultDto> findPersonal(User user) {
    UserEntity u = this.userRepository.findById(user.getUserSeq()).orElseThrow();

    return u.getNotice().stream().map(x -> new NoticeResultDto(x.getSeq(), x.getContent())).toList();
  }

  public BaseResultDto checkPersonal(User user, Long seq) {
    if (this.noticeRepository.existsByUserSeqAndSeq(user.getUserSeq(), seq)) {
      this.noticeRepository.updateChecked(seq);
      return new BaseResultDto();
    }
    throw new BadRequestException("해당 유저에게 해당하는 개인 알림이 존재하지 않습니다.");
  }
}
