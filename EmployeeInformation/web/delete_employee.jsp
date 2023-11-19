<%-- 
    Document   : deleteenployee
    Created on : 11 18, 23, 12:28:10 AM
    Author     : ccslearner
--%>

    <%@ page contentType="text/html" pageEncoding="UTF-8"%>
        <%@ page import="java.util.*, java.sql.*, data_management.*" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Delete Employee Information</title>
            </head>

            <body>
                <form action="delete_employee.jsp" method="post">
                    <jsp:useBean id="E" class="data_management.employee" scope="session" />
                    <% 
            // Receive the values from the form
            String v_employee_id = request.getParameter("employee_id");
            
            // Check if employee ID is provided
            if (v_employee_id != null && !v_employee_id.isEmpty()) {
                try {
                    // Set employee ID in the JavaBean
                    E.employeeId = Integer.parseInt(v_employee_id);

                    // Create a connection
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false");

                    // Check if the employee exists
                    if (E.employeeExists(conn)) {
                        // Call the deleteEmployee method
                        boolean res = E.deleteEmployee();

                        // Close the connection
                        conn.close();
        %>
                        <h1>
                            <%= res ? "Deleting Employee Successful!" : "Deleting Employee Failed." %>
                        </h1>
                        <%
                    } else {
        %>
                            <h1>Deleting Employee Failed. Employee ID does not exist.</h1>
                            <%
                    }
                } catch (NumberFormatException e) {
        %>
                                <h1>Deleting Employee Failed. Invalid number format for employee ID.</h1>
                                <%
                } catch (SQLException e) {
        %>
                                    <h1>Deleting Employee Failed. SQL Exception.</h1>
                                    <%
                }
            } else {
        %>
                                        <h1>Deleting Employee Failed. Employee ID is required.</h1>
                                        <%
            }
        %>
                                            <button type="button" onclick="window.location.href='employeeinfo.html'">Return to Employee Information Menu</button>
                </form>
            </body>

            </html>