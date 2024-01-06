package com.yourcompany.librarymanagement.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "author VARCHAR(255) NOT NULL," +
                    "genre VARCHAR(50) NOT NULL," +
                    "isbn VARCHAR(13) NOT NULL," +
                    "quantity INT DEFAULT 0)";
            statement.executeUpdate(createBooksTable);

            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "book_id INT," +
                    "user_name VARCHAR(255) NOT NULL," +
                    "phone_number VARCHAR(15) NOT NULL," +
                    "return_date DATE," +
                    "FOREIGN KEY (book_id) REFERENCES books(id))";
            statement.executeUpdate(createTransactionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
