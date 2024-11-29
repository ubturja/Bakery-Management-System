import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.*;

public class CashierOrderManagementPage {
    private JFrame frame;
    private DefaultTableModel tableModel;

    public CashierOrderManagementPage() {
        frame = new JFrame("Wee Ken Shin Bakers - Cashier Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setBounds(300, 20, 200, 30);
        frame.add(titleLabel);

        tableModel = new DefaultTableModel(new String[]{"Order Number", "Customer ID", "Product ID", "Quantity", "Status"}, 0);
        JTable orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(50, 70, 700, 400);
        frame.add(scrollPane);

        JButton confirmPaymentButton = new JButton("Confirm Payment");
        confirmPaymentButton.setBounds(300, 500, 200, 30);
        frame.add(confirmPaymentButton);

        // Load unpaid orders
        loadOrders();

        confirmPaymentButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an order to confirm payment.");
                return;
            }

            int orderNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            confirmPayment(orderNumber);
        });

        frame.setVisible(true);
    }

    private void loadOrders() {
        String sql = "SELECT * FROM Orders WHERE status = 'Unpaid'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderNumber = rs.getInt("orderID");
                int customerId = rs.getInt("customerID");
                int productId = rs.getInt("productID");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{orderNumber, customerId, productId, quantity, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading orders.");
        }
    }

    private void confirmPayment(int orderNumber) {
        // Fetch order details from database
        String fetchDetailsSQL = "SELECT o.orderID, o.customerID, u.username AS customerName, p.name AS productName, o.quantity, p.price " +
                                 "FROM Orders o " +
                                 "JOIN Users u ON o.customerID = u.userID " +
                                 "JOIN Products p ON o.productID = p.productID " +
                                 "WHERE o.orderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement fetchStmt = conn.prepareStatement(fetchDetailsSQL)) {

            fetchStmt.setInt(1, orderNumber);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                // Extract order details
                String customerName = rs.getString("customerName");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double totalAmount = quantity * price;

                // Update order status to 'Paid'
                String updateSQL = "UPDATE Orders SET status = 'Paid' WHERE orderID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                    updateStmt.setInt(1, orderNumber);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        // Generate Receipt
                        generateReceipt(orderNumber, customerName, productName, quantity, totalAmount);
                        JOptionPane.showMessageDialog(frame, "Payment confirmed and receipt generated.");
                        loadOrders(); // Reload the order list
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error confirming payment.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Order not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error processing payment.");
        }
    }

    private void generateReceipt(int orderNumber, String customerName, String productName, int quantity, double totalAmount) {
        String fileName = "Receipt_" + orderNumber + ".pdf";
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750);
            contentStream.showText("Wee Ken Shin Bakers - Receipt");
            contentStream.endText();

            // Order details
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Order Number: " + orderNumber);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Customer Name: " + customerName);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Product Name: " + productName);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Quantity: " + quantity);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Total Amount: $" + totalAmount);
            contentStream.endText();

            contentStream.close();
            document.save(fileName);

            JOptionPane.showMessageDialog(frame, "Receipt generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating receipt.");
        }
    }
}
