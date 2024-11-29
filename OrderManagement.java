import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderManagement {
    public void placeOrder(int customerID, int productID, int quantity) {
        String sql = "INSERT INTO Orders (customerID, productID, quantity, status) VALUES (?, ?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerID); // Bind customerID
            pstmt.setInt(2, productID); // Bind productID
            pstmt.setInt(3, quantity);  // Bind quantity

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order placed successfully.");
            } else {
                System.out.println("Order placement failed.");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while placing the order.");
            e.printStackTrace(); // Print detailed error information
        }
    }
}
