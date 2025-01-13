package org.example.finassistant.service;


import lombok.extern.slf4j.Slf4j;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.*;
import org.example.finassistant.repository.TaxRepository;
import org.example.finassistant.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TaxRepository taxRepository;

    public Transaction getTransactionById(Long id) {
        Transaction tr = transactionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No Transaction found"));
        tr.getAuthor().setPassword("");
        tr.getAuthor().setEmail("");
        return tr;
    }

    public List<Transaction> getAll() {
        List<Transaction> t = transactionRepository.findAll();
        for (Transaction tr : t) {
            tr.getAuthor().setPassword("");
            tr.getAuthor().setEmail("");
        }
        return t;
    }

    @Transactional
    public Transaction addRow(Transaction t) {
        Transaction t1 = new Transaction();
        LocalDateTime l = LocalDateTime.now();
        User author = new User();
        author.setId(t.getAuthor().getId());
        t1.setAuthor(author);
        Tax tax = taxRepository.findByActualIsTrue();
        t1.setTax(tax);
        t1.setQuantity(t.getQuantity());
        t1.setDate_created(l);
        t1.setDate_edited(l);
        Item i = new Item();
        i.setId(t.getPayableItem().getId());
        t1.setPayableItem(i);
        log.info("### Пользователь №"+author.getId()+" создал новую транзакцию");
        return transactionRepository.save(t1);
    }

    public Transaction editTransaction(Transaction t) {
        if (t.getId() == null) {
            throw new DataNotFoundException("Missing id statement");
        }
        Transaction oldT = transactionRepository.findById(t.getId()).orElseThrow(() -> new DataNotFoundException("No Data Found"));
        if (t.getQuantity() != 0) {
            oldT.setQuantity(t.getQuantity());
        }
        LocalDateTime l = LocalDateTime.now();
        oldT.setDate_edited(l);
        if (!(t.getTax() == null)) {
            oldT.setTax(t.getTax());
        }
        if (t.getAuthor() != null) {
            if (t.getAuthor().getId() != null) {
                User author = User.builder().id(t.getId()).build();
                oldT.setAuthor(author);
            } else {
                throw new DataNotFoundException("Bad author ID");
            }
        }
        if (t.getPayableItem() != null) {
            if (t.getPayableItem().getId() != null) {
                Item i = Item.builder().id(t.getPayableItem().getId()).build();
                oldT.setPayableItem(i);
            } else {
                throw new DataNotFoundException("Bad item ID");
            }
        }
        log.info("### Пользователь №"+oldT.getAuthor().getId()+" создал новую транзакцию");
        return transactionRepository.saveAndFlush(oldT);
    }

    public String delete(Long id) {
        Transaction tr = transactionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Transaction not found"));
        transactionRepository.delete(tr);
        log.info("### Транзакция  №"+tr.getId()+"удалена");
        return "Object deleted";
    }

    public List<Transaction> getAllBetween(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.getTransactionsByDate_createdBetween(start, end);
    }

}
