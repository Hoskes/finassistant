package org.example.finassistant.service;

import org.example.finassistant.dto.ItemDTO;
import org.example.finassistant.dto.SupplyDTO;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.mapper.ItemMapper;
import org.example.finassistant.mapper.SupplyMapper;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Supply;
import org.example.finassistant.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Item addItem(Item item){
        Item i = new Item();
        i.setTitle(item.getTitle());
        i.setDescription(item.getDescription());
        i.setPrice(item.getPrice());
        return itemRepository.save(i);
    }
    public Item editItem(Item item){
        Item oldItem = itemRepository.findById(item.getId()).orElseThrow(() -> new DataNotFoundException("No Data Founded"));
        if(item.getTitle()!=null) {
            oldItem.setTitle(item.getTitle());
        }
        if(item.getDescription()!=null) {
            oldItem.setDescription(item.getDescription());
        }
        if(item.getPrice()!=0) {
            oldItem.setPrice(item.getPrice());
        }
        return itemRepository.saveAndFlush(oldItem);
    }
    public String deleteItem(Item item){
        Item s = itemRepository.findById(item.getId()).orElseThrow(()->new DataNotFoundException("No Data founded"));
        itemRepository.delete(s);
        return "Object deleted";
    }

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    public ItemDTO getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No data with required ID found"));
        return ItemMapper.INSTANCE.itemToDTO(item);
    }
}
