<%@ page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ page import="java.util.*, java.sql.*, data_management.*" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Add Employee Information</title>
        </head>

        <body>
            <form action="add_employee.jsp" method="post">
                <jsp:useBean id="A" class="data_management.employee" scope="session" />
                <% 
            // Receive the values from addemployee.html
            String v_employee_id = request.getParameter("employee_id");
            String v_first_name = request.getParameter("first_name");
            String v_last_name = request.getParameter("last_name");
            String v_gender = request.getParameter("gender");
            String v_position = request.getParameter("position");
            String v_mobile_no = request.getParameter("mobile_no");
            
            // Check if required fields are provided
            if (v_employee_id != null && !v_employee_id.isEmpty() &&
                v_first_name != null && !v_first_name.isEmpty() &&
                v_last_name != null && !v_last_name.isEmpty() &&
                v_gender != null && !v_gender.isEmpty() &&
                v_position != null && !v_position.isEmpty() &&
                v_mobile_no != null && !v_mobile_no.isEmpty())
            {

                try {
                    // Set values in the JavaBean
                    A.employeeId = Integer.parseInt(v_employee_id);
                    A.firstName = v_first_name;
                    A.lastName = v_last_name;
                    A.gender = v_gender;
                    A.position = v_position;
                    A.mobileNo = Integer.parseInt(v_mobile_no);

                    // Call the addEmployee method
                    boolean res = A.addEmployee(A.employeeId, A.firstName, A.lastName, A.gender, A.position, A.mobileNo);
        %>
                    <h1>
                        <%= res ? "Adding Employee Successful!" : "Adding Employee Failed." %>
                    </h1>
                    <%
                } catch (NumberFormatException e) {
        %>
                        <h1>Adding Employee Failed. Invalid number format for employee ID, mobile number, or vendor ID.</h1>
                        <%
                }
            } else {
        %>
                            <h1>Adding Employee Failed. All fields are required.</h1>
                            <%
            }
        %>
                                <button type="button" onclick="window.location.href='employeeinfo.html'">Return to Employee Information Menu</button>
            </form>
        </body>

        </html>