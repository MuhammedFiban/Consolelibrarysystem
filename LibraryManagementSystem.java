package com.yourcompany.librarymanagement.core;

import java.sql.Connection;
import java.util.Scanner;

public class LibraryManagementSystem {

	public static void runApp(Connection connection) {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			displayMenu();

			choice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			switch (choice) {
			case 1:
				com.yourcompany.librarymanagement.book.BookManager.addBook(connection, scanner);
				break;
			case 2:
				com.yourcompany.librarymanagement.book.BookManager.searchBook(connection, scanner);
				break;
			case 3:
				com.yourcompany.librarymanagement.book.BookManager.viewBookDetails(connection, scanner);
				break;
			case 4:
				com.yourcompany.librarymanagement.transaction.TransactionManager.lendBook(connection, scanner);
				break;
			case 5:
				com.yourcompany.librarymanagement.transaction.TransactionManager.listBooksToBeReturnedToday(connection);
				break;
			case 6:
				com.yourcompany.librarymanagement.book.BookManager.countBooksByGenre(connection);
				break;
			case 0:
				System.out.println("Exiting the application. Goodbye!");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 0);

		scanner.close();
	}

	private static void displayMenu() {
		System.out.println("\nLibrary Management System");
		System.out.println("1. Add a new book");
		System.out.println("2. Search for a book and its availability");
		System.out.println("3. View details of a selected book");
		System.out.println("4. Lend a book to a user");
		System.out.println("5. List books to be returned today");
		System.out.println("6. Count of books by genre");
		System.out.println("0. Exit");
		System.out.print("Enter your choice: ");
	}
}
