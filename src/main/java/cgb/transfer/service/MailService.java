package cgb.transfer.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBatchReport(String to, String refLot, LocalDate date, int successCount, int failureCount) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Rapport de traitement du lot : " + refLot);

        String content = "Bonjour,\n\n" +
                         "Le traitement du lot " + refLot + " du " + date + " est terminé.\n" +
                         "Résumé des transactions :\n" +
                         "- Réussies : " + successCount + "\n" +
                         "- En échec : " + failureCount + "\n\n" +
                         "Cordialement,\nCrédit Général Bank";

        message.setText(content);
        mailSender.send(message);
    }
}