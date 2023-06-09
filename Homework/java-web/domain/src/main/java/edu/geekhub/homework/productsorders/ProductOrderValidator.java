package edu.geekhub.homework.productsorders;

import edu.geekhub.homework.orders.interfaces.OrderRepository;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderValidator {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductOrderValidator(ProductRepository productRepository,
                                 OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void validate(ProductOrder productOrder) {
        if (productOrder == null) {
            throw new IllegalArgumentException("ProductOrder was null");
        }
        validateProductExists(productOrder.productId());
        validateOrderExists(productOrder.orderId());
    }

    void validateOrderExists(int orderId) {
        if (orderRepository.getOrderById(orderId) == null) {
            throw new IllegalArgumentException("Order with id '" + orderId + "' doesn't exist");
        }
    }

    private void validateProductExists(int productId) {
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id '" + productId + "' doesn't exist");
        }
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Product with id '" + productId + "' out of stock");
        }
    }
}
