/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import javax.swing.JFrame;
import javax.swing.*;

/**
 *
 * @author ACER
 */
public class PopupWindow 
{
     private boolean confirm;
    PopupWindow(String string1, String yesString, String noString)
    {
       
        int result = JOptionPane.showConfirmDialog(
                null, 
                string1, 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
        );

        // Handle the user’s choice
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, yesString, "Result", JOptionPane.INFORMATION_MESSAGE);
            confirm = true;
        } else if (result == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, noString, "Result", JOptionPane.INFORMATION_MESSAGE);
            confirm = false;
        } else {
            JOptionPane.showMessageDialog(null, "Operation was cancelled.", "Result", JOptionPane.INFORMATION_MESSAGE);
            confirm = false;
        }
    }
    
    public boolean confirm()
    {
        return confirm;
    }

    
}
