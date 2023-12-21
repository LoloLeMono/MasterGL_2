package exo1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TermIdentifier
{
	private HashMap<String, Integer> termsMap = new HashMap<>();

    public TermIdentifier(String filePath) throws IOException {
        loadTerms(filePath);
    }

    private void loadTerms(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//") || line.trim().isEmpty()) continue; // Ignorer les commentaires et les lignes vides
                String[] parts = line.split(";");
                String term = parts[1].replace("\"", "").trim().intern(); // Supprimer les guillemets et espaces superflus
                int id = Integer.parseInt(parts[0]);
                termsMap.put(term, id);
            }
        }
    }

    public int getTermId(String term) {
        return termsMap.getOrDefault(term, -1);
    }
}
