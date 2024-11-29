import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class CartItem {
    private int productID;
    private String productName;
    private double price;
    private int quantity;

    public CartItem(int productID, String productName, double price, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

public class CustomerOrderPage {
    private JFrame frame;
    private DefaultTableModel productTableModel;
    private DefaultTableModel cartTableModel;
    private List<CartItem> cartItems = new ArrayList<>();
    private int customerId;

    public CustomerOrderPage(int customerId) {
        this.customerId = customerId;

        frame = new JFrame("Wee Ken Shin Bakers - Order Page");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Products:");
        titleLabel.setBounds(30, 10, 100, 30);
        frame.add(titleLabel);

        productTableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Price", "Stock"}, 0);
        JTable productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBounds(30, 50, 400, 300);
        frame.add(productScrollPane);

        JLabel cartLabel = new JLabel("Your Cart:");
        cartLabel.setBounds(500, 10, 100, 30);
        frame.add(cartLabel);

        cartTableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Price"}, 0);
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBounds(500, 50, 350, 300);
        frame.add(cartScrollPane);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 370, 100, 30);
        frame.add(quantityLabel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setBounds(120, 370, 50, 30);
        frame.add(quantitySpinner);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBounds(200, 370, 150, 30);
        frame.add(addToCartButton);

        JButton removeFromCartButton = new JButton("Remove Item");
        removeFromCartButton.setBounds(500, 370, 150, 30);
        frame.add(removeFromCartButton);

        JButton submitOrderButton = new JButton("Submit Order");
        submitOrderButton.setBounds(500, 420, 150, 30);
        frame.add(submitOrderButton);

        // Add Order History Button
        JButton orderHistoryButton = new JButton("Order History");
        orderHistoryButton.setBounds(30, 500, 150, 30);
        frame.add(orderHistoryButton);

        // Add Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(200, 500, 150, 30);
        frame.add(logoutButton);

        loadProducts();

        addToCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a product to add to the cart.");
                return;
            }

            int productID = Integer.parseInt(productTableModel.getValueAt(selectedRow, 0).toString());
            String productName = productTableModel.getValueAt(selectedRow, 1).toString();
            double price = Double.parseDouble(productTableModel.getValueAt(selectedRow, 2).toString());
            int quantity = (int) quantitySpinner.getValue();
            addToCart(productID, productName, price, quantity);
            updateCartTable();
        });

        removeFromCartButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove from the cart.");
                return;
            }

            cartItems.remove(selectedRow);
            updateCartTable();
        });

        submitOrderButton.addActionListener(e -> submitOrder());

        orderHistoryButton.addActionListener(e -> {
            frame.dispose();
            new OrderHistoryPage(customerId);
        });

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new BakerySystemGUI();
        });

        frame.setVisible(true);
    }

    private void loadProducts() {
        String sql = "SELECT * FROM Products";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                productTableModel.addRow(new Object[]{productID, name, price, stock});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading products.");
        }
    }

    private void addToCart(int productID, String productName, double price, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductID() == productID) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(productID, productName, price, quantity));
    }

    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        for (CartItem item : cartItems) {
            cartTableModel.addRow(new Object[]{item.getProductName(), item.getQuantity(), item.getPrice() * item.getQuantity()});
        }
    }

    private void submitOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty.");
            return;
        }

        String insertOrderSQL = "INSERT INTO Orders (customerID, productID, quantity, status) VALUES (?, ?, ?, 'Unpaid')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL)) {

            for (CartItem item : cartItems) {
                pstmt.setInt(1, customerId);
                pstmt.setInt(2, item.getProductID());
                pstmt.setInt(3, item.getQuantity());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            JOptionPane.showMessageDialog(frame, "Order submitted successfully!");
            cartItems.clear();
            updateCartTable();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error submitting order.");
        }
    }
}
