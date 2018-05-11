package net.mlgmag.Spring_Crud.repository;

import net.mlgmag.Spring_Crud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByName(String name);

}
