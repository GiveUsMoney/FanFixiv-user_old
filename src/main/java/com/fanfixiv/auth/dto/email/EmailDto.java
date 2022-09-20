package com.fanfixiv.auth.dto.email;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailDto {
  // TODO: 후일 다른 이메일로 교체해주세요.
  public static final String FROM_EMAIL = "jack555586@gmail.com";

  private final List<String> to; // 받는 사람
  private final String subject; // 제목
  private final String content; // 본문

  public EmailDto(final List<String> to, final String subject, final String content) {
    this.to = to;
    this.subject = subject;
    this.content = content;
  }

  public SendEmailRequest toSendRequestDto() {
    final Destination destination = new Destination().withToAddresses(this.to);

    final Message message =
        new Message()
            .withSubject(createContent(this.subject))
            .withBody(new Body().withHtml(createContent(this.content)));

    return new SendEmailRequest()
        .withSource(FROM_EMAIL)
        .withDestination(destination)
        .withMessage(message);
  }

  private Content createContent(final String text) {
    return new Content().withCharset("UTF-8").withData(text);
  }
}
