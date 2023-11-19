<%-- 
    Document   : updateemployee.jsp
    Created on : 11 14, 23, 11:08:00 PM
    Author     : ccslearner
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, data_management.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>

<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Employee Information</title>
</head>

<body>
<form action="updateemployee.jsp" method="post">
<jsp:useBean id="E" class="data_management.employee" scope="session" />
                                <%
        String errorMessage = "";
        boolean res = false;

        try {
            // Receive the values from updateinfo.html 
            String v_employee_id = request.getParameter("employee_id");
            String v_first_name = request.getParameter("first_name");
            String v_last_name = request.getParameter("last_name");
            String v_gender = request.getParameter("gender");
            String v_birthday = request.getParameter("birthday");
            String v_age = request.getParameter("age");
            String v_position = request.getParameter("position");
            String v_salary = request.getParameter("salary");
            String v_mobile_no = request.getParameter("mobile_no");
            String v_vendor_id = request.getParameter("vendor_id");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            Date birthday = dateFormat.parse(v_birthday);

            // Check if required fields are provided
            if (v_employee_id != null && !v_employee_id.isEmpty() &&
                v_first_name != null && !v_first_name.isEmpty() &&
                v_last_name != null && !v_last_name.isEmpty() &&
                v_gender != null && !v_gender.isEmpty() &&
                v_birthday != null && !v_birthday.isEmpty() &&
                v_age != null && !v_age.isEmpty() &&
                v_position != null && !v_position.isEmpty() &&
                v_salary != null && !v_salary.isEmpty() &&
                v_mobile_no != null && !v_mobile_no.isEmpty() && 
                v_vendor_id != null && !v_vendor_id.isEmpty()) {

                // Instantiate an object of the employee class
                data_management.employee employeeObj = new data_management.employee();

                // Set values in the JavaBean
                employeeObj.employeeId = Integer.parseInt(v_employee_id);
                employeeObj.firstName = v_first_name;
                employeeObj.lastName = v_last_name;
                employeeObj.gender = v_gender;
                employeeObj.birthday = birthday;
                employeeObj.age = Integer.parseInt(v_age);
                employeeObj.position = v_position;
                employeeObj.salary = Double.parseDouble(v_salary);
                employeeObj.mobileNo = Integer.parseInt(v_mobile_no);
                employeeObj.vendorId = Integer.parseInt(v_vendor_id);

                // Call the updateEmployee method
                System.out.println("Before updateEmployee");
                res = employeeObj.updateEmployee();
                System.out.println("After updateEmployee");
            } else {
                errorMessage = "Updating Employee Failed. All fields are required.";
            }
        } catch (NumberFormatException | ParseException e) {
    e.printStackTrace();
    errorMessage = "An error occurred while updating the employee.";
}

        %>

    <h1>
    <%= res ? "Updating Employee Successful!" : errorMessage %>
    </h1>
    <button type="button" onclick="window.location.href='employeeinfo.html'">Return to Employee Information Menu</button>
    </form>
    </body>

    </html>
