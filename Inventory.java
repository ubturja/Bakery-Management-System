import java.sql.*;

public class Inventory {
    public void markAsFinished(int ingredientID) {
        String sql = "UPDATE Ingredients SET status = 'Finished' WHERE ingredientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientID);
            pstmt.executeUpdate();
            System.out.println("Ingredient marked as finished.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAsLow(int ingredientID) {
        String sql = "UPDATE Ingredients SET status = 'Low' WHERE ingredientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientID);
            pstmt.executeUpdate();
            System.out.println("Ingredient marked as low.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getIngredients() {
        String sql = "SELECT * FROM Ingredients";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
