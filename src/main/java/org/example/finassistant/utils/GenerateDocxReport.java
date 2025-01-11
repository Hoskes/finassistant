package org.example.finassistant.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Supply;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// хард код переписать
public class GenerateDocxReport {
    public static ByteArrayInputStream suppliesReport(List<Supply> supplies) {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        try {
            // Добавление заголовка "Закупки"
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText("Закупки");
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            XWPFParagraph descriptionParagraph = document.createParagraph();
            XWPFRun descriptionRun = descriptionParagraph.createRun();
            descriptionRun.setText("Данные о закупках организации приведены в таблице ниже.");
            descriptionRun.setFontSize(12);
            descriptionRun.addBreak();

            // Создание таблицы
            XWPFTable table = document.createTable(supplies.size() + 1, 6);
            table.setWidth("90%");

            // Заголовки таблицы
            String[] headers = {"ID", "Название", "Описание", "Цена (руб.)", "Кол-во", "Сумма (руб.)"};
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.getCell(i).setText(headers[i]);
            }

            long sum = 0;
            int avgQuantity = 0;
            long avgPrice = 0;

            // Добавление данных в таблицу
            for (int i = 0; i < supplies.size(); i++) {
                Supply supply = supplies.get(i);
                XWPFTableRow row = table.getRow(i + 1); // Получаем существующую строку
                row.getCell(0).setText(supply.getId().toString());
                row.getCell(1).setText(supply.getTitle());
                row.getCell(2).setText(supply.getDescription());
                row.getCell(3).setText(decimalFormat.format(supply.getPrice()));
                row.getCell(4).setText(decimalFormat.format(supply.getQuantity()));
                long totalPrice = (long) supply.getPrice() * supply.getQuantity();
                row.getCell(5).setText(decimalFormat.format(totalPrice));

                sum += totalPrice;
                avgQuantity += supply.getQuantity();
                avgPrice += supply.getPrice();
            }

            // Добавление итогов
            XWPFParagraph itogParagraph = document.createParagraph();
            itogParagraph.createRun().setText("Итого: " + decimalFormat.format(sum) + " руб.");

            XWPFParagraph avgPriceParagraph = document.createParagraph();
            avgPriceParagraph.createRun().setText("Средняя цена: " + decimalFormat.format(avgPrice / supplies.size()) + " руб.");

            XWPFParagraph avgQuantityParagraph = document.createParagraph();
            avgQuantityParagraph.createRun().setText("Среднее кол-во: " + decimalFormat.format(avgQuantity / supplies.size()));

            // Добавление даты
            XWPFParagraph dateParagraph = document.createParagraph();
            dateParagraph.setAlignment(ParagraphAlignment.RIGHT);
            dateParagraph.createRun().setText("Дата: " + LocalDateTime.now().toLocalDate());

            document.write(out);
            document.close();

        } catch (Exception ex) {
            Logger.getLogger(GenerateDocxReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream itemsReport(List<Item> items) {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DecimalFormat decimalFormat = new DecimalFormat("####.##");

        try {
            // Добавление заголовка "Товары"
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText("Товары");
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            XWPFParagraph descriptionParagraph = document.createParagraph();
            XWPFRun descriptionRun = descriptionParagraph.createRun();
            descriptionRun.setText("Данные о товарах представлены в таблице ниже.");
            descriptionRun.setFontSize(12);
            descriptionRun.addBreak();

            // Создание таблицы
            XWPFTable table = document.createTable(items.size() + 1, 4);
            table.setWidth("90%");

            // Заголовки таблицы
            String[] headers = {"ID", "Название", "Описание", "Цена (руб.)"};
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.getCell(i).setText(headers[i]);
            }

            double totalPrice = 0;

            // Добавление данных в таблицу
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                XWPFTableRow row = table.getRow(i + 1); // Получаем существующую строку
                row.getCell(0).setText(item.getId().toString());
                row.getCell(1).setText(item.getTitle());
                row.getCell(2).setText(item.getDescription());
                row.getCell(3).setText(decimalFormat.format(item.getPrice()));

                totalPrice += item.getPrice();
            }

            // Добавление итогов
            XWPFParagraph totalParagraph = document.createParagraph();
            totalParagraph.createRun().setText("Средняя цена: " + decimalFormat.format(totalPrice/items.size()) + " руб.");

            // Добавление даты
            XWPFParagraph dateParagraph = document.createParagraph();
            dateParagraph.setAlignment(ParagraphAlignment.RIGHT);
            dateParagraph.createRun().setText("Дата: " + LocalDateTime.now().toLocalDate());

            // Запись документа
            document.write(out);
            document.close();

        } catch (Exception ex) {
            Logger.getLogger(GenerateDocxReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
