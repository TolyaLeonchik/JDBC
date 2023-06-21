package com.site;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/create")
public class Create extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("person_name");
        String lastname = req.getParameter("person_lastname");
        String login = req.getParameter("person_login");
        String ageString = req.getParameter("person_age");
        int age = Integer.parseInt(ageString);

        req.setAttribute("person_name", name);
        req.setAttribute("person_lastname", lastname);
        req.setAttribute("person_age", age);
        req.setAttribute("person_login", login);

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/site", "postgres", "root");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO information " +
                    "(id,person_name,person_lastname,person_age,person_login)\n" +
                    "VALUES (DEFAULT, ?, ?, ?, ?);");
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setInt(3, age);
            statement.setString(4, login);

            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet);

            req.getRequestDispatcher("/success.jsp").forward(req, resp);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        req.getRequestDispatcher("/success.jsp").forward(req, resp);
    }
}
