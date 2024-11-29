import java.sql.*;

public class Registration {
    // Method to register a new user
    public String registerUser(String firstName, String lastName, String email, String password, String confirmPassword, String securityQuestion, String securityAnswer) {
        // Check if passwords match
        String role = "Customer";
        if (!password.equals(confirmPassword)) {
            return "Same";
        }

        String userInsertQuery = "INSERT INTO Users (firstName, lastName, email, password, status, securityQuestion, securityAnswer) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(userInsertQuery, Statement.RETURN_GENERATED_KEYS)) {

            userStmt.setString(1, firstName);
            userStmt.setString(2, lastName);
            userStmt.setString(3, email);
            userStmt.setString(4, password); // In production, hash the password before saving
            userStmt.setString(5, role);
            userStmt.setString(6, securityQuestion);
            userStmt.setString(7, securityAnswer);

            int rowsAffected = userStmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert the user's role-specific data into the corresponding table
                    String roleInsertQuery = "";
                    if (role.equalsIgnoreCase("Customer")) {
                        roleInsertQuery = "INSERT INTO Customers (userID) VALUES (?)";
                    } else if (role.equalsIgnoreCase("Cashier")) {
                        roleInsertQuery = "INSERT INTO Cashiers (userID) VALUES (?)";
                    } else if (role.equalsIgnoreCase("Manager")) {
                        roleInsertQuery = "INSERT INTO Managers (userID) VALUES (?)";
                    } else if (role.equalsIgnoreCase("Baker")) {
                        roleInsertQuery = "INSERT INTO Bakers (userID) VALUES (?)";
                    }

                    try (PreparedStatement roleStmt = conn.prepareStatement(roleInsertQuery)) {
                        roleStmt.setInt(1, userId);
                        roleStmt.executeUpdate();
                    }

                    return "Success"; // Registration successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Fail"; // Registration failed
    }

    // Method to retrieve a user ID by email
    public int getUserIdByEmail(String email) {
        String query = "SELECT userID FROM Users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the user is not found
    }
}
