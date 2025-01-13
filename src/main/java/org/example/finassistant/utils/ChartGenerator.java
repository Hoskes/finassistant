package org.example.finassistant.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static byte[] generateLinearChart(Map<LocalDate, Long> reportData) throws IOException {
        TimeSeriesCollection dataset = createDataset(reportData);
        JFreeChart chart = createChart(dataset);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 800, 600);
            return baos.toByteArray();
        }
    }

    private static TimeSeriesCollection createDataset(Map<LocalDate, Long> reportData) {
        TimeSeries series = new TimeSeries("Товаров продано");

        for (Map.Entry<LocalDate, Long> entry : reportData.entrySet()) {
            LocalDate date = entry.getKey();
            Long totalSold = entry.getValue();
            // Добавляем данные в TimeSeries
            series.add(new Second(java.sql.Timestamp.valueOf(date.atStartOfDay())), totalSold.doubleValue());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private static JFreeChart createChart(TimeSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Линейный график объема продаж",
                "Дата",
                "Продано",
                dataset,
                true,
                true,
                false
        );

        // Настройка оси X для отображения дат
        DateAxis dateAxis = (DateAxis) chart.getXYPlot().getDomainAxis();
        dateAxis.setDateFormatOverride(new java.text.SimpleDateFormat("yyyy-MM-dd"));

        return chart;
    }
}
