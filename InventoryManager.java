import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryManager {

    // Updates the stock of a product based on its ID
    public void updateProductStock(int productID, int quantity) {
        // SQL query to decrement the stock based on the sold quantity
        String updateStockSQL = "UPDATE Products SET stock = stock - ? WHERE productID = ?";

        // Try-with-resources to automatically close the database connection and statement
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateStockSQL)) {

            // Set the parameters for the prepared statement
            pstmt.setInt(1, quantity); // Decrease the stock by this quantity
            pstmt.setInt(2, productID); // Product ID to be updated

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            // Print feedback for debugging purposes
            if (rowsAffected > 0) {
                System.out.println("Stock updated successfully for product ID: " + productID);
            } else {
                System.out.println("No product found with ID: " + productID);
            }

        } catch (SQLException e) {
            // Handle SQL exceptions and print the error stack trace
            e.printStackTrace();
        }
    }

    // Example usage (you can remove this main method for integration in your project)
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Test the updateProductStock method
        manager.updateProductStock(101, 2); // Reduce stock of productID 101 by 2
    }
}
