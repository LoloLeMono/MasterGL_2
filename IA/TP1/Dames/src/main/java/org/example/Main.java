package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Créez un objet Scanner pour lire l'entrée clavier
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez quelque chose : ");

        // Utilisez la méthode nextLine() pour attendre une entrée de l'utilisateur
        String input = scanner.nextLine();

        // Affichez l'entrée de l'utilisateur
        System.out.println("Vous avez saisi : " + input);

        // N'oubliez pas de fermer le Scanner lorsque vous avez terminé avec lui
        scanner.close();

        Integer n = Integer.valueOf(input);

        // Créez un tableau de taille n x n
        int[][] echiquier = new int[n][n];


    }
}