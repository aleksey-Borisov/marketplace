package com.example.demo.repositories;

import com.example.demo.models.RawProduct;
import com.example.demo.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> Product.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .price(rs.getInt("price"))
            .category(rs.getString("category"))
            .build();

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, id);
    }

    public int save(Product product) {
        String sql = "INSERT INTO products (name, description, price, category) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory());
    }

    public int update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getId());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        return jdbcTemplate.query(sql, productRowMapper, category);
    }

    public List<Product> findByPriceBetween(int minPrice, int maxPrice) {
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, productRowMapper, minPrice, maxPrice);
    }

    public List<Product> findByCategoryAndPriceBetween(String category, int minPrice, int maxPrice) {
        String sql = "SELECT * FROM products WHERE category = ? AND price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, productRowMapper, category, minPrice, maxPrice);
    }
}