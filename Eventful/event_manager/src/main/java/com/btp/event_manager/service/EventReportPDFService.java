package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.Event;
import com.itextpdf.io.codec.Base64;
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
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventReportPDFService {
    private static final DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    public static void generateReport(AppService appService) throws FileNotFoundException, MalformedURLException {
        String eventName = appService.getEventName();
        LocalDate startDate = appService.getStartDate();
        LocalDate endDate = appService.getEndDate();
        LocalTime startTime = appService.getStartTime();
        LocalTime endTime = appService.getEndTime();
        String description = appService.getDescription();
        List<String> guests = appService.getGuests();

        String path = "Eventful/dat/";
        path += appService.getCurrUser().getUsername() + "/" + eventName + ".pdf";

        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        String imgPath = "Eventful/event_manager/src/main/resources/logo.png";
        ImageData imageData = ImageDataFactory.create(imgPath);
        Image image = new Image(imageData);

        float x = pdfDocument.getDefaultPageSize().getWidth()/2;
        float y = pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x,y);
        image.setOpacity(1f);
        document.add(image);

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol+150f;
        float twocolWidth[] = {twocol150, twocol};
        float w[] = {threecol*3};
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

        Table divider2 = new Table(w);
        Border dgb = new DashedBorder(Color.GRAY, 0.5f);


        document.add(table);
        document.add(onespc);
        document.add(divider);
        document.add(onespc);
        document.add(twoColTable);

        if (((Event) appService.getSelectedEvent()).getBudgetTracker() != null) {
            document.add(divider2);
            document.add(divider2);
        }
        document.add(onespc);
        document.add(divider);
        document.close();
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
}
