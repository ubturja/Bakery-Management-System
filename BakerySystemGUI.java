import javax.swing.*;

public class BakerySystemGUI {
    public static void main(String[] args) {
        
        Login login = new Login();
        Registration registration = new Registration();
        ForgetPassword forgetPassword = new ForgetPassword();
        
        JFrame loginFrame = new JFrame("Wee Ken Shin Bakers - Login");
        loginFrame.setSize(1370, 780);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);

        // System Title
        JLabel systemTitle = new JLabel("Welcome to Wee Ken Shin Bakers!");
        systemTitle.setBounds(550, 100, 400, 40);
        systemTitle.setFont(systemTitle.getFont().deriveFont(20f)); // Larger Font
        loginFrame.add(systemTitle);
        
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(500, 200, 100, 30);
        JTextField emailField = new JTextField();
        emailField.setBounds(600, 200, 250, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 250, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(600, 250, 250, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(630, 300, 120, 30);
        loginButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/login.png")));

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(780, 300, 120, 30);
        registerButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/new product.png")));

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(500, 350, 400, 30);
        
        JButton forgetPasswordButton = new JButton("Forget Password?");
        forgetPasswordButton.setBounds(400, 300, 175, 30);
        forgetPasswordButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/change Security Question.png")));

        // Adding components to the frame
        loginFrame.add(emailLabel);
        loginFrame.add(emailField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);
        loginFrame.add(messageLabel);
        loginFrame.add(systemTitle);
        loginFrame.add(forgetPasswordButton);

        // Background Image
        JLabel bakerybackground = new JLabel();
        bakerybackground.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/bakery.jpg")));
        bakerybackground.setBounds(0, 0, 1370, 780);
        loginFrame.add(bakerybackground);

        // Set the background label behind other components
        loginFrame.getContentPane().setComponentZOrder(bakerybackground, loginFrame.getContentPane().getComponentCount() - 1);
        loginFrame.setVisible(true);

        // Login Button Action
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            String role = login.authenticateUser(email, password);
            
            if (role != null) {
                messageLabel.setText("Login successful! Role: " + role);
                switch (role) {
                    case "Customer":
                        int customerId = login.getUserIdByEmail(email);
                        if (customerId != -1) {
                            loginFrame.dispose(); // Close login frame
                            new CustomerOrderPage(customerId); // Open Customer Order Page
                        } else {
                            JOptionPane.showMessageDialog(loginFrame, "Error retrieving user ID.");
                        }
                        break;
                    case "Cashier":
                        loginFrame.dispose(); // Close login frame
                        new CashierDashboard(); // Open Cashier Dashboard
                        break;
                    case "Manager":
                        loginFrame.dispose(); // Close login frame
                        int managerId = login.getUserIdByEmail(email); // Get manager's ID based on email (assuming manager is a user)
                        new ManagerDashboard(managerId); // Pass the managerId to ManagerDashboard constructor
                        break;
                    case "Baker":
                        loginFrame.dispose(); // Close login frame
                        new BakerDashboard(); // Open Baker Dashboard
                        break;
                    default:
                        JOptionPane.showMessageDialog(loginFrame, "Invalid Role or Incorrect Credentials.");
                        break;
                }
            } else {
                messageLabel.setText("Invalid email or password. Try again.");
            }
        });

        // Register Button Action
        registerButton.addActionListener(e -> {
            loginFrame.dispose();
            openRegistrationWindow(registration, loginFrame);
        });
        
        // Forget Password Button Action
        forgetPasswordButton.addActionListener(e -> {
            loginFrame.dispose();
            openForgetPasswordWindow(forgetPassword, loginFrame);
        });
    }

    // Open Registration Window
    public static void openRegistrationWindow(Registration registration, JFrame loginFrame) {
        JFrame registerFrame = new JFrame("Wee Ken Shin Bakers - Register");
        registerFrame.setSize(1370, 780);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setLayout(null);

        JLabel systemTitle = new JLabel("Wee Ken Shin Bakers - Registration");
        systemTitle.setBounds(550, 100, 400, 40);
        systemTitle.setFont(systemTitle.getFont().deriveFont(20f)); // Larger Font
        registerFrame.add(systemTitle);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(500, 150, 100, 30);
        JTextField firstNameField = new JTextField();
        firstNameField.setBounds(600, 150, 250, 30);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(500, 200, 100, 30);
        JTextField lastNameField = new JTextField();
        lastNameField.setBounds(600, 200, 250, 30);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(500, 250, 100, 30);
        JTextField emailField = new JTextField();
        emailField.setBounds(600, 250, 250, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 300, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(600, 300, 250, 30);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(500, 350, 150, 30);
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(600, 350, 250, 30);

        JLabel securityQuestionLabel = new JLabel("Security Question:");
        securityQuestionLabel.setBounds(500, 400, 150, 30);
        JTextField securityQuestionField = new JTextField();
        securityQuestionField.setBounds(600, 400, 250, 30);

        JLabel securityAnswerLabel = new JLabel("Security Answer:");
        securityAnswerLabel.setBounds(500, 450, 150, 30);
        JTextField securityAnswerField = new JTextField();
        securityAnswerField.setBounds(600, 450, 250, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(600, 550, 100, 30);

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(500, 600, 400, 30);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(750, 550, 100, 30);
        exitButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/exit small.png")));

        registerFrame.add(firstNameLabel);
        registerFrame.add(firstNameField);
        registerFrame.add(lastNameLabel);
        registerFrame.add(lastNameField);
        registerFrame.add(emailLabel);
        registerFrame.add(emailField);
        registerFrame.add(passwordLabel);
        registerFrame.add(passwordField);
        registerFrame.add(confirmPasswordLabel);
        registerFrame.add(confirmPasswordField);
        registerFrame.add(securityQuestionLabel);
        registerFrame.add(securityQuestionField);
        registerFrame.add(securityAnswerLabel);
        registerFrame.add(securityAnswerField);
        registerFrame.add(submitButton);
        registerFrame.add(exitButton);
        registerFrame.add(messageLabel);
        registerFrame.add(systemTitle);

        // Background Image
        JLabel bakerybackground = new JLabel();
        bakerybackground.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/bakery.jpg")));
        bakerybackground.setBounds(0, 0, 1370, 780);
        registerFrame.add(bakerybackground);

        registerFrame.setVisible(true);

        submitButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String securityQuestion = securityQuestionField.getText();
            String securityAnswer = securityAnswerField.getText();
            
            PopupWindow popupWindow = new PopupWindow("Are you sure your information is correct?", "Registering.","No changes have been made.");
            popupWindow.confirm();
            
            if (popupWindow.confirm()==true)
            {
                String success = registration.registerUser(firstName, lastName, email, password, confirmPassword, securityQuestion, securityAnswer);
                if (null != success) 
                switch (success) {
                    case "Same":
                        messageLabel.setText("Passwords do not match.");
                        break;
                    case "Success":
                        messageLabel.setText("Registration successful!");
                        break;
                    case "Fail":
                        messageLabel.setText("Registration failed. Try again.");
                        break;
                    default:
                        break;
                }
            }

        });

        // Exit Button Action
        exitButton.addActionListener(e -> {
            registerFrame.dispose();         // Close the registration window
            loginFrame.setVisible(true);     // Show the login window again
        });
    }
    
    // Forget Password Window
    public static void openForgetPasswordWindow(ForgetPassword forgetPassword, JFrame loginFrame) {
        JFrame forgetFrame = new JFrame("Wee Ken Shin Bakers - Forget Password");
        forgetFrame.setSize(1370, 780);
        forgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        forgetFrame.setLayout(null);

        // System Title
        JLabel systemTitle = new JLabel("Wee Ken Shin Bakers - Forget Password");
        systemTitle.setBounds(550, 100, 400, 40);
        systemTitle.setFont(systemTitle.getFont().deriveFont(20f)); // Larger Font
        forgetFrame.add(systemTitle);

        // Components setup
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(500, 150, 100, 30);
        JTextField emailField = new JTextField();
        emailField.setBounds(600, 150, 250, 30);
        JButton checkButton = new JButton("Check");
        checkButton.setBounds(900, 150, 100, 30);
        checkButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/search.png")));
        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(500, 200, 400, 30);

        // Security Question Fields
        JLabel securityAnswerLabel = new JLabel("Security Answer:");
        securityAnswerLabel.setBounds(500, 250, 150, 30);
        JTextField securityAnswerField = new JTextField();
        securityAnswerField.setBounds(600, 250, 250, 30);
        JButton verifyButton = new JButton("Verify");
        verifyButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/verify users.png")));
        verifyButton.setBounds(900, 250, 100, 30);
        JLabel messageLabel2 = new JLabel("");
        messageLabel2.setBounds(500, 300, 400, 30);

        // New Password Fields
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(500, 350, 150, 30);
        JTextField newPasswordField = new JTextField();
        newPasswordField.setBounds(600, 350, 250, 30);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(480, 400, 150, 30);
        JTextField confirmPasswordField = new JTextField();
        confirmPasswordField.setBounds(600, 400, 250, 30);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(900, 400, 200, 30);
        changePasswordButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/change Password.png")));
        JLabel messageLabel3 = new JLabel("");
        messageLabel3.setBounds(500, 450, 400, 30);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(750, 550, 100, 30);
        exitButton.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/exit small.png")));

        // Add components
        forgetFrame.add(emailLabel);
        forgetFrame.add(emailField);
        forgetFrame.add(checkButton);
        forgetFrame.add(messageLabel);
        forgetFrame.add(securityAnswerLabel);
        forgetFrame.add(securityAnswerField);
        forgetFrame.add(verifyButton);
        forgetFrame.add(messageLabel2);
        forgetFrame.add(newPasswordLabel);
        forgetFrame.add(newPasswordField);
        forgetFrame.add(confirmPasswordLabel);
        forgetFrame.add(confirmPasswordField);
        forgetFrame.add(changePasswordButton);
        forgetFrame.add(messageLabel3);
        forgetFrame.add(exitButton);

        // Background Image
        JLabel bakerybackground = new JLabel();
        bakerybackground.setIcon(new javax.swing.ImageIcon(BakerySystemGUI.class.getResource("/images/bakery.jpg")));
        bakerybackground.setBounds(0, 0, 1370, 780);
        forgetFrame.add(bakerybackground);

        forgetFrame.setVisible(true);

        // Initially hide components
        securityAnswerField.setVisible(false);
        securityAnswerLabel.setVisible(false);
        verifyButton.setVisible(false);
        newPasswordLabel.setVisible(false);
        newPasswordField.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        confirmPasswordField.setVisible(false);
        changePasswordButton.setVisible(false);

        // Check Button Action
        checkButton.addActionListener(e -> {
            String email = emailField.getText();
            String securityQuestion = forgetPassword.getSecurityQuestion(email);

            if (securityQuestion != null) {
                messageLabel.setText("Email Found! Security Question: " + securityQuestion);
                securityAnswerField.setVisible(true);
                securityAnswerLabel.setVisible(true);
                verifyButton.setVisible(true);
                messageLabel2.setVisible(true);
            } else {
                messageLabel.setText("Email not found. Try again.");
                securityAnswerField.setVisible(false);
                securityAnswerLabel.setVisible(false);
                verifyButton.setVisible(false);
                messageLabel2.setVisible(false);
                newPasswordLabel.setVisible(false);
                newPasswordField.setVisible(false);
                confirmPasswordLabel.setVisible(false);
                confirmPasswordField.setVisible(false);
                changePasswordButton.setVisible(false);
            }
        });

        // Verify Button Action
        verifyButton.addActionListener(e -> {
            String email = emailField.getText();
            String securityAnswer = securityAnswerField.getText();

            boolean verify = forgetPassword.verifySecurityAnswer(email, securityAnswer);

            if (verify) {
                messageLabel2.setText("Email Verified!!");
                newPasswordLabel.setVisible(true);
                newPasswordField.setVisible(true);
                confirmPasswordLabel.setVisible(true);
                confirmPasswordField.setVisible(true);
                changePasswordButton.setVisible(true);
            } else {
                messageLabel2.setText("Security Answer Incorrect. Try again.");
                newPasswordLabel.setVisible(false);
                newPasswordField.setVisible(false);
                confirmPasswordLabel.setVisible(false);
                confirmPasswordField.setVisible(false);
                changePasswordButton.setVisible(false);
            }
        });

        // Change Password Button Action
        changePasswordButton.addActionListener(e -> {
            String email = emailField.getText();
            String securityAnswer = securityAnswerField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            String changePassword = forgetPassword.changePassword(email, securityAnswer, newPassword, confirmPassword);
            PopupWindow popupWindow = new PopupWindow("Are you sure you want to change password?", "Changing Password.","No changes have been made.");
            popupWindow.confirm();
            
            if(popupWindow.confirm()== true){
                if ("donotmatch".equals(changePassword)) {
                    messageLabel3.setText("Password Do Not Match");
                } else if ("same".equals(changePassword)) {
                    messageLabel3.setText("New Password is Old Password");
                } else {
                    messageLabel3.setText("Password Changed!!");
                }
            }
            
        });

        // Exit Button Action
        exitButton.addActionListener(e -> {
            forgetFrame.dispose();  // Close the forget password window
            loginFrame.setVisible(true);  // Show the login window again
        });
    }
}
