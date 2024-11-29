import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BakerDashboard {
    private JFrame frame;
    private DefaultTableModel ingredientTableModel;
    private DefaultTableModel recipeTableModel;

    public BakerDashboard() {
        frame = new JFrame("Wee Ken Shin Bakers - Baker Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Baker Dashboard");
        titleLabel.setBounds(300, 20, 200, 30);
        frame.add(titleLabel);

        // Ingredient Table
        ingredientTableModel = new DefaultTableModel(new String[]{"Ingredient ID", "Name", "Quantity", "Status"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        JScrollPane ingredientScrollPane = new JScrollPane(ingredientTable);
        ingredientScrollPane.setBounds(50, 70, 700, 200);
        frame.add(ingredientScrollPane);

        JButton updateQuantityButton = new JButton("Update Quantity");
        updateQuantityButton.setBounds(50, 300, 150, 30);
        frame.add(updateQuantityButton);

        JButton viewRecipesButton = new JButton("View Recipes");
        viewRecipesButton.setBounds(250, 300, 150, 30);
        frame.add(viewRecipesButton);

        JButton removeIngredientButton = new JButton("Remove Ingredient");
        removeIngredientButton.setBounds(450, 300, 150, 30);
        frame.add(removeIngredientButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(650, 300, 150, 30);
        frame.add(logoutButton);

        // Recipe Table
        JPanel recipeManagementPanel = new JPanel();
        recipeManagementPanel.setBounds(50, 350, 700, 200);
        recipeTableModel = new DefaultTableModel(new String[]{"Recipe ID", "Recipe Name", "Ingredients", "Action"}, 0);
        JTable recipeTable = new JTable(recipeTableModel);
        JScrollPane recipeScrollPane = new JScrollPane(recipeTable);
        recipeScrollPane.setBounds(50, 350, 700, 200);
        frame.add(recipeScrollPane);

        JButton addRecipeButton = new JButton("Add Recipe");
        addRecipeButton.setBounds(50, 560, 150, 30);
        frame.add(addRecipeButton);

        JButton editRecipeButton = new JButton("Edit Recipe");
        editRecipeButton.setBounds(250, 560, 150, 30);
        frame.add(editRecipeButton);

        JButton deleteRecipeButton = new JButton("Delete Recipe");
        deleteRecipeButton.setBounds(450, 560, 150, 30);
        frame.add(deleteRecipeButton);

        loadIngredients();
        loadRecipes();

        // Update Quantity Action
        updateQuantityButton.addActionListener(e -> updateIngredientQuantity(ingredientTable));

        // View Recipes Action
        viewRecipesButton.addActionListener(e -> viewRecipeDetails(recipeTable));

        // Remove Ingredient Action
        removeIngredientButton.addActionListener(e -> removeIngredient(ingredientTable));

        // Add Recipe Action
        addRecipeButton.addActionListener(e -> addRecipe());

        // Edit Recipe Action
        editRecipeButton.addActionListener(e -> editRecipe(recipeTable));

        // Delete Recipe Action
        deleteRecipeButton.addActionListener(e -> deleteRecipe(recipeTable));

        // Logout Action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new BakerySystemGUI(); 
        });

        frame.setVisible(true);
    }

    // Load ingredients into the table
    private void loadIngredients() {
        ingredientTableModel.setRowCount(0); // Clear table data
        String sql = "SELECT ingredientID, name, quantity, status FROM Ingredients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int ingredientID = rs.getInt("ingredientID");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");
                ingredientTableModel.addRow(new Object[]{ingredientID, name, quantity, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading ingredients.");
        }
    }

    // Load recipes into the table
    private void loadRecipes() {
        recipeTableModel.setRowCount(0); // Clear table data
        String sql = "SELECT recipeID, name, ingredients FROM Recipes";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int recipeID = rs.getInt("recipeID");
                String recipeName = rs.getString("name");
                String ingredients = rs.getString("ingredients");
                recipeTableModel.addRow(new Object[]{recipeID, recipeName, ingredients, "View"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading recipes.");
        }
    }

    // View the selected recipe details
    private void viewRecipeDetails(JTable recipeTable) {
        int selectedRow = recipeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a recipe to view.");
            return;
        }

        int recipeID = (int) recipeTableModel.getValueAt(selectedRow, 0);
        String recipeName = (String) recipeTableModel.getValueAt(selectedRow, 1);
        String ingredients = (String) recipeTableModel.getValueAt(selectedRow, 2);

        // Create a new dialog to display the recipe details
        JTextArea recipeDetailsArea = new JTextArea();
        recipeDetailsArea.setEditable(false);
        recipeDetailsArea.append("Recipe Name: " + recipeName + "\n\n");
        recipeDetailsArea.append("Ingredients:\n" + ingredients + "\n");

        // Displaying recipe details in a new dialog
        JOptionPane.showMessageDialog(frame, new JScrollPane(recipeDetailsArea), "Recipe Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Add a new recipe
    private void addRecipe() {
        // Ask for the recipe name
        String recipeName = JOptionPane.showInputDialog("Enter Recipe Name:");
        if (recipeName != null && !recipeName.trim().isEmpty()) {
            // Ask for the ingredients
            String ingredients = JOptionPane.showInputDialog("Enter Ingredients:");
            if (ingredients != null && !ingredients.trim().isEmpty()) {
                // Insert the new recipe into the database
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO Recipes (name, ingredients) VALUES (?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, recipeName);
                        pstmt.setString(2, ingredients);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Recipe added successfully!");
                            loadRecipes(); // Reload recipes after adding
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error adding recipe.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Ingredients cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Recipe Name cannot be empty.");
        }
    }

    // Update ingredient quantity
    private void updateIngredientQuantity(JTable ingredientTable) {
        int selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to update.");
            return;
        }

        int ingredientID = Integer.parseInt(ingredientTableModel.getValueAt(selectedRow, 0).toString());
        String ingredientName = ingredientTableModel.getValueAt(selectedRow, 1).toString();
        int currentQuantity = Integer.parseInt(ingredientTableModel.getValueAt(selectedRow, 2).toString());

        String newQuantityStr = JOptionPane.showInputDialog(frame, "Enter new quantity for " + ingredientName + ":", currentQuantity);
        if (newQuantityStr != null) {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                updateIngredientStatus(ingredientID, newQuantity);
                loadIngredients(); // Refresh the ingredients table
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity entered. Please enter a valid number.");
            }
        }
    }

    // Update the status based on the ingredient's new quantity
    private void updateIngredientStatus(int ingredientID, int newQuantity) {
        String status = "Available";
        if (newQuantity == 0) {
            status = "Finished";
        } else if (newQuantity <= 10) {
            status = "Low";
        }

        // Update the database with the new quantity and status
        String updateSQL = "UPDATE Ingredients SET quantity = ?, status = ? WHERE ingredientID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setString(2, status);
            pstmt.setInt(3, ingredientID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Ingredient updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update ingredient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating ingredient.");
        }
    }

    // Remove selected ingredient from the table and database
    private void removeIngredient(JTable ingredientTable) {
        int selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to remove.");
            return;
        }

        int ingredientID = (int) ingredientTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this ingredient?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteSQL = "DELETE FROM Ingredients WHERE ingredientID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                    pstmt.setInt(1, ingredientID);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Ingredient removed successfully.");
                        loadIngredients(); // Refresh ingredients table
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error removing ingredient.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error removing ingredient.");
            }
        }
    }

    
    // Edit a recipe
    private void editRecipe(JTable recipeTable) {
        int selectedRow = recipeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a recipe to edit.");
            return;
        }

        int recipeID = (int) recipeTableModel.getValueAt(selectedRow, 0);
        String currentName = (String) recipeTableModel.getValueAt(selectedRow, 1);
        String currentIngredients = (String) recipeTableModel.getValueAt(selectedRow, 2);

        String newRecipeName = JOptionPane.showInputDialog("Edit Recipe Name:", currentName);
        String newIngredients = JOptionPane.showInputDialog("Edit Ingredients:", currentIngredients);

        if (newRecipeName != null && !newRecipeName.trim().isEmpty() && newIngredients != null && !newIngredients.trim().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE Recipes SET name = ?, ingredients = ? WHERE recipeID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, newRecipeName);
                    pstmt.setString(2, newIngredients);
                    pstmt.setInt(3, recipeID);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Recipe updated successfully!");
                        loadRecipes(); // Reload recipes after editing
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating recipe.");
            }
        }
    }

    // Delete a recipe
    private void deleteRecipe(JTable recipeTable) {
        int selectedRow = recipeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a recipe to delete.");
            return;
        }

        int recipeID = (int) recipeTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this recipe?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteSQL = "DELETE FROM Recipes WHERE recipeID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                    pstmt.setInt(1, recipeID);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Recipe deleted successfully.");
                        loadRecipes(); // Reload recipes after deletion
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting recipe.");
            }
        }
    }

    public static void main(String[] args) {
        new BakerDashboard();
    }
}
