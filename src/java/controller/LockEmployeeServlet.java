package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import repository.UserRepository;

import java.io.IOException;

@WebServlet(name = "LockEmployeeServlet", value = "/lockemployee")
public class LockEmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empID=request.getParameter("empID");

        UserRepository.lockEmployee(empID);
        response.sendRedirect("manage-emp-account");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
