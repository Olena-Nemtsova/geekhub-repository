package edu.geekhub.homework.products;

import edu.geekhub.homework.categories.Category;
import edu.geekhub.homework.categories.interfaces.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    private final CategoryRepository categoryRepository;

    public ProductValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product was null");
        }
        validateName(product.getName());
        validatePrice(product.getPrice());
        validateProductCategory(product.getCategoryId());
        validateQuantity(product.getQuantity());
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Product quantity was less than zero");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Product name was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Product name was empty");
        }
        if (name.length() > 50 || name.length() < 2) {
            throw new IllegalArgumentException("Product name had wrong length");
        }
    }

    private void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Product price was equals or less than zero");
        }

        String numbersAfterPoint = Double.toString(price).split("\\.")[1];
        if (numbersAfterPoint.length() > 2) {
            throw new IllegalArgumentException("Product price had too much numbers after point");
        }
    }

    private void validateProductCategory(int categoryId) {
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Product had not exists category id");
        }
    }
}
