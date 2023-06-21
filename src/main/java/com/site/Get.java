package com.site;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/get")
public class Get extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("<h1>Enter ID:</h1>");
        String idPerson = req.getParameter("id");
        int id = Integer.parseInt(idPerson);

        req.setAttribute("id", id);

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/site", "postgres", "root");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM information WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            ArrayList<Information> userList = new ArrayList<>();
            while (resultSet.next()) {
                Information information = new Information();
                information.setId(resultSet.getLong("id"));
                information.setName(resultSet.getString("person_name"));
                information.setLastname(resultSet.getString("person_lastname"));
                information.setAge(resultSet.getInt("person_age"));
                information.setLogin(resultSet.getString("person_login"));
                userList.add(information);
            }
            if (userList.isEmpty()) {
                System.out.println("Такого id нету");
                printWriter.println("Такого id нету");
            } else {
                System.out.println(userList);
                printWriter.println(userList);
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            req.getRequestDispatcher("/failed.jsp").forward(req, resp);
        } catch (SQLException e) {
            System.out.println(e);
            req.getRequestDispatcher("/failed.jsp").forward(req, resp);
        } finally {
            try {
                connection.close();
                printWriter.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        req.getRequestDispatcher("/success.jsp").forward(req, resp);
    }
}
