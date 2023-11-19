<%-- 
    Document   : filteremployee
    Created on : 11 18, 23, 1:02:00 AM
    Author     : ccslearner
--%>

<%@ page import="java.util.List, employeemg.employee" %>
<%@ page import="java.io.*, java.sql.*" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search and Filter Employee Information</title>
</head>
<body>
    <h1>Search and Filter Employee Information</h1>

    <% 
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String position = request.getParameter("position");

        List<employee> searchResults = employeemg.employee.searchEmployees(firstName, lastName, position);

        if (searchResults.isEmpty()) {
    %>
            <p>No results found.</p>
    <%
        } else {
    %>
            <table border="1">
                <tr>
                    <th>Employee ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Gender</th>
                    <th>Position</th>
                    <th>Mobile Number</th>
                </tr>

                <% for (employee emp : searchResults) { %>
                    <tr>
                        <td><%= emp.employeeId %></td>
                        <td><%= emp.firstName %></td>
                        <td><%= emp.lastName %></td>
                        <td><%= emp.gender %></td>
                        <td><%= emp.position %></td>
                        <td><%= emp.mobileNo %></td>
                    </tr>
                <% } %>
            </table>
    <%
        }
    %>

    <p><a href="filterinfo.html">Back to Search</a></p>
</body>
</html>

