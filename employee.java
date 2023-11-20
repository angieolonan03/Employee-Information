package data_management;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ccslearner
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class employee {
    public int employeeId;
    public String firstName;
    public String lastName;
    public String gender;
    public Date birthday;
    public int age;
    public String position;
    public double salary;
    public int mobileNo;
    public int vendorId;

    public ArrayList<String> employeeNameList = new ArrayList<>();
    public ArrayList<String> employeeIdList = new ArrayList<>();

    public employee() {}
    
    public boolean addEmployee() {
        try (
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false")
        ) {
            System.out.println("Connection Successful!");

            // Check if the vendor with the given ID exists
            if (!vendorExists(vendorId, conn)) {
                System.out.println("Vendor with ID " + vendorId + " does not exist. Employee Add Failed.");
                return false;
            }
            
            // Generate a new employee ID
            int newEmployeeId = generateNewEmployeeId(conn);

            // Insert new employee with the generated employee ID
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee(employee_id, first_name, last_name, gender, birthday, age, position, salary, mobile_no, vendor_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, newEmployeeId);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, gender);
                pstmt.setDate(5, new java.sql.Date(birthday.getTime()));
                pstmt.setInt(6, age);
                pstmt.setString(7, position);
                pstmt.setDouble(8, salary);
                pstmt.setInt(9, mobileNo);
                pstmt.setInt(10, vendorId);
                pstmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int generateNewEmployeeId(Connection conn) throws SQLException {
        int newEmployeeId = 0;

        try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(employee_id), 0) + 1 AS ID FROM employee")) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                newEmployeeId = rs.getInt("ID");
            }
        }
        return newEmployeeId;
    }

    private boolean vendorExists(int vendorId, Connection conn) throws SQLException {
        try (PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM vendor WHERE vendor_id = ?")) {
            checkStmt.setInt(1, vendorId);
            ResultSet checkResult = checkStmt.executeQuery();
            return checkResult.next();
        }
    }

    private boolean isEmployeeIdExists(int employeeId, Connection conn) throws SQLException {
        try (PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM employee WHERE employee_id = ?")) {
            checkStmt.setInt(1, employeeId);
            ResultSet checkResult = checkStmt.executeQuery();
            return checkResult.next();
        }
    }

    public boolean updateEmployee() {
try {
    // Connect to the database
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false");
    System.out.println("Connection Successful!");

    // Check if employee exists
    PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM employee WHERE employee_id = ?");
    checkStmt.setInt(1, employeeId);
    ResultSet checkResult = checkStmt.executeQuery();

    if (!checkResult.next()) {
        // Employee with the specified ID does not exist
        System.out.println("Employee with ID " + employeeId + " does not exist.");
        return false;
    }

    // Update employee information
    PreparedStatement updateStmt = conn.prepareStatement("UPDATE employee SET first_name = ?, last_name = ?, gender = ?, birthday = ?, age = ?, position = ?, salary = ?, mobile_no = ?, vendor_id = ? WHERE employee_id = ?");
    updateStmt.setString(1, firstName);
    updateStmt.setString(2, lastName);
    updateStmt.setString(3, gender);
    // Convert java.util.Date to java.sql.Date
    java.sql.Date sqlDate = new java.sql.Date(birthday.getTime());
    updateStmt.setDate(4, sqlDate);
    updateStmt.setInt(5, age);
    updateStmt.setString(6, position);
    updateStmt.setDouble(7, salary);
    updateStmt.setInt(8, mobileNo);
    updateStmt.setInt(9, vendorId);
    
    updateStmt.setInt(10, employeeId);

    int rowsAffected = updateStmt.executeUpdate();
    System.out.println("Rows affected: " + rowsAffected);

    checkStmt.close();
    updateStmt.close();
    conn.close();

    return true;
} catch (Exception e) {
    e.printStackTrace();
}
return false;

}

    public boolean deleteEmployee() {
        try (
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false")
        ) {
            // Check if employee exists
            if (employeeExists(conn)) {
                // Delete employee
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM employee WHERE employee_id = ?")) {
                    deleteStmt.setInt(1, employeeId);
                    deleteStmt.executeUpdate();
                }

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace(); // Log or handle the exception appropriately
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Log or handle the exception appropriately
            return false;
        }
    }

    public boolean employeeExists(Connection conn) {
        try (
            // No need to create a new connection here, use the provided connection
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM employee WHERE employee_id = ?")
        ) {
            checkStmt.setInt(1, employeeId);
            ResultSet checkResult = checkStmt.executeQuery();

            return checkResult.next();
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace(); // Log or handle the exception appropriately
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Log or handle the exception appropriately
            return false;
        }
    }

    public List<employee> searchEmployees(String firstName, String lastName, String gender, Date birthday, Integer age, String position, Double salary) {
        List<employee> searchResults = new ArrayList<>();

        try (
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false")
        ) {
            // Build the SQL query based on search parameters
            StringBuilder query = new StringBuilder("SELECT * FROM employee WHERE 1=1");

            if (firstName != null && !firstName.isEmpty()) {
                query.append(" AND first_name LIKE ?");
            }

            if (lastName != null && !lastName.isEmpty()) {
                query.append(" AND last_name LIKE ?");
            }

            if (gender != null && !gender.isEmpty()) {
                query.append(" AND gender = ?");
            }

            if (birthday != null) {
                query.append(" AND birthday = ?");
            }

            if (age != null) {
                query.append(" AND age = ?");
            }

            if (position != null && !position.isEmpty()) {
                query.append(" AND position LIKE ?");
            }

            if (salary != null) {
                // If salary is not null, add the condition to filter by salary
                query.append(" AND salary = ?");
            } else {
                // If salary is null, add a condition to exclude null values
                query.append(" AND salary IS NOT NULL");
            }

            try (
               // Prepare and execute the SQL statement
                PreparedStatement stmt = conn.prepareStatement(query.toString())
            ) {
                int parameterIndex = 1;

                if (firstName != null && !firstName.isEmpty()) {
                    stmt.setString(parameterIndex++, "%" + firstName + "%");
                }

                if (lastName != null && !lastName.isEmpty()) {
                    stmt.setString(parameterIndex++, "%" + lastName + "%");
                }

                if (gender != null && !gender.isEmpty()) {
                    stmt.setString(parameterIndex++, gender);
                }

                if (birthday != null) {
                    stmt.setDate(parameterIndex++, new java.sql.Date(birthday.getTime()));
                }

                if (age != null) {
                    stmt.setInt(parameterIndex++, age);
                }

                if (position != null && !position.isEmpty()) {
                    stmt.setString(parameterIndex++, "%" + position + "%");
                }

                if (salary != null) {
                    stmt.setDouble(parameterIndex++, salary);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    // Process the query results and populate the searchResults list
                    while (rs.next()) {
                        employee emp = new employee();
                        emp.employeeId = rs.getInt("employee_id");
                        emp.firstName = rs.getString("first_name");
                        emp.lastName = rs.getString("last_name");
                        emp.gender = rs.getString("gender");
                        emp.birthday = rs.getDate("birthday");
                        emp.age = rs.getInt("age");
                        emp.position = rs.getString("position");
                        emp.salary = rs.getDouble("salary");
                        emp.mobileNo = rs.getInt("mobile_no");
                        emp.vendorId = rs.getInt("vendor_id");

                        searchResults.add(emp);
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }

        return searchResults;
    }
    
    public static void main(String args[]){
        /*employee e = new employee();
        
        e.firstName = "Kimi";
        e.lastName = "Valdez";
        e.gender= "Female";
        e.age= 19;
        e.position = "Manager";
        e.birthday = new Date();
        e.salary = 120000.00;;
        e.mobileNo = 93912479;
        e.vendorId= 9001;
        
        e.addEmployee();*/
    }
    
}

