package org.example.finassistant.controller;

import org.example.finassistant.dto.DeleteResponseMessage;
import org.example.finassistant.dto.TransactionDTO;
import org.example.finassistant.exception.AcsessForbiddenException;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Message;
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.Transaction;
import org.example.finassistant.service.TransactionService;
import org.example.finassistant.service.UserService;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/t/get_by_id")
    public Transaction getByID(@RequestParam(name = "id") Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping(value = "/t/get_all")
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @PostMapping(value = "/t/add")
    public Transaction addRow(@RequestBody Transaction t) {
        validateUser(t);
        return transactionService.addRow(t);
    }

    @PatchMapping(value = "/t/edit")
    public Transaction editRow(@RequestBody Transaction t) {
        validateUser(t);
        return transactionService.editTransaction(t);
    }

    @DeleteMapping(value = "/t/delete")
    public DeleteResponseMessage delete(@RequestBody TransactionDTO transaction) {
        validateUser(transaction);
        System.out.println(transaction.getAuthor()+" # "+transaction.getId());
        return new DeleteResponseMessage(transactionService.delete(transaction));
    }

    @RequestMapping(value = "/t/pdf_report", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> itemsPdfReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                              @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {

        List<Transaction> items = transactionService.getAllBetween(startDate, endDate);

        ByteArrayInputStream bis = GeneratePdfReport.transactionReportPdf(items, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TranasctionReport.pdf"); //attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/t/csv_report", method = RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<InputStreamResource> suppliesCsvReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                                 @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {
        List<Transaction> items = transactionService.getAllBetween(startDate, endDate);

        ByteArrayInputStream bis = GenerateCsvReport.transactionReport(items, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TransactionReport.csv"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/t/docx_report", method = RequestMethod.GET,
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> suppliesDocxReport(@RequestParam(name = "startDate") LocalDateTime startDate,
                                                                  @RequestParam(name = "endDate") LocalDateTime endDate) throws IOException {
        List<Transaction> items = transactionService.getAllBetween(startDate, endDate);

        ByteArrayInputStream bis = GenerateDocxReport.transactionReportDocx(items, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TransactionReport.docx"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML)
                .body(new InputStreamResource(bis));
    }
    private void validateUser(Transaction transaction) {
        if (transaction.getAuthor() != null) {
            userService.findByID(transaction.getAuthor().getId()).orElseThrow(() -> new AcsessForbiddenException("Need to signed in"));
        }else {
            throw new DataNotFoundException("Missing author");
        }
    }
    private void validateUser(TransactionDTO transaction) {
        if (transaction.getAuthor() != 0) {
            userService.findByID(transaction.getAuthor()).orElseThrow(() -> new AcsessForbiddenException("Need to signed in"));
        }else {
            throw new DataNotFoundException("Missing author");
        }
    }

}
