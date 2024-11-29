import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cashier {
    public void addDiscount(int productID, double discount) {
        String sql = "UPDATE Products SET price = price - ? WHERE productID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, discount);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();
            System.out.println("Discount applied successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void completeTransaction(int orderID) {
        String sql = "UPDATE Orders SET status = 'Completed' WHERE orderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderID);
            pstmt.executeUpdate();
            System.out.println("Transaction completed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
