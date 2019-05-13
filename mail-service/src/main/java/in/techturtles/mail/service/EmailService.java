package in.techturtles.mail.service;

import in.techturtles.mail.entity.dto.UserDto;

public interface EmailService {

    void sendSimpleMessage(UserDto input);
}
