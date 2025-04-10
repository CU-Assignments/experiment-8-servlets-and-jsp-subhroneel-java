package com.studentportal.servlet;

import com.studentportal.dao.AttendanceDAO;
import com.studentportal.model.Attendance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AttendanceDAO attendanceDAO;
    
    public void init() {
        attendanceDAO = new AttendanceDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAttendance(request, response);
                break;
            default:
                listAttendance(request, response);
                break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "add";
        }
        
        switch (action) {
            case "add":
                addAttendance(request, response);
                break;
            case "update":
                updateAttendance(request, response);
                break;
            default:
                listAttendance(request, response);
                break;
        }
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/attendance.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        // Logic to get attendance record by ID and set it as request attribute
        // For simplicity, this is not implemented
        
        request.getRequestDispatcher("/attendance.jsp").forward(request, response);
    }
    
    private void addAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            String dateStr = request.getParameter("date");
            String subject = request.getParameter("subject");
            String status = request.getParameter("status");
            String remarks = request.getParameter("remarks");
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            
            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setDate(date);
            attendance.setSubject(subject);
            attendance.setStatus(status);
            attendance.setRemarks(remarks);
            
            boolean success = attendanceDAO.addAttendance(attendance);
            
            if (success) {
                request.setAttribute("message", "Attendance added successfully!");
            } else {
                request.setAttribute("message", "Failed to add attendance!");
            }
            
            listAttendance(request, response);
            
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid date format!");
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        }
    }
    
    private void updateAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            String dateStr = request.getParameter("date");
            String subject = request.getParameter("subject");
            String status = request.getParameter("status");
            String remarks = request.getParameter("remarks");
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            
            Attendance attendance = new Attendance();
            attendance.setId(id);
            attendance.setStudentId(studentId);
            attendance.setDate(date);
            attendance.setSubject(subject);
            attendance.setStatus(status);
            attendance.setRemarks(remarks);
            
            boolean success = attendanceDAO.updateAttendance(attendance);
            
            if (success) {
                request.setAttribute("message", "Attendance updated successfully!");
            } else {
                request.setAttribute("message", "Failed to update attendance!");
            }
            
            listAttendance(request, response);
            
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid date format!");
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        }
    }
    
    private void deleteAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        boolean success = attendanceDAO.deleteAttendance(id);
        
        if (success) {
            request.setAttribute("message", "Attendance deleted successfully!");
        } else {
            request.setAttribute("message", "Failed to delete attendance!");
        }
        
        listAttendance(request, response);
    }
    
    private void listAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int studentId = 0;
        try {
            studentId = Integer.parseInt(request.getParameter("studentId"));
        } catch (NumberFormatException e) {
            // If no studentId provided, redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
            return;
        }
        
        request.setAttribute("attendanceList", attendanceDAO.getAttendanceByStudent(studentId));
        request.getRequestDispatcher("/attendance.jsp").forward(request, response);
    }
}