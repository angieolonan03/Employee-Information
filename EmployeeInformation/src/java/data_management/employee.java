

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

        if (vendorId != null) {
            // If vendorId is not null, add the condition to filter by vendor_id
            query.append(" AND vendor_id = ?");
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

            if (vendorId != null) {
                stmt.setInt(parameterIndex++, vendorId);
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


}
