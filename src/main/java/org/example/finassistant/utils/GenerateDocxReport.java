package org.example.finassistant.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.Transaction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            String[] headers = {"ID", "Название", "Описание", "Цена (руб.)", "Кол-во", "Сумма (руб.)"};
            // Создание таблицы
            XWPFTable table = document.createTable(supplies.size() + 1, headers.length);
            table.setWidth("90%");

            // Заголовки таблицы

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
    public static ByteArrayInputStream transactionReportDocx(List<Transaction> transactions, LocalDateTime startDate,LocalDateTime endDate) {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy");
        try {
            // Заголовок
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText("Отчет по транзакциям за период "+f.format(startDate)+" - "+f.format(endDate));
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            XWPFParagraph titleIndent = document.createParagraph();
            XWPFRun titleRun2 = titleIndent.createRun();
            titleRun2.setText("\n\n");
            titleRun2.setBold(true);
            titleRun2.setFontSize(20);

            // Создание таблицы
            XWPFTable table = document.createTable(transactions.size() + 1, 7);
            table.setWidth("90%");

            // Заголовки таблицы
            String[] headers = {"ID", "Автор", "Товар", "Дата создания", "Цена (руб.)", "Количество", "Сумма (руб.)"};
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.getCell(i).setText(headers[i]);
            }

            long sum = 0;
            int avgQuantity = 0;
            long avgPrice = 0;
            HashMap<String,Integer> mostOperator= new HashMap<>();

            // Добавление данных в таблицу
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                XWPFTableRow row = table.getRow(i + 1);
                row.getCell(0).setText(transaction.getId().toString());
                row.getCell(1).setText(transaction.getAuthor().getName());
                row.getCell(2).setText(transaction.getPayableItem().getTitle());
                row.getCell(3).setText(transaction.getDate_created().toString());
                row.getCell(4).setText(decimalFormat.format(transaction.getPayableItem().getPrice()));
                row.getCell(5).setText(decimalFormat.format(transaction.getQuantity()));
                long totalPrice = (long) (transaction.getQuantity() * transaction.getPayableItem().getPrice());
                row.getCell(6).setText(decimalFormat.format(totalPrice));

                sum += totalPrice;
                avgQuantity += transaction.getQuantity();
                avgPrice += transaction.getPayableItem().getPrice();
                mostOperator.merge(transaction.getAuthor().getName(),1,Integer::sum);
            }
            String operator ="";
            int max = 0;
            for (Map.Entry<String, Integer> entry : mostOperator.entrySet()) {
                if (entry.getValue()>=max) {
                    max = entry.getValue();
                    operator = entry.getKey();
                }
            }
            // Итоги
            XWPFParagraph itogParagraph = document.createParagraph();
            itogParagraph.createRun().setText("Итого: " + decimalFormat.format(sum) + " руб.");
            XWPFParagraph avgPriceParagraph = document.createParagraph();
            avgPriceParagraph.createRun().setText("Средняя цена: " + decimalFormat.format(avgPrice / transactions.size()) + " руб.");
            XWPFParagraph mostOperatorParagraph = document.createParagraph();
            mostOperatorParagraph.createRun().setText("Лучший оператор: " + operator);
            XWPFParagraph mostQuantityParagraph = document.createParagraph();
            mostQuantityParagraph.createRun().setText("Лучшее количество продаж оператором за период: " + max);

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

            // Заголовки таблицы
            String[] headers = {"ID", "Название", "Описание", "Цена (руб.)","Удален"};
            // Создание таблицы
            XWPFTable table = document.createTable(items.size() + 1, headers.length);
            table.setWidth("90%");

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
                String sd="Нет";
                if(item.getDeleted()){
                    sd = "Да";
                }
                row.getCell(4).setText(sd);
                totalPrice += item.getPrice();
            }

            // Добавление итогов
            XWPFParagraph totalParagraph = document.createParagraph();
            totalParagraph.createRun().setText("Средняя цена: " + decimalFormat.format(totalPrice/items.size()) + " руб.");

            // Добавление даты
            XWPFParagraph dateParagraph = document.createParagraph();
            dateParagraph.setAlignment(ParagraphAlignment.RIGHT);
            DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy");
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
