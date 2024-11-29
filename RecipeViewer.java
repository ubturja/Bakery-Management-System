import javax.swing.*;
import java.sql.*;

public class RecipeViewer {
    private JFrame frame;
    private JTextArea recipeArea;

    public RecipeViewer() {
        frame = new JFrame("Wee Ken Shin Bakers - Recipes");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Recipes");
        titleLabel.setBounds(300, 20, 200, 30);
        frame.add(titleLabel);

        recipeArea = new JTextArea();
        recipeArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recipeArea);
        scrollPane.setBounds(50, 70, 700, 400);
        frame.add(scrollPane);

        loadRecipes();

        frame.setVisible(true);
    }

    private void loadRecipes() {
        String sql = "SELECT productID, recipe FROM Recipes";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            recipeArea.setText(""); // Clear the text area before adding new data
            while (rs.next()) {
                int productID = rs.getInt("productID");
                String recipe = rs.getString("recipe");
                recipeArea.append("Product ID: " + productID + "\n");
                recipeArea.append("Recipe: " + (recipe != null ? recipe : "No recipe available") + "\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading recipes.");
        }
    }

    public static void main(String[] args) {
        new RecipeViewer();
    }
}
