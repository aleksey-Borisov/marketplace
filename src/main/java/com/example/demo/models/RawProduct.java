package com.example.demo.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawProduct {

    @NotBlank(message = "Название товара обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов")
    private String name;


    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;


    @Positive(message = "Цена должна быть положительным числом")
    @Max(value = 1000000, message = "Цена не может превышать 1 000 000")
    private int price;


    @NotBlank(message = "Категория товара обязательна для заполнения")
    @Size(min = 2, max = 50, message = "Категория должна содержать от 2 до 50 символов")
    private String category;
}