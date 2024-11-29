import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CashierDashboard {
    private JFrame frame;
    private DefaultTableModel tableModel;
    private DefaultTableModel productTableModel;

    public CashierDashboard() {
        frame = new JFrame("Wee Ken Shin Bakers - Cashier Dashboard");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());  // Use BorderLayout here

        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, BorderLayout.CENTER);  // Use BorderLayout here

        // Order Management Tab
        JPanel orderManagementPanel = new JPanel(new BorderLayout());  // Use BorderLayout here
        tabbedPane.addTab("Order Management", orderManagementPanel);

        tableModel = new DefaultTableModel(new String[]{"Order Number", "Customer ID", "Product Name", "Quantity", "Status"}, 0);
        JTable orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        orderManagementPanel.add(scrollPane, BorderLayout.CENTER);  // Use BorderLayout here

        JPanel orderButtonPanel = new JPanel();
        JButton confirmPaymentButton = new JButton("Confirm Payment");
        orderButtonPanel.add(confirmPaymentButton);
        orderManagementPanel.add(orderButtonPanel, BorderLayout.SOUTH);  // Use BorderLayout here

        // Product Catalogue Tab
        JPanel productCataloguePanel = new JPanel(new BorderLayout());  // Use BorderLayout here
        tabbedPane.addTab("Product Catalogue", productCataloguePanel);

        productTableModel = new DefaultTableModel(new String[]{"Product Name", "Price", "Stock"}, 0);
        JTable productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productCataloguePanel.add(productScrollPane, BorderLayout.CENTER);  // Use BorderLayout here

        // Logout Button
        JPanel logoutPanel = new JPanel();
        JButton logoutButton = new JButton("Logout");
        logoutPanel.add(logoutButton);
        frame.add(logoutPanel, BorderLayout.SOUTH);  // Place logout button at the bottom

        loadOrders();
        loadProducts(); // Load product catalogue

        // Confirm Payment Button Action
        confirmPaymentButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an order to confirm payment.");
                return;
            }

            int orderNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            int customerId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
            String productName = tableModel.getValueAt(selectedRow, 2).toString();
            int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());

            confirmPayment(orderNumber, customerId, productName, quantity);
        });

        // Logout Button Action
        logoutButton.addActionListener(e -> {
            frame.dispose();  // Close CashierDashboard window
            new Login(); // Redirect to login screen
        });

        frame.setVisible(true);
    }

    // Load orders into the table
    private void loadOrders() {
        String sql = "SELECT o.orderID, o.customerID, o.productID, p.name AS productName, o.quantity, o.status " +
                     "FROM Orders o JOIN Products p ON o.productID = p.productID WHERE o.status = 'Unpaid'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            tableModel.setRowCount(0); // Clear previous rows
            while (rs.next()) {
                int orderNumber = rs.getInt("orderID");
                int customerId = rs.getInt("customerID");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{orderNumber, customerId, productName, quantity, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading orders.");
        }
    }

    // Load product catalogue into the table
    private void loadProducts() {
        String sql = "SELECT name, price FROM Products";  // Removed description from the query

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            productTableModel.setRowCount(0); // Clear previous rows
            while (rs.next()) {
                String productName = rs.getString("name");
                double price = rs.getDouble("price");

                // Add row to the product catalogue table
                productTableModel.addRow(new Object[]{productName, price});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading products.");
        }
    }

    // Confirm payment for an order
    private void confirmPayment(int orderNumber, int customerId, String productName, int quantity) {
        String updateSql = "UPDATE Orders SET status = 'Paid' WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            pstmt.setInt(1, orderNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Payment confirmed for Order #" + orderNumber);
                generateReceipt(orderNumber, customerId, productName, quantity);
                loadOrders(); // Refresh orders table
            } else {
                JOptionPane.showMessageDialog(frame, "Error confirming payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating payment status.");
        }
    }

    // Generate receipt for the paid order
    private void generateReceipt(int orderNumber, int customerId, String productName, int quantity) {
        try {
            double totalAmount = getTotalAmount(orderNumber);
            String customerName = getCustomerName(customerId);

            PDFReceiptGenerator.generateReceipt(orderNumber, customerName, productName + " x " + quantity, totalAmount);
            JOptionPane.showMessageDialog(frame, "Receipt generated for Order #" + orderNumber);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating receipt.");
        }
    }

    // Get total amount for the order
    private double getTotalAmount(int orderNumber) throws SQLException {
        String sql = "SELECT SUM(p.price * o.quantity) AS totalAmount " +
                     "FROM Orders o JOIN Products p ON o.productID = p.productID " +
                     "WHERE o.orderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("totalAmount");
            }
        }
        return 0;
    }

    // Get customer name by customer ID
    private String getCustomerName(int customerId) throws SQLException {
        String sql = "SELECT firstName, lastName FROM Users WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("firstName") + " " + rs.getString("lastName");
            }
        }
        return "Unknown Customer";
    }

    public static void main(String[] args) {
        new CashierDashboard(); // Example starting the dashboard
    }
}
