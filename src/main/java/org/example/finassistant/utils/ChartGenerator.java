package org.example.finassistant.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ChartGenerator {
    public static JFreeChart generateChart(Map<LocalDate, BigDecimal[]> reportData){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<LocalDate, BigDecimal[]> entry : reportData.entrySet()) {
            dataset.addValue(entry.getValue()[0], "Закупки", entry.getKey());
            dataset.addValue(entry.getValue()[1], "Продажи", entry.getKey());
        }
        JFreeChart chart = ChartFactory.createBarChart("Сравнительная характеристика доходов/расходов", "Период", "Сумма", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBorderPaint(Color.WHITE);
        return chart;

    }
}
