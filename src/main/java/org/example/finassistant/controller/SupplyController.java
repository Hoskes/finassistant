package org.example.finassistant.controller;

import org.example.finassistant.dto.DeleteResponseMessage;
import org.example.finassistant.dto.SupplyDTO;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Supply;
import org.example.finassistant.service.SupplyService;
import org.example.finassistant.utils.GenerateCsvReport;
import org.example.finassistant.utils.GenerateDocxReport;
import org.example.finassistant.utils.GeneratePdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//# TODO добавить отслеживание null-данных
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class SupplyController {
    @Autowired
    private SupplyService supplyService;
    @GetMapping(value = "/supply/get_by_id")
    public SupplyDTO getById(@RequestParam(name = "id") Long id){
        System.out.println(id);
        return supplyService.getById(id);
    }
    @GetMapping(value = "/supply/get_all")
    public ArrayList<Supply> getAll(){
        return new ArrayList<>(supplyService.getAll());
    }
    @PostMapping(value = "/supply/add")
    public Supply addRow(@RequestBody Supply supply){
        Supply s = supplyService.addSupply(supply);
        s.getAuthor().setPassword("");
        s.getAuthor().setEmail("");
        s.getAuthor().setRole("");
        return s;
    }
    @PatchMapping(value = "/supply/edit")
    public Supply editRow(@RequestBody Supply supply){
        Supply s = supplyService.editSupply(supply);
        s.getAuthor().setPassword("");
        s.getAuthor().setEmail("");
        s.getAuthor().setRole("");
        return s;
    }
    @DeleteMapping(value = "/supply/delete")
    public DeleteResponseMessage deleteRow(@RequestBody Supply supply){
        return new DeleteResponseMessage(supplyService.deleteSupply(supply));
    }
    @RequestMapping(value = "/supply/pdf_report", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> suppliesPdfReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                                 @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {

        List<Supply> supplies = (List<Supply>) supplyService.getAllBetweeen(startDate,endDate);
        if(supplies.isEmpty()){
            throw new DataNotFoundException("No data in period");
        }
        ByteArrayInputStream bis = GeneratePdfReport.suppliesReport(supplies,startDate,endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=supplyReport.pdf"); //attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @RequestMapping(value = "/supply/csv_report", method = RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<InputStreamResource> suppliesCsvReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                                 @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {
        List<Supply> supplies = (List<Supply>) supplyService.getAllBetweeen(startDate,endDate);
        if(supplies.isEmpty()){
            throw new DataNotFoundException("No data in period");
        }
        ByteArrayInputStream bis = GenerateCsvReport.suppliesReport(supplies);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=supplyReport.csv"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN) // Убедитесь, что тип контента соответствует CSV
                .body(new InputStreamResource(bis));
    }
    @RequestMapping(value = "/supply/docx_report", method = RequestMethod.GET,
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> suppliesDocxReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                                  @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {
        List<Supply> supplies = (List<Supply>) supplyService.getAllBetweeen(startDate,endDate);
        if(supplies.isEmpty()){
            throw new DataNotFoundException("No data in period");
        }
        ByteArrayInputStream bis = GenerateDocxReport.suppliesReport(supplies);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=supplyReport.docx"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML) // Убедитесь, что тип контента соответствует DOCX
                .body(new InputStreamResource(bis));
    }

}
