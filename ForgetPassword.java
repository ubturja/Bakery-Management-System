

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgetPassword {
    
    // Method to retrieve security question based on email
    public String getSecurityQuestion(String email) {
        // SQL query to fetch the security question based on the email
        String sql = "SELECT securityQuestion FROM Users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the email parameter for the prepared statement
            pstmt.setString(1, email);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Check if the user with the provided email exists
            if (rs.next()) {
                // Return the security question
                return rs.getString("securityQuestion");
            } else {
                return null; // Return null if no user is found with the provided email
            }
        } catch (SQLException e) {
            System.out.println("Error during authentication process.");
            e.printStackTrace();
            return null;
        }
    }
    
    
    public boolean verifySecurityAnswer(String email, String securityAnswer) {
        // SQL query to fetch the password based on the email and security answer
        
        String sql = "SELECT password FROM Users WHERE email = ? AND securityAnswer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the email and security answer parameters for the prepared statement
            pstmt.setString(1, email);
            pstmt.setString(2, securityAnswer);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                // If the result set contains a row, the email and answer match
                return rs.next();  // Returns true if there's a match, otherwise false
            }
        } catch (SQLException e) {
            // Log the error message for debugging purposes
            System.out.println("Error during security answer verification process.");
            e.printStackTrace();
            return false;  // Return false if there was an error
        }
    }
    
    public String changePassword(String email, String securityAnswer, String newPassword, String confirmPassword) {
        // Check if new password matches confirm password
        if (!newPassword.equals(confirmPassword)) {
            return "donotmatch"; // Return if passwords don't match
        }
        
        else
        {
           // SQL query to fetch the current password based on email and security answer
            String sql = "SELECT password FROM Users WHERE email = ? AND securityAnswer = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set the parameters for the prepared statement
                pstmt.setString(1, email);
                pstmt.setString(2, securityAnswer);

                // Execute the query
                try (ResultSet rs = pstmt.executeQuery()) {

                    // If user exists and the security answer matches, check if the new password is the same as the old one
                    if (rs.next()) {
                        String currentPassword = rs.getString("password");

                        if (newPassword.equals(currentPassword)) {
                            return "same"; // Return if the new password is the same as the old one
                        } else {                      

                            // If new password is different, update it
                            String updateSql = "UPDATE Users SET password = ? WHERE email = ? AND securityAnswer = ?";
                            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                                updatePstmt.setString(1, newPassword);
                                updatePstmt.setString(2, email);
                                updatePstmt.setString(3, securityAnswer);

                                // Execute the update query
                                int rowsUpdated = updatePstmt.executeUpdate();

                                if (rowsUpdated > 0) {
                                    return "passwordChanged"; // Return success message
                                } else {
                                    return "updateFailed"; // No rows updated, indicating some issue
                                }
                            }
                        }
                    } else {
                        return "invalidSecurityAnswer"; // Return if no user found with provided email and security answer
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error during password change process.");
                e.printStackTrace();
                return "errorOccurred"; // Return null in case of an error
            } 
        }

        
    }
        
}