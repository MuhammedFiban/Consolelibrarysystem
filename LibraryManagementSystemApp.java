package com.yourcompany.librarymanagement;

import com.yourcompany.librarymanagement.database.DatabaseInitializer;
import com.yourcompany.librarymanagement.book.BookManager;
import com.yourcompany.librarymanagement.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryManagementSystemApp {

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library_system";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "mysql4277";

	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			com.yourcompany.librarymanagement.database.DatabaseInitializer.createTables(connection);
			com.yourcompany.librarymanagement.core.LibraryManagementSystem.runApp(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
