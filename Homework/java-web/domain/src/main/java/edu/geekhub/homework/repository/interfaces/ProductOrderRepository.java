package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.ProductOrder;
import java.util.List;

public interface ProductOrderRepository {

    List<ProductOrder> getProductOrders();

    void addProductOrder(ProductOrder productOrder);
}
