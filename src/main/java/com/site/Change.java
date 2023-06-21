package com.site;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/change-login")
public class Change extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("<h1>Deleting ID:</h1>");

        String idPerson = req.getParameter("id");
        int id = Integer.parseInt(idPerson);
        String login = req.getParameter("login");

        req.setAttribute("id", id);
        req.setAttribute("login", login);

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/site", "postgres", "root");
            PreparedStatement statement = connection.prepareStatement("UPDATE information SET person_login=? WHERE id=?");
            statement.setString(1, login);
            statement.setInt(2, id);

            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet);
            req.getRequestDispatcher("/success.jsp").forward(req, resp);

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
    }
}
