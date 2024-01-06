package com.yourcompany.librarymanagement.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookManager {

	public static void addBook(Connection connection, Scanner scanner) {
		System.out.print("Enter book name: ");
		String bookName = scanner.nextLine();

		// Check if the book already exists
		if (isBookExists(connection, bookName)) {
			updateBookQuantity(connection, bookName, 1);
		} else {
			System.out.print("Enter author: ");
			String author = scanner.nextLine();

			System.out.print("Enter genre: ");
			String genre = scanner.nextLine();

			System.out.print("Enter ISBN number: ");
			String isbn = scanner.nextLine();

			try {
				PreparedStatement addBook = connection.prepareStatement(
						"INSERT INTO books (name, author, genre, isbn, quantity) VALUES (?, ?, ?, ?, 1)");
				addBook.setString(1, bookName);
				addBook.setString(2, author);
				addBook.setString(3, genre);
				addBook.setString(4, isbn);
				addBook.executeUpdate();

				System.out.println("Book added successfully!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void searchBook(Connection connection, Scanner scanner) {
		System.out.print("Enter book name to search: ");
		String searchQuery = scanner.nextLine();

		try {
			PreparedStatement searchStatement = connection.prepareStatement("SELECT * FROM books WHERE name LIKE ?");
			searchStatement.setString(1, "%" + searchQuery + "%");
			ResultSet resultSet = searchStatement.executeQuery();

			System.out.println("Search results:");
			while (resultSet.next()) {
				String bookName = resultSet.getString("name");
				String author = resultSet.getString("author");
				int quantity = resultSet.getInt("quantity");

				System.out.println("Book: " + bookName + ", Author: " + author + ", Quantity: " + quantity
						+ ", Availability: " + (quantity > 0 ? "Available" : "Not Available"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void viewBookDetails(Connection connection, Scanner scanner) {
		System.out.print("Enter book name to view details: ");
		String bookName = scanner.nextLine();

		try {
			PreparedStatement detailsStatement = connection.prepareStatement("SELECT * FROM books WHERE name = ?");
			detailsStatement.setString(1, bookName);
			ResultSet resultSet = detailsStatement.executeQuery();

			if (resultSet.next()) {
				String author = resultSet.getString("author");
				int quantity = resultSet.getInt("quantity");
				String isbn = resultSet.getString("isbn");

				System.out.println("Book Details:");
				System.out.println("Book: " + bookName);
				System.out.println("Author: " + author);
				System.out.println("Quantity: " + quantity);
				System.out.println("ISBN: " + isbn);
			} else {
				System.out.println("Book not found!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean isBookExists(Connection connection, String bookName) {
		try {
			PreparedStatement checkBook = connection.prepareStatement("SELECT * FROM books WHERE name = ?");
			checkBook.setString(1, bookName);
			ResultSet resultSet = checkBook.executeQuery();

			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void updateBookQuantity(Connection connection, String bookName, int quantityChange) {
		try {
			PreparedStatement updateQuantity = connection
					.prepareStatement("UPDATE books SET quantity = quantity + ? WHERE name = ?");
			updateQuantity.setInt(1, quantityChange);
			updateQuantity.setString(2, bookName);
			updateQuantity.executeUpdate();

			System.out.println("Book quantity updated successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void lendBook(Connection connection, Scanner scanner) {
		System.out.print("Enter book name to lend: ");
		String bookName = scanner.nextLine();

		try {
			// Retrieve book details
			PreparedStatement bookDetailsStatement = connection.prepareStatement("SELECT * FROM books WHERE name = ?");
			bookDetailsStatement.setString(1, bookName);
			ResultSet resultSet = bookDetailsStatement.executeQuery();

			if (resultSet.next()) {
				int bookId = resultSet.getInt("id");

				// Capture user details
				System.out.print("Enter user name: ");
				String userName = scanner.nextLine();
				System.out.print("Enter return date (yyyy-MM-dd): ");
				String returnDateStr = scanner.nextLine();
				Date returnDate = parseDate(returnDateStr);

				// Insert transaction
				insertTransaction(connection, bookId, userName, returnDate);

				System.out.println("Book lent successfully.");
			} else {
				System.out.println("Book not found!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void listBooksToBeReturnedToday(Connection connection) {
		try {
			PreparedStatement returnTodayStatement = connection
					.prepareStatement("SELECT b.name, t.user_name, t.return_date " + "FROM transactions t "
							+ "JOIN books b ON t.book_id = b.id " + "WHERE DATE(t.return_date) = CURDATE()");
			ResultSet resultSet = returnTodayStatement.executeQuery();

			System.out.println("Books to be returned today:");

			while (resultSet.next()) {
				System.out.println("Book Name: " + resultSet.getString("book_id"));
				System.out.println("Borrower: " + resultSet.getString("borrower"));
				System.out.println("Return Date: " + resultSet.getString("return_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void countBooksByGenre(Connection connection) {
		try {
			PreparedStatement countByGenre = connection
					.prepareStatement("SELECT genre, COUNT(*) as count FROM books GROUP BY genre");
			ResultSet resultSet = countByGenre.executeQuery();

			System.out.println("Count of books by genre:");
			while (resultSet.next()) {
				String genre = resultSet.getString("genre");
				int count = resultSet.getInt("count");

				System.out.println("Genre: " + genre + ", Count: " + count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
