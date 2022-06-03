package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PDFController {

    public static Document createPDF(String content, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String reportName = "Raport";
        reportName = reportName.concat(calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR));
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(reportName + ".pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Font dateFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 25, BaseColor.BLACK);
        Paragraph header = new Paragraph(new Chunk("                                " +
                "RAPORT" + "\n", headerFont));
        Paragraph dateParagraph = new Paragraph(new Chunk("                                                   " +
                "                                                   "
                + "                               raport utworzony dnia  " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR), dateFont));
        Paragraph paragraph = new Paragraph(content);
        document.open();
        try {
            document.add(header);
            document.add(dateParagraph);
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        return document;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


}
