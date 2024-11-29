import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManagerDashboard {
    private JFrame frame;
    private JTable userTable;
    private DefaultTableModel userModel;
    private JTable orderTable;
    private DefaultTableModel orderModel;
    private JTable feedbackTable;
    private DefaultTableModel feedbackModel;
    private JTable productTable;
    private DefaultTableModel productModel;
    private int managerId; 

    public ManagerDashboard(int managerId) {
        this.managerId = managerId;

        frame = new JFrame("Wee Ken Shin Bakers - Manager Dashboard");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, BorderLayout.CENTER);

        // User Management Tab
        JPanel userManagementPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("User Management", userManagementPanel);

        userModel = new DefaultTableModel(new String[]{"User ID", "Name", "Email", "Status"}, 0);
        userTable = new JTable(userModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        userManagementPanel.add(userScrollPane, BorderLayout.CENTER);

        JPanel userButtonPanel = new JPanel();
        JButton blockButton = new JButton("Block User");
        JButton unblockButton = new JButton("Unblock User");
        //JButton removeButton = new JButton("Remove User");
        JButton editButton = new JButton("Edit User");
        JButton logoutButton = new JButton("Logout");

        userButtonPanel.add(blockButton);
        userButtonPanel.add(unblockButton);
        //userButtonPanel.add(removeButton);
        userButtonPanel.add(editButton);
        userButtonPanel.add(logoutButton);
        userManagementPanel.add(userButtonPanel, BorderLayout.SOUTH);

        // Order Management Tab
        JPanel orderManagementPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Order Management", orderManagementPanel);

        orderModel = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Product Name", "Quantity", "Total Amount", "Order Status"}, 0);
        orderTable = new JTable(orderModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderManagementPanel.add(orderScrollPane, BorderLayout.CENTER);

        JPanel orderButtonPanel = new JPanel();
        JButton updateOrderStatusButton = new JButton("Update Order Status");
        orderButtonPanel.add(updateOrderStatusButton);
        orderManagementPanel.add(orderButtonPanel, BorderLayout.SOUTH);

        // Feedback Management Tab
        JPanel feedbackManagementPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Feedback Management", feedbackManagementPanel);

        feedbackModel = new DefaultTableModel(new String[]{"Customer Name", "Product Name", "Feedback", "Manager's Response", "Action"}, 0);
        feedbackTable = new JTable(feedbackModel);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTable);
        feedbackManagementPanel.add(feedbackScrollPane, BorderLayout.CENTER);

        JPanel feedbackButtonPanel = new JPanel();
        JButton replyButton = new JButton("Reply to Feedback");
        feedbackButtonPanel.add(replyButton);
        feedbackManagementPanel.add(feedbackButtonPanel, BorderLayout.SOUTH);
        
         // Product Management Tab
        JPanel productManagementPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Product Management", productManagementPanel);

        productModel = new DefaultTableModel(new String[]{"Product ID", "Product Name", "Price", "Stock"}, 0);
        productTable = new JTable(productModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productManagementPanel.add(productScrollPane, BorderLayout.CENTER);

        JPanel productButtonPanel = new JPanel();
        JButton addProductButton = new JButton("Add New Product");
        productButtonPanel.add(addProductButton);
        JButton removeProductButton = new JButton("Remove Product");
        productButtonPanel.add(removeProductButton);
        productManagementPanel.add(productButtonPanel, BorderLayout.SOUTH);


        loadUsers();
        loadOrders();
        loadFeedback();
        loadProducts();

        // Block User Action
        blockButton.addActionListener(e -> blockUser());

        // Unblock User Action
        unblockButton.addActionListener(e -> unblockUser());

        // Remove User Action
        //removeButton.addActionListener(e -> removeUser());

        // Edit User Action
        editButton.addActionListener(e -> editUser());

        // Logout Action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new Login(); // Redirect to login page
        });

        // Update Order Status Action
        updateOrderStatusButton.addActionListener(e -> updateOrderStatus());

        // Reply to Feedback Action
        replyButton.addActionListener(e -> replyToFeedback());

        frame.setVisible(true);
        
        // Add Product Action
        addProductButton.addActionListener(e -> addNewProduct());

        frame.setVisible(true);
        
        // Remove Product Action
        removeProductButton.addActionListener(e -> removeProduct());

        frame.setVisible(true);
    }

    // Load users into the table
    private void loadUsers() {
        userModel.setRowCount(0); // Clear table data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT u.userID, CONCAT(u.firstName, ' ', u.lastName) AS Name, u.email, u.status " +
                     "FROM users u")) {

            while (rs.next()) {
                int userID = rs.getInt("userID");
                String name = rs.getString("Name");
                String email = rs.getString("email");
                String status = rs.getString("status");

                userModel.addRow(new Object[]{userID, name, email, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading users.");
        }
    }

    // Load orders into the order management table
    private void loadOrders() {
        orderModel.setRowCount(0); // Clear table data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT o.orderID, CONCAT(c.firstName, ' ', c.lastName) AS customerName, p.name AS productName, o.quantity, (p.price * o.quantity) AS totalAmount, o.status " +
                     "FROM Orders o " +
                     "JOIN Customers c ON o.customerID = c.userID " +
                     "JOIN Products p ON o.productID = p.productID")) 
        {

            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                String customerName = rs.getString("customerName");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                double totalAmount = rs.getDouble("totalAmount");
                String status = rs.getString("status");

                orderModel.addRow(new Object[]{orderID, customerName, productName, quantity, totalAmount, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading orders.");
        }
    }

    // Load feedback into the table
    private void loadFeedback() {
        feedbackModel.setRowCount(0); // Clear table data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CONCAT(c.firstName, ' ', c.lastName) AS customerName, p.name AS productName, f.feedback, fr.response " +
                     "FROM Feedback f " +
                     "JOIN Customers c ON f.customerID = c.userID " +
                     "JOIN Products p ON f.productID = p.productID " +
                     "LEFT JOIN FeedbackResponses fr ON f.feedbackID = fr.feedbackID")) {

            while (rs.next()) {
                String customerName = rs.getString("customerName");
                String productName = rs.getString("productName");
                String feedback = rs.getString("feedback");
                String response = rs.getString("response");

                feedbackModel.addRow(new Object[]{customerName, productName, feedback, response != null ? response : "No response", "Reply"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading feedback.");
        }
    }
    
    // Load products into the product management table
    private void loadProducts() {
        productModel.setRowCount(0); // Clear table data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT productID, name, price, stock FROM Products")) {

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                productModel.addRow(new Object[]{productID, productName, price, stock});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading products.");
        }
    }

    // Add new product method
    private void addNewProduct() {
        JTextField productNameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Product Name:"));
        panel.add(productNameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Stock:"));
        panel.add(stockField);

         int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Product", JOptionPane.OK_CANCEL_OPTION);
    System.out.println("JOptionPane result: " + result); // Debugging line to check dialog result
    if (result == JOptionPane.OK_OPTION) {
        // Get the input values
        String productName = productNameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();

        // Validate the inputs
        if (productName.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        int stock;
        try {
            // Parse the price and stock values
            price = Double.parseDouble(priceText);
            stock = Integer.parseInt(stockText);

            // Ensure that price and stock are positive numbers
            if (price <= 0 || stock < 0) {
                JOptionPane.showMessageDialog(frame, "Price must be positive and stock must be non-negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Price and Stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
       
        // Proceed with database insertion
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Products (name, price, stock) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, productName);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, stock);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Product added successfully!");
                    loadProducts(); // Refresh the product table
                } else {
                    JOptionPane.showMessageDialog(frame, "Error adding product. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding new product to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void removeProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a product to remove.");
            return;
        }

        int productID = (int) productModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this product?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM Products WHERE productID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, productID);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Product removed successfully.");
                        loadProducts(); // Refresh the product table
                    } else {
                        JOptionPane.showMessageDialog(frame, "Product not found or could not be removed.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error removing product.");
            }
        }
    }
    // Update the order status method
    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an order to update.");
            return;
        }
        int orderID = (int) orderModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) orderModel.getValueAt(selectedRow, 5);
        String[] statusOptions = {"Pending", "In Progress", "Completed"};
        String newStatus = (String) JOptionPane.showInputDialog(frame, "Select the new status for the order:", "Update Order Status", JOptionPane.PLAIN_MESSAGE, null, statusOptions, currentStatus);
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE Orders SET status = ? WHERE orderID = ?")) {
                pstmt.setString(1, newStatus);
                pstmt.setInt(2, orderID);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Order status updated successfully.");
                loadOrders(); // Refresh the orders table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating order status.");
            }
        }
    }

    // Block User method
    private void blockUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to block.");
            return;
        }

        int userID = (int) userModel.getValueAt(selectedRow, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET status = 'Blocked' WHERE userID = ?")) {

            pstmt.setInt(1, userID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "User blocked successfully.");
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error blocking user.");
        }
    }

    // Unblock User method
    private void unblockUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to unblock.");
            return;
        }

        int userID = (int) userModel.getValueAt(selectedRow, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET status = 'Customer' WHERE userID = ?")) {

            pstmt.setInt(1, userID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "User unblocked successfully.");
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error unblocking user.");
        }
    }

    // Remove User method
    private void removeUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to remove.");
            return;
        }

        int userID = (int) userModel.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete user-related data in Customers table first
            try (PreparedStatement deleteCustomer = conn.prepareStatement("DELETE FROM Customers WHERE userID = ?")) {
                deleteCustomer.setInt(1, userID);
                deleteCustomer.executeUpdate();
            }

            // Then, delete user from Users table
            try (PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM Users WHERE userID = ?")) {
                deleteUser.setInt(1, userID);
                int rowsAffected = deleteUser.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "User removed successfully.");
                    loadUsers(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(frame, "User not found or could not be removed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error removing user.");
        }
    }

    // Edit User method
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to edit.");
            return;
        }

        int userID = (int) userModel.getValueAt(selectedRow, 0);
        String currentName = (String) userModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) userModel.getValueAt(selectedRow, 2);
        String currentStatus = (String) userModel.getValueAt(selectedRow, 3);

        // Create a panel for editing the user's details
        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);

        // Add a ComboBox for status selection with options: Customer, Baker, Cashier, Blocked
        String[] statuses = {"Customer", "Baker", "Cashier", "Blocked"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setSelectedItem(currentStatus);

        JPanel panel = new JPanel(new GridLayout(3, 2)); // Adjusted for 3 fields: Name, Email, Status
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            String newStatus = (String) statusComboBox.getSelectedItem();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, email = ?, status = ? WHERE userID = ?")) {

                String[] nameParts = newName.split(" ", 2);
                pstmt.setString(1, nameParts[0]);
                pstmt.setString(2, nameParts.length > 1 ? nameParts[1] : "");
                pstmt.setString(3, newEmail);
                pstmt.setString(4, newStatus);
                pstmt.setInt(5, userID);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "User updated successfully.");
                loadUsers();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating user.");
            }
        }
    }

    // Reply to selected feedback
    private void replyToFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a feedback to reply to.");
            return;
        }

        String customerName = (String) feedbackModel.getValueAt(selectedRow, 0);
        String productName = (String) feedbackModel.getValueAt(selectedRow, 1);

        JTextArea replyTextArea = new JTextArea();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Reply to " + customerName + " on Product: " + productName), BorderLayout.NORTH);
        panel.add(new JScrollPane(replyTextArea), BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Reply to Feedback", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String reply = replyTextArea.getText();
            saveResponse(customerName, productName, reply);
        }
    }
    
    // Load ingredients into the table
private void loadIngredients() {
    productModel.setRowCount(0); // Clear table data
    String sql = "SELECT ingredientID, name, quantity, status FROM Ingredients";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int ingredientID = rs.getInt("ingredientID");
            String name = rs.getString("name");
            int quantity = rs.getInt("quantity");
            String status = rs.getString("status");
            productModel.addRow(new Object[]{ingredientID, name, quantity, status});
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error loading ingredients.");
    }
}

// Action for ordering ingredients
private void orderIngredient(JTable productTable) {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(frame, "Please select an ingredient to order.");
        return;
    }

    int ingredientID = (int) productModel.getValueAt(selectedRow, 0);
    String ingredientName = (String) productModel.getValueAt(selectedRow, 1);
    String status = (String) productModel.getValueAt(selectedRow, 3);

    if ("Low".equals(status) || "Finished".equals(status)) {
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to order " + ingredientName + "?", "Confirm Order", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Place the order and update the status
            placeOrder(ingredientID);
        }
    } else {
        JOptionPane.showMessageDialog(frame, "Ingredient status is already sufficient.");
    }
}

// Method to place an order for an ingredient
private void placeOrder(int ingredientID) {
    String updateSQL = "UPDATE Ingredients SET status = 'Ordered' WHERE ingredientID = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

        pstmt.setInt(1, ingredientID);
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(frame, "Order placed successfully.");
            loadIngredients(); // Refresh ingredients table
        } else {
            JOptionPane.showMessageDialog(frame, "Error placing order.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error placing order.");
    }
}

    // Save the manager's response to the database
    private void saveResponse(String customerName, String productName, String response) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO FeedbackResponses (customerName, productName, response) VALUES (?, ?, ?)")) {

            pstmt.setString(1, customerName);
            pstmt.setString(2, productName);
            pstmt.setString(3, response);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Response saved successfully.");
            loadFeedback(); // Refresh the feedback table to show the response
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving response.");
        }
    }

    public static void main(String[] args) {
        new ManagerDashboard(1); // Example manager ID
    }
}
