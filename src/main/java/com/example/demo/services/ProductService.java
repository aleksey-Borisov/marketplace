package com.example.demo.services;

import com.example.demo.models.RawProduct;
import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private int nextId = 1;
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public void createProduct(RawProduct rawProduct) {
        nextId++;
        Product product = new Product(nextId, rawProduct.getName(), rawProduct.getDescription(),
                rawProduct.getPrice(), rawProduct.getCategory());
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> filterProducts(String category, int minPrice, int maxPrice) {
        // Проверяем валидность диапазона цен
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Минимальная цена не может быть больше максимальной");
        }

        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
    }
    }
