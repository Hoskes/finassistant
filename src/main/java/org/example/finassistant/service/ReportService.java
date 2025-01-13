package org.example.finassistant.service;

import org.example.finassistant.dto.NameCountDTO;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Period; // Импортируйте ваше перечисление
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.Tax;
import org.example.finassistant.model.Transaction;
import org.example.finassistant.repository.SupplyRepository;
import org.example.finassistant.repository.TaxRepository;
import org.example.finassistant.repository.TransactionRepository;
import org.example.finassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TaxRepository taxRepository;

    public Map<LocalDate, BigDecimal[]> getDualChart(LocalDateTime startDate, LocalDateTime endDate, Period period) {
        List<Supply> supplies = supplyRepository.getSuppliesByDate_createdBetween(startDate, endDate);
        List<Transaction> transactions = transactionRepository.getTransactionByDate_createdBetween(startDate, endDate);
        Map<LocalDate, BigDecimal[]> reportData = new HashMap<>();

        // Обработка данных по закупкам
        for (Supply supply : supplies) {
            LocalDate date = getPeriodDate(supply.getDate_created(), period);
            reportData.putIfAbsent(date, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            reportData.get(date)[0] = reportData.get(date)[0].add(BigDecimal.valueOf(supply.getPrice() * supply.getQuantity()));
        }

        // Обработка данных по продажам
        for (Transaction transaction : transactions) {
            LocalDate date = getPeriodDate(transaction.getDate_created(), period);
            reportData.putIfAbsent(date, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            reportData.get(date)[1] = reportData.get(date)[1].add(BigDecimal.valueOf(transaction.getQuantity() * transaction.getPayableItem().getPrice()));
        }

        return reportData;
    }
    private LocalDate getPeriodDate(LocalDateTime dateTime, Period period) {
        switch (period) {
            case DAY:
                return dateTime.toLocalDate();
            case MONTH:
                return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), 1);
            case YEAR:
                return LocalDate.of(dateTime.getYear(), 1, 1);
            default:
                throw new IllegalArgumentException("Unknown period: " + period);
        }
    }
    public List<NameCountDTO> getUserTransactionCounts() {
        List<Object[]> t = userRepository.findUserTransactionCounts();
        ArrayList<NameCountDTO> l = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            l.add(NameCountDTO.builder().name(t.get(i)[0].toString()).count(Long.parseLong(t.get(i)[1].toString())).build());
        }
        return l.stream().toList();
    }
    public Long nowDaySum(){
        Long a = transactionRepository.getDaySum();
        if(a == null){
            return 0l;
        }
        return a;
    }
    public List<NameCountDTO> getTopItems(){
        List<Object[]> obj = transactionRepository.getTop5Items();
        ArrayList<NameCountDTO> t = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {
            t.add(NameCountDTO.builder().name(obj.get(i)[0].toString()).count(Long.parseLong(obj.get(i)[1].toString())).build());
        }
        return t.stream().toList();
    }
    public Double getTaxes(){
        Long pros = transactionRepository.getQuartalPros();
        Long cons = supplyRepository.getQuartalCons();
        Tax t= taxRepository.findByActualIsTrue();
        if(t.getId()==1){
            return pros*0.01*t.getPersentage();
        } else if (t.getId()==2) {
            return (pros-cons)*0.01*t.getPersentage();
        }
        throw  new DataNotFoundException("Ошибка в налоговой ставке");
    }
    public Long getCons(){
        return supplyRepository.getQuartalCons();
    }
    public Long getPros(){
        return transactionRepository.getQuartalPros();
    }
    public Double getProfit(){
        return getPros()-getCons()-getTaxes();
    }

    public Map<LocalDate, Long> getLinearData(Period period,LocalDateTime dateStart,LocalDateTime dateEnd){
        String sPeriod = "";
        switch (period){
            case DAY -> sPeriod="day";
            case MONTH -> sPeriod="month";
            case YEAR -> sPeriod="year";
            case null, default -> throw new DataNotFoundException("Illegal Argument");
        }
        List<Object[]> list = transactionRepository.getLinearChartDataWithDate(sPeriod,dateStart,dateEnd);
        Map<LocalDate,Long> map = new HashMap<>();
        for (Object[] o:list){
            map.put((Timestamp.valueOf(o[0].toString()).toLocalDateTime().toLocalDate()),Long.parseLong(o[1].toString()));
        }
        return map;
    }
}