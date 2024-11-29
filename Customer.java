import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    public void browseProducts() {
        String sql = "SELECT * FROM Products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("productID") +
                                   ", Name: " + rs.getString("name") +
                                   ", Price: " + rs.getDouble("price") +
                                   ", Stock: " + rs.getInt("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFeedback(int productID, int customerID, String feedback) {
        String sql = "INSERT INTO Feedback (productID, customerID, feedback) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.setInt(2, customerID);
            pstmt.setString(3, feedback);
            pstmt.executeUpdate();
            System.out.println("Feedback submitted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
