package dz.khidma.express.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.internet.MimeMessage;

/**
 * Provides a fallback no-op JavaMailSender when no MailSender is auto-configured.
 * This allows the application context to start even if SMTP properties are not set.
 * In production, set spring.mail.* properties to enable the real sender provided by
 * Spring Boot's MailSenderAutoConfiguration.
 */
@Configuration
public class MailConfig {

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender noOpMailSender() {
        return new NoOpMailSender();
    }

    static class NoOpMailSender implements JavaMailSender {
        private static final Logger log = LoggerFactory.getLogger(NoOpMailSender.class);

        @Override
        public MimeMessage createMimeMessage() {
            return new MimeMessage((jakarta.mail.Session) null);
        }

        @Override
        public MimeMessage createMimeMessage(java.io.InputStream contentStream) throws MailException {
            try {
                return new MimeMessage(null, contentStream);
            } catch (Exception e) {
                throw new org.springframework.mail.MailParseException("Failed to create MimeMessage from stream", e);
            }
        }

        @Override
        public void send(MimeMessage mimeMessage) throws MailException {
            log.info("[NoOpMailSender] send(MimeMessage) called. Email suppressed.");
        }

        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {
            log.info("[NoOpMailSender] send(MimeMessage...) called ({} messages). Emails suppressed.", mimeMessages != null ? mimeMessages.length : 0);
        }

        @Override
        public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
            log.info("[NoOpMailSender] send(MimeMessagePreparator) called. Email suppressed.");
        }

        @Override
        public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
            log.info("[NoOpMailSender] send(MimeMessagePreparator...) called ({} preparators). Emails suppressed.", mimeMessagePreparators != null ? mimeMessagePreparators.length : 0);
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            if (simpleMessage != null) {
                log.info("[NoOpMailSender] Suppressed email to='{}' subject='{}'", String.join(",", simpleMessage.getTo() != null ? simpleMessage.getTo() : new String[]{}), simpleMessage.getSubject());
            } else {
                log.info("[NoOpMailSender] send(SimpleMailMessage) called with null. Email suppressed.");
            }
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            int count = simpleMessages != null ? simpleMessages.length : 0;
            log.info("[NoOpMailSender] Suppressed {} simple email(s).", count);
        }
    }
}
