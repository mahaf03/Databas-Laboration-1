package bemsih.databaslaboration1;

import bemsih.databaslaboration1.Model.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Anslutning lyckades!");

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\nVälj ett alternativ:");
                System.out.println("1. Sök efter en bok baserat på titel");
                System.out.println("2. Sök efter en bok baserat på författarnamn");
                System.out.println("3. Avsluta");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Konsumera radbrytningen

                switch (choice) {
                    case 1 -> searchByTitle(connection, scanner);
                    case 2 -> searchByAuthor(connection, scanner);
                    case 3 -> {
                        System.out.println("Avslutar programmet.");
                        running = false;
                    }
                    default -> System.out.println("Ogiltigt val. Försök igen.");
                }
            }
            scanner.close();
        } catch (SQLException e) {
            System.out.println("Anslutning misslyckades.");
            e.printStackTrace();
        }
    }
}
