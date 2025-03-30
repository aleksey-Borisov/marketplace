package com.example.demo.services;

import com.example.demo.models.Product;
import com.example.demo.models.RawProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с товарами, объединяющий бизнес-логику и доступ к данным
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final JdbcTemplate jdbcTemplate;
    private int nextId = 1;

    // Маппер для преобразования строк ResultSet в объекты Product
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> Product.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .price(rs.getInt("price"))
            .category(rs.getString("category"))
            .build();

    /**
     * Получить список всех товаров
     */
    public List<Product> getAllProducts() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    /**
     * Найти товар по ID
     * @throws IllegalArgumentException если товар не найден
     */
    public Product getProductById(int id) {
        Product product = jdbcTemplate.queryForObject(
                "SELECT * FROM products WHERE id = ?",
                productRowMapper,
                id
        );
        if (product == null) {
            throw new IllegalArgumentException("Товар с ID " + id + " не найден");
        }
        return product;
    }

    /**
     * Создать новый товар
     * @return созданный товар с присвоенным ID
     */
    public Product createProduct(RawProduct rawProduct) {
        Product product = Product.builder()
                .id(nextId++)
                .name(rawProduct.getName())
                .description(rawProduct.getDescription())
                .price(rawProduct.getPrice())
                .category(rawProduct.getCategory())
                .build();

        jdbcTemplate.update(
                "INSERT INTO products (id, name, description, price, category) VALUES (?, ?, ?, ?, ?)",
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
        );

        return product;
    }

    /**
     * Обновить существующий товар
     * @throws IllegalArgumentException если товар не найден
     */
    public void updateProduct(Product product) {
        int updated = jdbcTemplate.update(
                "UPDATE products SET name = ?, description = ?, price = ?, category = ? WHERE id = ?",
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getId()
        );

        if (updated == 0) {
            throw new IllegalArgumentException("Товар с ID " + product.getId() + " не найден");
        }
    }

    /**
     * Удалить товар
     * @throws IllegalArgumentException если товар не найден
     */
    public void deleteProduct(int id) {
        int deleted = jdbcTemplate.update("DELETE FROM products WHERE id = ?", id);
        if (deleted == 0) {
            throw new IllegalArgumentException("Товар с ID " + id + " не найден");
        }
    }

    /**
     * Фильтрация товаров по параметрам
     * @throws IllegalArgumentException если параметры фильтрации некорректны
     */
    public List<Product> filterProducts(String category, Integer minPrice, Integer maxPrice) {
        validatePriceRange(minPrice, maxPrice);

        if (category != null && minPrice != null && maxPrice != null) {
            return jdbcTemplate.query(
                    "SELECT * FROM products WHERE category = ? AND price BETWEEN ? AND ?",
                    productRowMapper,
                    category,
                    minPrice,
                    maxPrice
            );
        } else if (category != null) {
            return jdbcTemplate.query(
                    "SELECT * FROM products WHERE category = ?",
                    productRowMapper,
                    category
            );
        } else if (minPrice != null && maxPrice != null) {
            return jdbcTemplate.query(
                    "SELECT * FROM products WHERE price BETWEEN ? AND ?",
                    productRowMapper,
                    minPrice,
                    maxPrice
            );
        }
        return getAllProducts();
    }

    private void validatePriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Минимальная цена не может быть больше максимальной");
        }
        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
    }
}