package com.yourcompany.librarymanagement.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TransactionManager {

    public static void lendBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book ID to lend: ");
        int bookId = scanner.nextInt();

        System.out.print("Enter user name: ");
        scanner.nextLine(); // Consume the newline character
        String userName = scanner.nextLine();

        System.out.print("Enter user phone number: ");
        String phoneNumber = scanner.nextLine();

        // Get the current date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = dateFormat.format(currentDate);

        try {
            // Check if the book is available
            PreparedStatement checkAvailability = connection.prepareStatement(
                    "SELECT quantity FROM books WHERE id = ?");
            checkAvailability.setInt(1, bookId);
            ResultSet availabilityResult = checkAvailability.executeQuery();

            if (availabilityResult.next() && availabilityResult.getInt("quantity") > 0) {
                // Decrease the quantity of the book
                PreparedStatement decreaseQuantity = connection.prepareStatement(
                        "UPDATE books SET quantity = quantity - 1 WHERE id = ?");
                decreaseQuantity.setInt(1, bookId);
                decreaseQuantity.executeUpdate();

                // Insert the transaction record
                PreparedStatement lendBook = connection.prepareStatement(
                        "INSERT INTO transactions (book_id, user_name, phone_number, return_date) " +
                                "VALUES (?, ?, ?, ?)");
                lendBook.setInt(1, bookId);
                lendBook.setString(2, userName);
                lendBook.setString(3, phoneNumber);
                lendBook.setString(4, currentDateStr);
                lendBook.executeUpdate();

                System.out.println("Book lent successfully!");
            } else {
                System.out.println("Book not available for lending.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listBooksToBeReturnedToday(Connection connection) {
        try {
            // Get the current date
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateStr = dateFormat.format(currentDate);

            // Query books to be returned today
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT b.name, t.return_date FROM transactions t " +
                            "JOIN books b ON t.book_id = b.id " +
                            "WHERE t.return_date = ?");
            preparedStatement.setString(1, currentDateStr);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Book: " + resultSet.getString("name"));
                System.out.println("Return Date: " + resultSet.getString("return_date"));
                System.out.println("-----------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
