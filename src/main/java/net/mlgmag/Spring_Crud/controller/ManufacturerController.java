package net.mlgmag.Spring_Crud.controller;

import net.mlgmag.Spring_Crud.model.Manufacturer;
import net.mlgmag.Spring_Crud.service.genericService.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/manufacturer")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manufacturerAddPage(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("title", "Add Manufacturer");
        return "Manufacturer/manufacturerAdd";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manufacturerAdd(@ModelAttribute("manufacturer") Manufacturer manufacturer, Model model) {

        if (manufacturerService.validate(manufacturer, model)) {
            model.addAttribute("title", "Add Manufacturer");
            return "Manufacturer/manufacturerAdd";
        }

        manufacturerService.save(manufacturer);
        return "redirect:/manufacturer/list";
    }

    @GetMapping("/update/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manufacturerUpdatePage(@RequestParam(value = "id") UUID id, Model model) {
        model.addAttribute("manufacturer", manufacturerService.findById(id));
        model.addAttribute("title", "Edit Manufacturer");
        return "Manufacturer/manufacturerUpdate";
    }

    @PostMapping("/update/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manufacturerUpdate(@ModelAttribute("manufacturer") Manufacturer manufacturer, Model model) {

        if (!manufacturer.getName().equals(manufacturerService.findById(manufacturer.getId()).getName())) {
            if (manufacturerService.findByName(manufacturer.getName()) != null) {
                model.addAttribute("DuplicateManufacturer", "Manufacturer name already exist");
                model.addAttribute("title", "Edit Manufacturer");
                return "Manufacturer/manufacturerUpdate";
            }
        }

        manufacturerService.update(manufacturer);
        return "redirect:/manufacturer/list";
    }

    @GetMapping("/delete/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manufacturerDelete(@RequestParam(value = "id") UUID id) {
        manufacturerService.delete(manufacturerService.findById(id));
        return "redirect:/manufacturer/list";
    }

    @GetMapping("/")
    public String manufacturerView(@RequestParam(value = "id") UUID id, Model model) {
        model.addAttribute("manufacturer", manufacturerService.findById(id));
        model.addAttribute("products", manufacturerService.findById(id).getProducts());
        model.addAttribute("title", "Manufacturer");
        return "Manufacturer/manufacturerView";
    }

    @GetMapping("/list")
    public String manufacturersList(Model model) {
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("products", manufacturerService);
        model.addAttribute("title", "Manufacturers");
        System.out.println(manufacturerService.findAll());
        return "Manufacturer/manufacturersList";
    }

}
