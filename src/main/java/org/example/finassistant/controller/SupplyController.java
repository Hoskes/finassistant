package org.example.finassistant.controller;

import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Supply;
import org.example.finassistant.service.SupplyService;
import org.hibernate.mapping.Array;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SupplyController {
    @Autowired
    private SupplyService supplyService;
    @GetMapping(value = "/supply/get_by_id")
    public Supply getById(@RequestParam(name = "id") Long id){
        System.out.println(id);
        Supply supply = supplyService.getById(id);
        if(supply==null){
            throw new DataNotFoundException("No data found with that ID");
        }
//      не работает разобраться че не так
        return supply;
    }
    @GetMapping(value = "/supply/get_all")
    public ArrayList<Supply> getAll(){
        return new ArrayList<>(supplyService.getAll());
    }
    @PostMapping(value = "/supply/add")
    public Supply addRow(@RequestBody Supply supply){
        return supplyService.addSupply(supply);
    }
    @PatchMapping(value = "/supply/edit")
    public Supply editRow(@RequestBody Supply supply){
        return supplyService.editSupply(supply);
    }
    @DeleteMapping(value = "/supply/delete")
    public ResponseEntity<String> deleteRow(@RequestBody Supply supply){
        return new ResponseEntity<>(supplyService.deleteSupply(supply), HttpStatus.OK);
    }

}
