package org.example.finassistant.service;

import lombok.extern.slf4j.Slf4j;
import org.example.finassistant.dto.SupplyDTO;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.mapper.SupplyMapper;
import org.example.finassistant.model.Supply;
import org.example.finassistant.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SupplyService {
    @Autowired
    private SupplyRepository supplyRepository;
    @Transactional
    public Supply addSupply(Supply supply){
        LocalDateTime l = LocalDateTime.now();
        Supply s = new Supply();
        s.setTitle(supply.getTitle());
        s.setDescription(supply.getDescription());
                s.setPrice(supply.getPrice());
                s.setQuantity(supply.getQuantity());
                s.setAuthor(supply.getAuthor());
                s.setDate_created(l);
                s.setDate_edited(l);
        System.out.println(s.getId());
        Supply z = supplyRepository.save(s);
        log.info("# Запись о закупке №"+z.getId()+" создана оператором №"+z.getAuthor().getId());
        return z;
    }
    @Transactional
    public Supply editSupply(Supply s){
        Supply oldSupply = supplyRepository.findById(s.getId()).orElseThrow(()->new DataNotFoundException("No Data ID"));
        LocalDateTime l = LocalDateTime.now();
        if(!(s.getTitle() ==null)){
            oldSupply.setTitle(s.getTitle());
            oldSupply.setDate_edited(l);
        }
        if(!(s.getAuthor() ==null)){
            oldSupply.setAuthor(s.getAuthor());
            oldSupply.setDate_edited(l);
        }
        if(!(s.getDescription()==null)){
            oldSupply.setDescription(s.getDescription());
            oldSupply.setDate_edited(l);
        }
        if(!(s.getPrice() == 0.0d)){
            oldSupply.setPrice(s.getPrice());
            oldSupply.setDate_edited(l);
        }if(!(s.getQuantity() == 0)) {
            oldSupply.setQuantity(s.getQuantity());
            oldSupply.setDate_created(l);
        }
        Supply z = supplyRepository.saveAndFlush(oldSupply);
        log.info("# Запись о закупке "+z.getId()+" изменена оператором №"+z.getAuthor().getId());
        return z;
    }
    @Transactional
    public String deleteSupply(Supply supply){
        Supply s = supplyRepository.findById(supply.getId()).orElseThrow(()->new DataNotFoundException("No Data founded"));
        supplyRepository.delete(s);
        log.info("# Запись о закупке "+s.getId()+" удалена пользователем №"+supply.getAuthor().getId());
        return "Object deleted";
    }

    public List<Supply> getAll() {
        return supplyRepository.findAll();
    }
    public List<Supply> getAllBetweeen(LocalDateTime dateStart,LocalDateTime dateEnd){
        return supplyRepository.getSuppliesByDate_createdBetween(dateStart,dateEnd);
    }

    public SupplyDTO getById(Long id) {
        Supply supply = supplyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No data with required ID found"));
        return SupplyMapper.INSTANCE.supplyToDTO(supply);
    }
}
