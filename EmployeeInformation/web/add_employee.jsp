<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, data_management.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Employee Information</title>
</head>
<body>
    <form action="add_employee.jsp" method="post">
        <jsp:useBean id="E" class="data_management.employee" scope="session"/>
        <% 
            // Receive the values from addemployee.html
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
            Date birthday = dateFormat.parse(v_birthday);

            // Check if required fields are provided
            if (v_first_name != null && !v_first_name.isEmpty() &&
                v_last_name != null && !v_last_name.isEmpty() &&
                v_gender != null && !v_gender.isEmpty() &&
                v_birthday != null && !v_birthday.isEmpty() &&  
                v_age != null && !v_age.isEmpty() &&  
                v_position != null && !v_position.isEmpty() &&
                v_salary != null && !v_salary.isEmpty() &&
                v_mobile_no != null && !v_mobile_no.isEmpty() &&
                v_vendor_id != null && !v_vendor_id.isEmpty()) {

                try {
                    // Check if the vendor_id exists in the vendor table
                    if (vendorExists(Integer.parseInt(v_vendor_id))) {
                        // Instantiate an object of the employee class
                        data_management.employee employeeObj = new data_management.employee();

                        // Set values in the JavaBean
                        employeeObj.firstName = v_first_name;
                        employeeObj.lastName = v_last_name;
                        employeeObj.gender = v_gender;
                        employeeObj.birthday = birthday;
                        employeeObj.age = Integer.parseInt(v_age);
                        employeeObj.position = v_position;
                        employeeObj.salary = Double.parseDouble(v_salary);
                        employeeObj.mobileNo = Integer.parseInt(v_mobile_no);
                        employeeObj.vendorId = Integer.parseInt(v_vendor_id);
                        

                        // Call the addEmployee method
                        boolean res = employeeObj.addEmployee(employeeObj.firstName, employeeObj.lastName, employeeObj.gender, employeeObj.birthday, employeeObj.age, employeeObj.position, Double.parseDouble(v_salary), employeeObj.mobileNo, employeeObj.vendorId);
        %>
                        <h1><%= res ? "Adding Employee Successful!" : "Adding Employee Failed." %></h1>
        <%
                    } else {
        %>
                        <h1>Adding Employee Failed. Vendor with ID <%= v_vendor_id %> does not exist.</h1>
        <%
                    }
                } catch (NumberFormatException e) {
        %>
                    <h1>Adding Employee Failed. Invalid number format for mobile number, vendor ID, or salary.</h1>
        <%
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
        %> 
                <h1>Adding Employee Failed. All fields are required.</h1>
        <%
            }
        %>  
        <button type="button" onclick="window.location.href='employeeinfo.html'">Return to Employee Information Menu</button>
    </form>

    <%!
       public boolean vendorExists(int vendorId) {
    try (
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false")
    ) {
        // Check if the vendor with the given ID exists
        try (PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM vendor WHERE vendor_id = ?")) {
            checkStmt.setInt(1, vendorId);
            ResultSet checkResult = checkStmt.executeQuery();

            return checkResult.next(); // Return true if the vendor exists, false otherwise
        }
    } catch (SQLException e) {
        // Handle SQLException
        e.printStackTrace(); // Log or handle the exception appropriately
        return false;
    } catch (Exception e) {
        // Handle other exceptions
        e.printStackTrace(); 
        return false;
    }
}

    %>
</body>
</html>
