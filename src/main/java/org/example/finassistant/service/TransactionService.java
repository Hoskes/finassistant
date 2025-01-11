package org.example.finassistant.service;


import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Tax;
import org.example.finassistant.model.Transaction;
import org.example.finassistant.model.User;
import org.example.finassistant.repository.TaxRepository;
import org.example.finassistant.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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
        for (Transaction tr:t){
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


//        Tax tax = taxRepository.findById(t.getTax().getId()).orElseThrow(() -> new DataNotFoundException("Tax not found"));
        Tax tax = taxRepository.findByActualIsTrue();
        t1.setTax(tax);

        t1.setQuantity(t.getQuantity());
        t1.setDate_created(l);
        t1.setDate_edited(l);
        Item i = new Item();
        i.setId(t.getPayableItem().getId());
        t1.setPayableItem(i);

        System.out.println("#"+t1.getTax().getId());
        return transactionRepository.save(t1);
    }

    public Transaction editTransaction(Transaction t) {
        if(t.getId()==null){
            throw new DataNotFoundException("Missing id statement");
        }
        Transaction oldT = transactionRepository.findById(t.getId()).orElseThrow(() -> new DataNotFoundException("No Data Found"));
        if (t.getQuantity() != 0) {
            oldT.setQuantity(t.getQuantity());
        }
        LocalDateTime l = LocalDateTime.now();
        oldT.setDate_edited(l);
        if (!(t.getTax()==null)) {
            oldT.setTax(t.getTax());
        }
        if (t.getAuthor() != null) {
            if (t.getAuthor().getId() != null) {
                User author = User.builder().id(t.getId()).build();
                oldT.setAuthor(author);
            }else{
                throw new DataNotFoundException("Bad author ID");
            }
        }
        if (t.getPayableItem() != null) {
            if (t.getPayableItem().getId() != null) {
                Item i = Item.builder().id(t.getPayableItem().getId()).build();
                oldT.setPayableItem(i);
            }else{
                throw new DataNotFoundException("Bad item ID");
            }
        }
        return transactionRepository.saveAndFlush(oldT);
    }
    public String delete(Long id){
        Transaction tr = transactionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Transaction not found"));
        transactionRepository.delete(tr);
        return "Object deleted";
    }
}
