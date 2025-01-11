package org.example.finassistant.service;

import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Tax;
import org.example.finassistant.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaxService {
    @Autowired
    private TaxRepository taxRepository;
    public Tax getCurrentTax(){
        return Optional.of(taxRepository.findByActualIsTrue()).orElseThrow(() -> new DataNotFoundException("NO object in db"));
    }
    @Transactional
    public String changeTax(Long id){
        taxRepository.resetAllActualToFalse();
        taxRepository.updateActualToTrueById(id);
        return "OK";
    }
}
