package com.example.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Database connection parameters
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeedb";
    private static final String USER = "root";
    private static final String PASS = "password";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // Check if an employee ID was provided for search
            String employeeId = request.getParameter("empId");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Employee Management System</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1 { color: #333366; }");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
            out.println("form { margin-bottom: 20px; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }");
            out.println("input[type=text] { padding: 8px; width: 200px; }");
            out.println("input[type=submit] { padding: 8px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Employee Management System</h1>");
            
            // Search form
            out.println("<form action='employees' method='get'>");
            out.println("  <label for='empId'>Search by Employee ID: </label>");
            out.println("  <input type='text' id='empId' name='empId' placeholder='Enter Employee ID'>");
            out.println("  <input type='submit' value='Search'>");
            out.println("  <a href='employees' style='margin-left: 10px; text-decoration: none;'>Show All</a>");
            out.println("</form>");
            
            String sql;
            
            if (employeeId != null && !employeeId.trim().isEmpty()) {
                // Query to get specific employee by ID
                sql = "SELECT * FROM employees WHERE employee_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, employeeId);
                out.println("<h2>Employee Details for ID: " + employeeId + "</h2>");
            } else {
                // Query to get all employees
                sql = "SELECT * FROM employees";
                stmt = conn.prepareStatement(sql);
                out.println("<h2>All Employees</h2>");
            }
            
            rs = stmt.executeQuery();
            
            // Display employee table
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th><th>Salary</th></tr>");
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String id = rs.getString("employee_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + department + "</td>");
                out.println("<td>" + salary + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            
            if (!hasResults) {
                if (employeeId != null && !employeeId.trim().isEmpty()) {
                    out.println("<p>No employee found with ID: " + employeeId + "</p>");
                } else {
                    out.println("<p>No employees found in the database.</p>");
                }
            }
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (SQLException se) {
            out.println("<div style='color: red; padding: 10px; background-color: #ffe6e6; border-radius: 5px;'>");
            out.println("Database Error: " + se.getMessage());
            out.println("</div>");
            se.printStackTrace();
        } catch (Exception e) {
            out.println("<div style='color: red; padding: 10px; background-color: #ffe6e6; border-radius: 5px;'>");
            out.println("Error: " + e.getMessage());
            out.println("</div>");
            e.printStackTrace();
        } finally {
            // Clean-up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}