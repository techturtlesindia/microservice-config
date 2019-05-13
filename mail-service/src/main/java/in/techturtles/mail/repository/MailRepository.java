package in.techturtles.mail.repository;

import in.techturtles.mail.entity.Mail;
import org.springframework.data.repository.CrudRepository;


public interface MailRepository extends CrudRepository<Mail, Long> {

}