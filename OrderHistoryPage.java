import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.Desktop;

public class OrderHistoryPage {
    private JFrame frame;
    private DefaultTableModel tableModel;
    private int customerId;

    public OrderHistoryPage(int customerId) {
        this.customerId = customerId;

        frame = new JFrame("Wee Ken Shin Bakers - Order History");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Order History");
        titleLabel.setBounds(350, 20, 200, 30);
        frame.add(titleLabel);

        // Table to display orders
        tableModel = new DefaultTableModel(new String[]{"Order Number", "Product Name", "Quantity", "Total Amount", "Status"}, 0);
        JTable orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(50, 70, 800, 350);
        frame.add(scrollPane);

        // Button to download receipt
        JButton downloadReceiptButton = new JButton("Download Receipt");
        downloadReceiptButton.setBounds(300, 450, 200, 30);
        frame.add(downloadReceiptButton);

        // Button to submit feedback
        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.setBounds(500, 450, 200, 30);
        frame.add(submitFeedbackButton);

        // Button to go back to order page
        JButton backButton = new JButton("Back");
        backButton.setBounds(650, 20, 100, 30);
        frame.add(backButton);

        // Button to logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(750, 20, 100, 30);
        frame.add(logoutButton);

        // Load order history
        loadOrderHistory(customerId);

        // Handle "Download Receipt" button click
        downloadReceiptButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an order to download the receipt.");
                return;
            }

            String status = tableModel.getValueAt(selectedRow, 4).toString();
            if (!status.equalsIgnoreCase("Paid")) {
                JOptionPane.showMessageDialog(frame, "Receipt can only be downloaded for Paid orders.");
                return;
            }

            int orderNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String productName = tableModel.getValueAt(selectedRow, 1).toString();
            int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());
            double totalAmount = Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString());

            String productDetails = productName + " x" + quantity;
            generateReceipt(orderNumber, "Customer #" + customerId, productDetails, totalAmount);
            downloadReceipt(orderNumber);
        });

        // Handle "Submit Feedback" button click
        submitFeedbackButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an order to provide feedback.");
                return;
            }

            int orderID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String productName = tableModel.getValueAt(selectedRow, 1).toString();
            showFeedbackForm(orderID, productName);
        });

        // Handle "Back" button click (go back to the order page)
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the order history page
            new CustomerOrderPage(customerId); // Open the customer order page again
        });

        // Handle logout
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new BakerySystemGUI(); // Redirect to login page
        });

        frame.setVisible(true);
    }

    // Load the order history from the database
    private void loadOrderHistory(int customerId) {
        String sql = """
                SELECT o.orderID, p.name AS productName, o.quantity, (p.price * o.quantity) AS totalAmount, o.status
                FROM Orders o
                JOIN Products p ON o.productID = p.productID
                WHERE o.customerID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderNumber = rs.getInt("orderID");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                double totalAmount = rs.getDouble("totalAmount");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{orderNumber, productName, quantity, totalAmount, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading order history.");
        }
    }

    // Generate the receipt
    private void generateReceipt(int orderNumber, String customerName, String productDetails, double totalAmount) {
        try {
            PDFReceiptGenerator.generateReceipt(orderNumber, customerName, productDetails, totalAmount);
            JOptionPane.showMessageDialog(frame, "Receipt generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating receipt.");
        }
    }

    // Download the receipt
    private void downloadReceipt(int orderNumber) {
        String filePath = "Receipt_" + orderNumber + ".pdf";
        File file = new File(filePath);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "Receipt file does not exist. Please ensure the receipt has been generated.");
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error opening receipt file.");
        }
    }

    // Display the feedback form for the selected order
    private void showFeedbackForm(int orderID, String productName) {
        JFrame feedbackFrame = new JFrame("Provide Feedback for " + productName);
        feedbackFrame.setSize(400, 300);
        feedbackFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        feedbackFrame.setLayout(null);

        JLabel feedbackLabel = new JLabel("Please provide your feedback:");
        feedbackLabel.setBounds(30, 30, 200, 30);
        feedbackFrame.add(feedbackLabel);

        JTextArea feedbackTextArea = new JTextArea();
        feedbackTextArea.setBounds(30, 70, 300, 100);
        feedbackFrame.add(feedbackTextArea);

        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.setBounds(100, 200, 150, 30);
        feedbackFrame.add(submitFeedbackButton);

        // Handle feedback submission
        submitFeedbackButton.addActionListener(e -> {
            String feedback = feedbackTextArea.getText();
            submitFeedback(orderID, feedback);
            feedbackFrame.dispose(); // Close the feedback form
        });

        feedbackFrame.setVisible(true);
    }

    // Submit the feedback to the database
    private void submitFeedback(int orderID, String feedbackText) {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkOrderStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE orderID = ?")) {
        
        checkOrderStmt.setInt(1, orderID);
        ResultSet rs = checkOrderStmt.executeQuery();
        rs.next();
        
        if (rs.getInt(1) == 0) {
            JOptionPane.showMessageDialog(frame, "Error: Order ID does not exist.");
            return;  // Stop the process if the order doesn't exist
        }

        // Proceed to insert the feedback
        String sql = "INSERT INTO feedback (orderID, feedback) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
            insertStmt.setInt(1, orderID);  // Use orderID instead of productID
            insertStmt.setString(2, feedbackText);
            insertStmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Feedback submitted successfully!");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error submitting feedback.");
    }
}

    // Refresh the order history table to reflect the new feedback
    private void refreshOrderHistory() {
        // Clear the existing rows
        tableModel.setRowCount(0);

        // Reload the order history after submitting feedback
        loadOrderHistory(customerId);
    }

    public static void main(String[] args) {
        new OrderHistoryPage(1); // Example customer ID
    }
}
