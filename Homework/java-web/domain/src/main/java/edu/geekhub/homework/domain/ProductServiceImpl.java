package edu.geekhub.homework.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.tinylog.Logger;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    @Override
    public boolean containsProduct(Product product) {
        return productRepository.getProducts().contains(product);
    }

    @Override
    public boolean addProduct(Product product) {
        boolean successAdd = false;
        try {
            productValidator.validate(product);
            productRepository.addProduct(product);
            Logger.info("Product was added:\n" + product);
            successAdd = true;
        } catch (IllegalArgumentException exception) {
            Logger.warn("Product wasn't added: " + product + "\n" + exception.getMessage());
        }
        return successAdd;
    }

    @Override
    public boolean deleteProductById(int id) {
        Product product = productRepository.getProductById(id);
        boolean successDelete = productRepository.deleteProductById(id);

        if (successDelete) {
            Logger.info("Product was deleted:\n" + product);
        } else {
            Logger.warn("Product wasn't deleted:\nNo found product with id: " + id);
        }
        return successDelete;
    }

    @Override
    public boolean updateProductById(Product product, int id) {
        boolean successEditing = false;
        try {
            productValidator.validate(product);
            successEditing = productRepository.updateProductById(product, id);

            if (successEditing) {
                Logger.info("Product was edited:\n" + product);
            } else {
                Logger.warn("Product wasn't edited:\nNo found product with id: " + id);
            }
        } catch (IllegalArgumentException exception) {
            Logger.warn("Product wasn't edited: " + product + "\n" + exception.getMessage());
        }
        return successEditing;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public List<Product> getSortedByNameProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparing(Product::getName)));
        return sortedProducts;
    }

    @Override
    public List<Product> getSortedByPriceProducts() {
        List<Product> sortedProducts = new ArrayList<>(getProducts());
        sortedProducts.sort((Comparator.comparingDouble(Product::getPrice)));
        return sortedProducts;
    }
}