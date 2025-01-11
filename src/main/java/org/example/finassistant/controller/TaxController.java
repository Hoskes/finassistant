package org.example.finassistant.controller;

import org.example.finassistant.model.Tax;
import org.example.finassistant.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class TaxController {
    @Autowired
    private TaxService taxService;

    @GetMapping(value = "/tax/get_current")
    @CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
    public Tax getCurrent(){
        return taxService.getCurrentTax();
    }
    @GetMapping(value = "/tax/set_current")
    public ResponseEntity<String> setCurrentTax(@RequestParam(name = "id") Long id){
        return new ResponseEntity<>(taxService.changeTax(id), HttpStatus.OK);
    }
}
