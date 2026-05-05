package cgb.transfer.service;

import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogService {

    private static final String NOM_FICHIER = "log.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String message) {
        try (FileWriter fw = new FileWriter(NOM_FICHIER, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = LocalDateTime.now().format(formatter);
            pw.println(timestamp + " | " + message);

        } catch (IOException e) {
            System.err.println("ERREUR CRITIQUE LOG : " + e.getMessage());
        }
    }
}