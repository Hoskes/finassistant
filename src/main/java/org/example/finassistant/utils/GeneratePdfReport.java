package org.example.finassistant.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.Transaction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// хард код переписать
public class GeneratePdfReport{

    public static ByteArrayInputStream suppliesReport(List<Supply> supplies,LocalDateTime startDate,LocalDateTime endDate) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Добавление заголовка "Закупки"
            Font titleFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Paragraph title = new Paragraph("Закупки", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy");
            Paragraph description = new Paragraph("Данные о закупках организации за период "+startDate.format(f)+" - "+endDate.format(f)+" приведены в таблице ниже.", titleFont);
            description.setAlignment(Element.ALIGN_LEFT);
            description.setFirstLineIndent(20);
            document.add(description);
            // Разделитель между заголовком и таблицей
            document.add(new Paragraph("\n"));

            // Создание таблицы
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1, 2, 4, 2, 2, 2});

            Font headFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfPCell hcell;

            // Заголовки таблицы
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Название", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Описание", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Цена (руб.)", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Кол-во", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Сумма (руб.)", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            long sum =0;
            int avgQuantity = 0;
            long avgPrice = 0;
            // Добавление данных в таблицу
            for (Supply supply : supplies) {
                PdfPCell cell;

                sum += supply.getQuantity()*supply.getPrice();
                avgQuantity+=supply.getQuantity();
                avgPrice+=supply.getPrice();

                cell = new PdfPCell(new Phrase(supply.getId().toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(supply.getTitle(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(supply.getDescription(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(supply.getPrice()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(supply.getQuantity()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(supply.getPrice() * supply.getQuantity()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }


            // Добавление таблицы в документ
            document.add(table);

            Paragraph itog = new Paragraph("Итого: "+ decimalFormat.format(sum) +" руб.", titleFont);
            itog.setAlignment(Element.ALIGN_LEFT);
            itog.setFirstLineIndent(20);
            document.add(itog);

            Paragraph avgPr = new Paragraph("Средняя цена: "+ decimalFormat.format(avgPrice/supplies.size()) +" руб.", titleFont);
            avgPr.setAlignment(Element.ALIGN_LEFT);
            avgPr.setFirstLineIndent(20);
            document.add(avgPr);

            Paragraph avgQuant = new Paragraph("Среднее кол-во: "+ decimalFormat.format(avgQuantity/supplies.size()), titleFont);
            avgQuant.setAlignment(Element.ALIGN_LEFT);
            avgQuant.setFirstLineIndent(20);
            document.add(avgQuant);

            document.add(new Paragraph("\n"));

            Paragraph date = new Paragraph("Дата: "+ LocalDateTime.now().toLocalDate(), titleFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setFirstLineIndent(10);
            document.add(date);

            document.close();

        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    public static ByteArrayInputStream itemsReport(List<Item> items) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy");
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Добавление заголовка "Товары"
            Font titleFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Paragraph title = new Paragraph("Товары", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph description = new Paragraph("Данные о товарах представлены в таблице ниже.", titleFont);
            description.setAlignment(Element.ALIGN_LEFT);
            description.setFirstLineIndent(20);
            document.add(description);
            document.add(new Paragraph("\n"));

            // Создание таблицы
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1, 2, 4, 2});

            Font headFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfPCell hcell;

            // Заголовки таблицы
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Название", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Описание", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Цена (руб.)", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            double totalPrice = 0;

            // Добавление данных в таблицу
            for (Item item : items) {
                PdfPCell cell;

                totalPrice += item.getPrice();

                cell = new PdfPCell(new Phrase(item.getId().toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(item.getTitle(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(item.getDescription(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(item.getPrice()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            // Добавление итогов
            document.add(table);

            Paragraph total = new Paragraph("Средняя цена товара: " + decimalFormat.format(totalPrice/items.size()) + " руб.", titleFont);
            total.setAlignment(Element.ALIGN_LEFT);
            total.setFirstLineIndent(20);
            document.add(total);

            document.add(new Paragraph("\n"));

            Paragraph date = new Paragraph("Дата: " + LocalDateTime.now().toLocalDate(), titleFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setFirstLineIndent(10);
            document.add(date);

            document.close();

        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    public static ByteArrayInputStream transactionReportPdf(List<Transaction> transactions,LocalDateTime startDate,LocalDateTime endDate) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DecimalFormat decimalFormat = new DecimalFormat("####.##");

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy");
            // Заголовок
            Font titleFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Paragraph title = new Paragraph("Отчет по проведенным транзакциям за период"+f.format(startDate)+" - "+f.format(endDate), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            Paragraph indent = new Paragraph("\n\n", titleFont);
            indent.setAlignment(Element.ALIGN_CENTER);
            document.add(indent);
            // Создание таблицы
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1, 3, 4, 3, 2, 2, 2});

            Font headFont = FontFactory.getFont("./static/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfPCell hcell;

            // Заголовки таблицы
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Автор", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Товар", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Дата создания", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Цена (руб.)", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Количество", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Сумма (руб.)", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            long sum = 0;
            int avgQuantity = 0;
            long avgPrice = 0;
            HashMap<String,Integer> mostOperator= new HashMap<>();
            // Добавление данных в таблицу
            for (Transaction transaction : transactions) {
                PdfPCell cell;

                long totalPrice = (long) (transaction.getQuantity() * transaction.getPayableItem().getPrice());
                sum += totalPrice;
                avgQuantity += transaction.getQuantity();
                avgPrice += transaction.getPayableItem().getPrice();

                cell = new PdfPCell(new Phrase(transaction.getId().toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(transaction.getAuthor().getName(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                mostOperator.merge(transaction.getAuthor().getName(),1,Integer::sum);

                cell = new PdfPCell(new Phrase(transaction.getPayableItem().getTitle(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(transaction.getDate_created().toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(transaction.getPayableItem().getPrice()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(transaction.getQuantity()), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(decimalFormat.format(totalPrice), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }
            String operator ="";
            int max = 0;
            for (Map.Entry<String, Integer> entry : mostOperator.entrySet()) {
                if (entry.getValue() >= max) {
                    max = entry.getValue();
                    operator = entry.getKey();
                }
            }
            System.out.println(operator+"#  #"+max);
            // Добавление итогов
            document.add(table);
            document.add(new Paragraph("Итого: " + decimalFormat.format(sum) + " руб.", titleFont));
            document.add(new Paragraph("Средняя цена: " + decimalFormat.format(avgPrice / transactions.size()) + " руб.", titleFont));
            document.add(new Paragraph("Лучший оператор за период: " + operator,titleFont));
            document.add(new Paragraph("Лучшее количество продаж оператором за период: " + max, titleFont));
            document.add(new Paragraph("Дата: " + LocalDateTime.now().toLocalDate(), titleFont));

            document.close();

        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
