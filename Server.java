/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_manage;

/**
 *
 * @author User
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started. Listening on port " + SERVER_PORT);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handleClient(clientSocket);
                    }
                });
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command = in.readLine();

            if ("ADD_BOOK".equals(command)) {
                String title = in.readLine();
                String author = in.readLine();
                addBook(title, author);
            } else if ("LIST_BOOKS".equals(command)) {
                List<Book> books = listBooks();
                for (Book book : books) {
                    out.println(book.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addBook(String title, String author) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO books (title, author, available) VALUES (?, ?, ?)")) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setBoolean(3, true);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Book> listBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM books")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean available = resultSet.getBoolean("available");
                books.add(new Book(id, title, author, available));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
