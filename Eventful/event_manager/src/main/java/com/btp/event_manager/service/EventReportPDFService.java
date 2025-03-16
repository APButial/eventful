package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.budget.model.ExpenseEntry;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventReportPDFService {
    private static final DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    public static void generateReport(AppService appService) throws IOException {
        String eventName = appService.getEventName();
        LocalDate startDate = appService.getStartDate();
        LocalDate endDate = appService.getEndDate();
        LocalTime startTime = appService.getStartTime();
        LocalTime endTime = appService.getEndTime();
        String description = appService.getDescription();
        List<String> guests = appService.getGuests();

        String path = "Eventful/dat/";
        path += appService.getCurrUser().getUsername() + "/" + eventName.toLowerCase().replaceAll(" ", "_") + "_report.pdf";

        // checks if PDF is open
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            if (!pdfFile.delete()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Event Detail");
                alert.setHeaderText("The PDF file is currently open.");
                alert.setContentText("Please close the PDF file and try again.");
                alert.showAndWait();
                return; // Exit the method if the file is open
            }
        }

        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        float fourCol = 142.5f;
        float twocol = 285f;
        float twocol150 = twocol+150f;
        float twocolWidth[] = {twocol150, twocol};
        float fourColWidth[] = {fourCol, fourCol, fourCol, fourCol};
        float w[] = {fourCol*4};
        Paragraph onespc = new Paragraph("\n");

        Table table = new Table(twocolWidth);
        table.addCell(new Cell().add(appService.getEventName().toUpperCase() + " - Event Details").setFontSize(16f).setBorder(Border.NO_BORDER).setBold());

        Table nestTable = new Table(new float[]{twocol/2,twocol/2});
        nestTable.addCell(getHeaderTextCell("Start Date:"));
        nestTable.addCell(getHeaderTextCellValue(dtFormat.format(startDate)));
        nestTable.addCell(getHeaderTextCell("End Date:"));
        nestTable.addCell(getHeaderTextCellValue(dtFormat.format(endDate)));

        table.addCell(new Cell().add(nestTable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(Color.GRAY, 1f/2f);
        Table divider = new Table(w);
        divider.setBorder(gb);

        Table twoColTable = new Table(twocolWidth);
        Table leftColTable = new Table(new float[]{twocolWidth[0]});
        Table rightColTable = new Table(new float[]{twocolWidth[1]});

        leftColTable.addCell(getCell12fLeft("Event Name", true));
        leftColTable.addCell(getCell12fLeft(eventName, false));
        leftColTable.addCell(getCell12fLeft("Start Date", true));
        leftColTable.addCell(getCell12fLeft(dtFormat.format(startDate), false));
        leftColTable.addCell(getCell12fLeft("End Date", true));
        leftColTable.addCell(getCell12fLeft(dtFormat.format(endDate), false));
        leftColTable.addCell(getCell12fLeft("Start Time", true));;
        leftColTable.addCell(getCell12fLeft(startTime.toString(), false));
        leftColTable.addCell(getCell12fLeft("End Time", true));
        leftColTable.addCell(getCell12fLeft(endTime.toString(), false));
        leftColTable.addCell(getCell12fLeft("Description", true));
        leftColTable.addCell(getCell12fLeft(description, false));

        rightColTable.addCell(getCell12fLeft("Number of Guests", true));
        rightColTable.addCell(getCell12fLeft(""+guests.size(), false));
        rightColTable.addCell(getCell12fLeft("Guests", true));

        Paragraph guestsParagraph = new Paragraph(String.join("\n", guests)).setFontSize(10f).setMultipliedLeading(0.75f);
        rightColTable.addCell(new Cell().add(guestsParagraph).setBorder(Border.NO_BORDER));

        twoColTable.addCell(new Cell().add(leftColTable).setBorder(Border.NO_BORDER));
        twoColTable.addCell(new Cell().add(rightColTable).setBorder(Border.NO_BORDER));
        twoColTable.setMarginBottom(50f);

        Table divider2 = new Table(w);
        Border dgb = new DashedBorder(Color.GRAY, 0.5f);
        divider2.setBorder(dgb);

        document.add(table);
        document.add(onespc);
        document.add(divider);
        document.add(onespc);
        document.add(twoColTable);

        if (((EventManAppService) appService).getBudgetTracker() != null) {
            document.add(divider2);
            document.add(onespc);

            Paragraph expensePar = new Paragraph("Expenses Tracker");
            document.add(expensePar.setBold());

            Table fourColTableUp = new Table(fourColWidth);
            fourColTableUp.setBorder(Border.NO_BORDER);

            fourColTableUp.setBackgroundColor(Color.BLACK, 0.7f);
            fourColTableUp.addCell(new Cell().add("Quantity").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            fourColTableUp.addCell(new Cell().add("Item").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            fourColTableUp.addCell(new Cell().add("Cost Per Item (PHP)").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            fourColTableUp.addCell(new Cell().add("Total (PHP)").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

            Table fourColTableLow = new Table(fourColWidth);
            fourColTableLow.setBackgroundColor(Color.WHITE).setBorder(new SolidBorder(Border.SOLID));

            Table fourColTable1 = new Table(new float[]{fourColWidth[0]});
            fourColTable1.setBackgroundColor(Color.WHITE);
            Table fourColTable2 = new Table(new float[]{fourColWidth[1]});
            fourColTable2.setBackgroundColor(Color.WHITE);
            Table fourColTable3 = new Table(new float[]{fourColWidth[2]});
            fourColTable3.setBackgroundColor(Color.WHITE);
            Table fourColTable4 = new Table(new float[]{fourColWidth[3]});
            fourColTable4.setBackgroundColor(Color.WHITE);

            for (ExpenseEntry entry : ((EventManAppService) appService).getBudgetTracker().getExpenses()) {
                fourColTable1.addCell(new Cell().add(String.valueOf(entry.getQuantity())).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginLeft(5f));
                fourColTable2.addCell(new Cell().add(entry.getItemName()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                fourColTable3.addCell(new Cell().add(String.format("%.2f", entry.getCostPerItem())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                fourColTable4.addCell(new Cell().add(String.format("%.2f", entry.getQuantity()*entry.getCostPerItem())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(5f));
            }

            fourColTableLow.addCell(new Cell().add(fourColTable1).setBorder(Border.NO_BORDER));
            fourColTableLow.addCell(new Cell().add(fourColTable2).setBorder(Border.NO_BORDER));
            fourColTableLow.addCell(new Cell().add(fourColTable3).setBorder(Border.NO_BORDER));
            fourColTableLow.addCell(new Cell().add(fourColTable4).setBorder(Border.NO_BORDER));

            document.add(fourColTableUp);
            document.add(fourColTableLow);

            double sum = ((EventManAppService) appService).getBudgetTracker().getTotalExpenses();
            Paragraph totSum = new Paragraph("Total Sum (PHP): " + String.format("%.2f", sum));
            totSum.setBold().setTextAlignment(TextAlignment.RIGHT);
            document.add(totSum);

            document.add(onespc);
            document.add(divider2);
        }

        document.add(onespc);
        document.add(divider);
        document.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Detail");
        alert.setHeaderText("PDF file has been successfully exported.");
        alert.setContentText("The PDF file is saved in " + path + ".");
        alert.showAndWait();

        openPDF(path);
    }

    static Cell getHeaderTextCell (String text) {
        return new Cell().add(text).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextCellValue (String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell12fLeft (String text, Boolean isBold) {
        Cell cell = new Cell().add(text).setFontSize(12f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? cell.setBold().setUnderline():cell ;
    }

    private static void openPDF(String path) throws IOException {
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            Desktop.getDesktop().open(pdfFile);
        }
    }
}
