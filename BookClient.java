/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_manage;

/**
 *
 * @author User
 */


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BookClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private JFrame frame;
    private JTextField titleField;
    private JTextField authorField;
    private JTextArea displayArea;

    public BookClient() {
        frame = new JFrame("Library Management System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(10, 20, 80, 25);
        panel.add(titleLabel);

        titleField = new JTextField(20);
        titleField.setBounds(100, 20, 165, 25);
        panel.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(10, 50, 80, 25);
        panel.add(authorLabel);

        authorField = new JTextField(20);
        authorField.setBounds(100, 50, 165, 25);
        panel.add(authorField);

        JButton addButton = new JButton("Add Book");
        addButton.setBounds(10, 80, 120, 25);
        panel.add(addButton);

        JButton listButton = new JButton("List Books");
        listButton.setBounds(150, 80, 120, 25);
        panel.add(listButton);

        displayArea = new JTextArea();
        displayArea.setBounds(10, 110, 360, 240);
        panel.add(displayArea);

        frame.add(panel);
        frame.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        listButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listBooks();
            }
        });
    }

    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("ADD_BOOK");
            out.println(title);
            out.println(author);
            JOptionPane.showMessageDialog(null, "Book added successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void listBooks() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("LIST_BOOKS");
            String response;
            StringBuilder sb = new StringBuilder();
            while ((response = in.readLine()) != null) {
                sb.append(response).append("\n");
            }
            displayArea.setText(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
