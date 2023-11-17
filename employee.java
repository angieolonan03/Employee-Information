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


public class employee {
    public static int employeeId;
    public static String firstName;
    public static String lastName;
    public static String gender;
    public static String position;
    public static int mobileNo;

    public ArrayList<String> employeeNameList = new ArrayList<String>();
    public ArrayList<String> employeeIdList = new ArrayList<String>();

    public employee() {}
    

    public static boolean addEmployee(int employeeId, String firstName, String lastName, String gender, String position, int mobileNo) {
        try (
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app?user=root&password=12345678&useTimezone=true&serverTimezone=UTC&useSSL=false")
        ) {
            System.out.println("Connection Successful!");
            
            // Check if the employee ID already exists
        if (isEmployeeIdExists(employeeId, conn)) {
            System.out.println("Employee with ID " + employeeId + " already exists.");
            return false;
        }

            // Insert new employee with the provided employee_id
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee(employee_id, first_name, last_name, gender, position, mobile_no, vendor_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, gender);
            pstmt.setString(5, position);
            pstmt.setInt(6, mobileNo);
            pstmt.setNull(7, Types.INTEGER);  // Vendor ID is not used in the provided code for employee registration
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    private static boolean isEmployeeIdExists(int employeeId, Connection conn) throws SQLException {
    PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM employee WHERE employee_id = ?");
    checkStmt.setInt(1, employeeId);
    ResultSet checkResult = checkStmt.executeQuery();

    return checkResult.next();
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
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE employee SET first_name = ?, last_name = ?, gender = ?, position = ?, mobile_no = ? WHERE employee_id = ?");
        updateStmt.setString(1, firstName);
        updateStmt.setString(2, lastName);
        updateStmt.setString(3, gender);
        updateStmt.setString(4, position);
        updateStmt.setInt(5, mobileNo);
        updateStmt.setInt(6, employeeId);

        updateStmt.executeUpdate();

        System.out.println("Employee Update Successfully");

        checkStmt.close();
        updateStmt.close();
        conn.close();

        return true;
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    System.out.println("Employee Update Failed");
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

    public static List<employee> searchEmployees(String firstName, String lastName, String position) {
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

            if (position != null && !position.isEmpty()) {
                query.append(" AND position LIKE ?");
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

                if (position != null && !position.isEmpty()) {
                    stmt.setString(parameterIndex++, "%" + position + "%");
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    // Process the query results and populate the searchResults list
                    while (rs.next()) {
                        employee emp = new employee();
                        emp.employeeId = rs.getInt("employee_id");
                        emp.firstName = rs.getString("first_name");
                        emp.lastName = rs.getString("last_name");
                        emp.gender = rs.getString("gender");
                        emp.position = rs.getString("position");
                        emp.mobileNo = rs.getInt("mobile_no");

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
}
