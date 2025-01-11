package org.example.finassistant.controller;

import org.example.finassistant.model.Message;
import org.example.finassistant.model.Transaction;
import org.example.finassistant.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @GetMapping(value = "/t/get_by_id")
    public Transaction getByID(@RequestParam(name = "id") Long id){
        return transactionService.getTransactionById(id);
    }

    @GetMapping(value = "/t/get_all")
    public List<Transaction> getAll(){
        return transactionService.getAll();
    }
    @PostMapping(value = "/t/add")
    public Transaction addRow(@RequestBody Transaction t){
        return transactionService.addRow(t);
    }
    @PatchMapping(value = "/t/edit")
    public Transaction editRow(@RequestBody Transaction t){
        return transactionService.editTransaction(t);
    }
    @DeleteMapping(value = "/t/delete")
    public Message delete(@RequestParam(name = "id") Long id){
        return new Message(transactionService.delete(id));
    }


}
