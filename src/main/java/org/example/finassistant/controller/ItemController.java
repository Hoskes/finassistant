package org.example.finassistant.controller;

import org.example.finassistant.dto.ItemDTO;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Item;
import org.example.finassistant.model.Tax;
import org.example.finassistant.service.ItemService;
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
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class ItemController {
    @Autowired
    private ItemService itemService;
    @GetMapping(value = "/item/get_by_id")
    public ItemDTO getById(@RequestParam(name="id") Long id){
        return itemService.getById(id);
    }
    @GetMapping(value = "/item/get_all")
    public List<Item> getAll(){
        return itemService.getAll();
    }
    @PostMapping(value = "/item/add")
    public Item addItem(@RequestBody Item item){
        return itemService.addItem(item);
    }
    @PatchMapping(value = "/item/edit")
    public Item editItem( @RequestBody Item item){
        return itemService.editItem(item);

    }
    @DeleteMapping(value = "/item/delete")
    public ResponseEntity<String> deleteItem(@RequestBody Item item){
        return new ResponseEntity<>(itemService.deleteItem(item), HttpStatus.OK);
    }
    @RequestMapping(value = "/item/pdf_report", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> itemsPdfReport() throws IOException {

        List<Item> items = (List<Item>) itemService.getAll();

        ByteArrayInputStream bis = GeneratePdfReport.itemsReport(items);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ItemReport.pdf"); //attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @RequestMapping(value = "/item/csv_report", method = RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<InputStreamResource> suppliesCsvReport() throws IOException {
        List<Item> items = (List<Item>) itemService.getAll();

        ByteArrayInputStream bis = GenerateCsvReport.itemsReport(items);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ItemReport.csv"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN) // Убедитесь, что тип контента соответствует CSV
                .body(new InputStreamResource(bis));
    }
    @RequestMapping(value = "/item/docx_report", method = RequestMethod.GET,
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> suppliesDocxReport() throws IOException {
        List<Item> items = (List<Item>) itemService.getAll();

        ByteArrayInputStream bis = GenerateDocxReport.itemsReport(items);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ItemReport.docx"); // attachment в заголовок

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML) // Убедитесь, что тип контента соответствует DOCX
                .body(new InputStreamResource(bis));
    }
}
