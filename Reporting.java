import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reporting {
    public void generateSalesReport() {
        String sql = "SELECT productID, SUM(quantity) AS totalSold FROM Orders WHERE status = 'Completed' GROUP BY productID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("productID");
                int totalSold = rs.getInt("totalSold");

                System.out.println("Product ID: " + productId + ", Total Sold: " + totalSold);
            }

        } catch (SQLException e) {
            System.out.println("Error generating sales report.");
            e.printStackTrace();
        }
    }
}
