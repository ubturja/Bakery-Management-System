import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesManager {

    // Method to add a sale to the database
    public void addSale(int cashierID, double saleAmount) {
        String sql = "INSERT INTO Sales (cashierID, saleAmount, saleDate) VALUES (?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cashierID); // Set the cashier ID
            pstmt.setDouble(2, saleAmount); // Set the sale amount
            pstmt.executeUpdate();
            System.out.println("Sale added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    // Method to view sales records
    public void viewSales() {
        String sql = "SELECT * FROM Sales";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Sales Records:");
            while (rs.next()) {
                int saleID = rs.getInt("saleID");
                int cashierID = rs.getInt("cashierID");
                double saleAmount = rs.getDouble("saleAmount");
                String saleDate = rs.getString("saleDate");

                System.out.printf("Sale ID: %d | Cashier ID: %d | Amount: %.2f | Date: %s%n",
                        saleID, cashierID, saleAmount, saleDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
