import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.io.*;

public class PDFReceiptGenerator 
{
    public static void generateReceipt(int orderNumber, String customerName, String productDetails, double totalAmount) {
        String fileName = "Receipt_" + orderNumber + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) 
            {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(50, 750);

                // Header
                
                contentStream.showText("   Wee Ken Shin Bakers - Payment Receipt");
                contentStream.newLine();
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 14);

                // Order Details
                contentStream.showText("Order Number:        " + orderNumber);
                contentStream.newLine();
                contentStream.showText("Customer Name: " + customerName);
                contentStream.newLine();
                contentStream.showText("Products Ordered:");
                contentStream.newLine();

                // Product Details
                String[] products = productDetails.split("\n");
                for (String product : products) {
                    contentStream.showText("- "     + product);
                    contentStream.newLine();
                }

                contentStream.newLine();
                contentStream.showText("Total Amount: $"    + totalAmount);
                contentStream.newLine();
                contentStream.showText("Payment Status: Paid");
                contentStream.newLine();
                
                

                // Footer
                contentStream.newLine();
                contentStream.showText("Thank you for choosing Wee Ken Shin Bakers!");
                contentStream.newLine();
                contentStream.endText();
            }

            // Save PDF
            document.save(fileName);
            System.out.println("Receipt generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
