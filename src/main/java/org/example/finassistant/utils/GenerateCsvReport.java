package org.example.finassistant.utils;

import org.example.finassistant.model.Item;
import org.example.finassistant.model.Supply;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

// хард код переписать
public class GenerateCsvReport {
    public static ByteArrayInputStream suppliesReport(List<Supply> supplies) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        DecimalFormat decimalFormat = new DecimalFormat("####.##");

        // Заголовки
        writer.println("ID,Название,Описание,Цена (руб.),Кол-во,Сумма (руб.)");

        // Заполнение данными
        long sum = 0;
        for (Supply supply : supplies) {
            long totalPrice = (long)(supply.getQuantity() * supply.getPrice());
            sum += totalPrice;
            writer.printf("%d,%s,%s,%s,%s,%s%n",
                    supply.getId(),
                    supply.getTitle(),
                    supply.getDescription(),
                    decimalFormat.format(supply.getPrice()),
                    decimalFormat.format(supply.getQuantity()),
                    decimalFormat.format(totalPrice));
        }

        // Итоги
        long avgQuantity = supplies.stream().mapToLong(Supply::getQuantity).sum() / supplies.size();
        double avgPrice = supplies.stream().mapToDouble(Supply::getPrice).sum() / supplies.size();
        writer.printf("Итого,%s,,,,%s%n", decimalFormat.format(sum), "");
        writer.printf("Средняя цена,%s,,,,%s%n", decimalFormat.format(avgPrice), "");
        writer.printf("Среднее кол-во,%s,,,,%s%n", decimalFormat.format(avgQuantity), "");

        // Дата
        writer.printf("Дата,%s,,,,%s%n", LocalDateTime.now().toLocalDate(), "");

        writer.flush();
        writer.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
    public static ByteArrayInputStream itemsReport(List<Item> items) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        DecimalFormat decimalFormat = new DecimalFormat("####.##");

        // Заголовки
        writer.println("ID,Название,Описание,Цена (руб.)");

        // Заполнение данными
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
            writer.printf("%d,%s,%s,%s%n",
                    item.getId(),
                    item.getTitle(),
                    item.getDescription(),
                    decimalFormat.format(item.getPrice()));
        }

        // Итоги
        double avgPrice = items.stream().mapToDouble(Item::getPrice).average().orElse(0.0);
        writer.printf("Итого,,,,%s%n", decimalFormat.format(totalPrice));
        writer.printf("Средняя цена,,,,%s%n", decimalFormat.format(avgPrice));

        // Дата
        writer.printf("Дата,,,,%s%n", LocalDateTime.now().toLocalDate());

        writer.flush();
        writer.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
