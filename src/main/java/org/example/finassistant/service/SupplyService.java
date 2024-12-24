package org.example.finassistant.service;

import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Supply;
import org.example.finassistant.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplyService {
    @Autowired
    private SupplyRepository supplyRepository;
    public Supply addSupply(Supply supply){
        LocalDateTime l = LocalDateTime.now();
        Supply s = Supply.builder()
                .title(supply.getTitle())
                .description(supply.getDescription())
                .price(supply.getPrice())
                .date_created(l)
                .date_edited(l).build();
        return supplyRepository.save(s);
    }
    public Supply editSupply(Supply s){
        Supply oldSupply = Optional.of(supplyRepository.getReferenceById(s.getId()))
                .orElseThrow(()->new DataNotFoundException("No Data ID"));
        LocalDateTime l = LocalDateTime.now();
        if(!(s.getTitle() ==null)){
            oldSupply.setTitle(s.getTitle());
            oldSupply.setDate_edited(l);
        }
        if(!(s.getDescription()==null)){
            oldSupply.setDescription(s.getDescription());
            oldSupply.setDate_edited(l);
        }
        if(!(s.getPrice() == 0.0d)){
            oldSupply.setPrice(s.getPrice());
            oldSupply.setDate_edited(l);
        }
        return supplyRepository.saveAndFlush(oldSupply);
    }
    public String deleteSupply(Supply supply){
        Supply s = Optional.of(supplyRepository.getReferenceById(supply.getId()))
                .orElseThrow(()->new DataNotFoundException("No Data ID"));
        supplyRepository.delete(s);
        return "Object deleted";
    }

    public List<Supply> getAll() {
        return supplyRepository.findAll();
    }

    public Supply getById(Long id) {
        return supplyRepository.getReferenceById(id);
    }
}
