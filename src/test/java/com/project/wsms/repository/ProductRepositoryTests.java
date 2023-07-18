package com.project.wsms.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.project.wsms.model.Product;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void ProductRepository_SaveAll_ReturnSavedProduct() {

        //Arrange
        Product product = Product.builder()
                .productName("Test")
                .barcode("test")
                .weight(Float.valueOf(100))
                .categories(null)
                .link("link")
                .note("note")
                .items(new HashSet<>())
                .build();

        //Act
        Product savedProduct = productRepository.save(product);

        //Assert
        Assertions.assertNotNull(savedProduct);
        Assertions.assertNotEquals(savedProduct.getId(),0);
    }

    @Test
    public void ProductRepository_FindById_ReturnProduct() {
        Product product = Product.builder()
                .productName("Test")
                .barcode("test")
                .weight(Float.valueOf(100))
                .categories(null)
                .link("link")
                .note("note")
                .items(new HashSet<>())
                .build();

        productRepository.save(product);

        Product productList = productRepository.findById(product.getId()).get();

        Assertions.assertNotNull(productList);
    }

    @Test
    public void ProductRepository_FindByKeyword_ReturnProductNotNull() {
        Product product = Product.builder()
                .productName("Test")
                .barcode("test")
                .weight(Float.valueOf(100))
                .categories(null)
                .link("link")
                .note("note")
                .items(new HashSet<>())
                .build();

        productRepository.save(product);

        List<Product> productList = productRepository.findByKeyword("Tes");

        assertNotNull(productList);
    }

    @Test
    public void ProductRepository_UpdateProduct_ReturnProductNotNull() {
        Product product = Product.builder()
                .productName("Test")
                .barcode("test")
                .weight(Float.valueOf(100))
                .categories(null)
                .link("link")
                .note("note")
                .items(new HashSet<>())
                .build();

        productRepository.save(product);

        Product productSave = productRepository.findById(product.getId()).get();
        productSave.setProductName("Tset");
        productSave.setWeight(Float.valueOf(200));

        Product updatedProduct = productRepository.save(productSave);

        Assertions.assertEquals(updatedProduct.getProductName(),"Tset");
        Assertions.assertEquals(updatedProduct.getWeight(), Float.valueOf(200));
    }

    @Test
    public void ProductRepository_ProductDelete_ReturnProductIsEmpty() {
        Product product = Product.builder()
                .productName("Test")
                .barcode("test")
                .weight(Float.valueOf(100))
                .categories(null)
                .link("link")
                .note("note")
                .items(new HashSet<>())
                .build();

        productRepository.save(product);

        productRepository.deleteById(product.getId());
        Optional<Product> productReturn = productRepository.findById(product.getId());

        Assertions.assertTrue(productReturn.isEmpty());
    }
}
