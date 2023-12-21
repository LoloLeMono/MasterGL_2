package exo1;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            TermIdentifier termIdentifier = new TermIdentifier("path_to_file.txt");
            System.out.println(termIdentifier.getTermId("chat de gouttière")); // Exemple
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}