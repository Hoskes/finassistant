package org.example.finassistant.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.finassistant.dto.NameCountDTO;
import org.example.finassistant.model.Message;
import org.example.finassistant.model.Period;
import org.example.finassistant.service.ReportService;
import org.example.finassistant.utils.ChartGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/report")
    public void getChart(@RequestParam(name = "startDate") LocalDateTime startDate,
                         @RequestParam(name = "endDate") LocalDateTime endDate,
                         @RequestParam(name = "period") Period period,
                         HttpServletResponse response) throws IOException {
        Map<LocalDate, BigDecimal[]> reportData = reportService.getDualChart(startDate, endDate, period);
        JFreeChart chart = ChartGenerator.generateChart(reportData);
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "attachment; filename=\"chart.png\"");
        ChartUtils.writeChartAsPNG(response.getOutputStream(), chart, 1920, 1080);
    }
    @GetMapping(value = "/kpi_report")
    public List<NameCountDTO> getUserTransactionCounts() {
        return reportService.getUserTransactionCounts();
    }
    @GetMapping(value = "/day_sum")
    public Message nowSum(){
        return Message.builder().message(reportService.nowDaySum().toString()).build();
    }
    @GetMapping(value = "/top_five")
    public List<NameCountDTO> topFiveItems(){
        return reportService.getTopItems();
    }
    @GetMapping(value = "/cur_taxes")
    public Message getTaxes(){
        Double taxes = reportService.getTaxes();
        return Message.builder().message(taxes.toString()).build();
    }
    @GetMapping(value = "/cur_pros")
    public Message getPros(){
        return Message.builder().message(reportService.getPros().toString()).build();
    }
    @GetMapping(value = "/cur_cons")
    public Message getCons(){
        return Message.builder().message(reportService.getCons().toString()).build();
    }
    @GetMapping(value = "/cur_profit")
    public Message getProfit(){
        return Message.builder().message(reportService.getProfit().toString()).build();
    }
    @GetMapping("/sales/chart")
    public ResponseEntity<byte[]> generateChart(@RequestParam(name = "period") Period period,
                                                @RequestParam(name = "startDate") LocalDateTime startDate,
                                                @RequestParam(name = "endDate") LocalDateTime endDate) {
        Map<LocalDate,Long> reportData = reportService.getLinearData(period,startDate,endDate);
        try {
            byte[] chartBytes = ChartGenerator.generateLinearChart(reportData);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            log.info("Линейный график за "+startDate+" "+startDate+" "+endDate+" сформирован");
            return new ResponseEntity<>(chartBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}