package net.mlgmag.Spring_Crud.service;

import lombok.extern.slf4j.Slf4j;
import net.mlgmag.Spring_Crud.definition.ManufacturerService;
import net.mlgmag.Spring_Crud.model.Manufacturer;
import net.mlgmag.Spring_Crud.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void save(Manufacturer manufacturer) {
        log.info("IN ManufacturerServiceImpl save {}", manufacturer);
        manufacturerRepository.save(manufacturer);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void update(Manufacturer manufacturer) {
        log.info("IN ManufacturerServiceImpl update {}", manufacturer);
        manufacturerRepository.saveAndFlush(manufacturer);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Manufacturer manufacturer) {
        log.info("IN ManufacturerServiceImpl delete {}", manufacturer);
        manufacturerRepository.delete(manufacturer);
    }

    @Override
    public Optional<Manufacturer> findById(UUID uuid) {
        log.info("IN ManufacturerServiceImpl findById {}", uuid);
        return manufacturerRepository.findById(uuid);
    }

    @Override
    public List<Manufacturer> findAll() {
        log.info("IN ManufacturerServiceImpl findAll {}");
        return manufacturerRepository.findAll();
    }

    @Override
    public Optional<Manufacturer> findByName(String name) {
        log.info("IN ManufacturerServiceImpl findByName {}", name);
        return manufacturerRepository.findByName(name);
    }

    @Override
    public Boolean manufacturerNameValidation(String name, Model model) {

        if (findByName(name).isPresent()) {
            String error = "Manufacturer name already exist";
            log.info("IN ManufacturerServiceImpl manufacturerNameValidation {} ->", "Validation failed: " + error);
            model.addAttribute("DuplicateManufacturer", error);
            return true;
        }

        return false;
    }

    @Override
    public Boolean saveValidation(Manufacturer manufacturer, Model model) {
        return manufacturerNameValidation(manufacturer.getName(), model);
    }

    @Override
    public Boolean updateValidation(Manufacturer manufacturer, Model model) {

        if (!manufacturer.getName().equals(findById(manufacturer.getId()).map(Manufacturer::getName).orElse(null))) {
            return manufacturerNameValidation(manufacturer.getName(), model);
        }

        return false;
    }

}
