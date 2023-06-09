package edu.geekhub.homework.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    private ProductRepositoryImpl productRepository;
    private final Product product = new Product("Milk", 45.6, 1);
    @Mock
    private ProductValidator productValidator;
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(jdbcTemplate, productValidator);
    }

    @Test
    void can_not_add_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.addProduct(null)
        );
    }

    @Test
    void can_not_add_not_added_at_database() {
        doNothing().when(productValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> productRepository.addProduct(product)
        );
    }

    @Test
    void can_add_product() {
        doNothing().when(productValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> productRepository.addProduct(product)
        );
    }

    @Test
    void can_not_update_to_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(productValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productRepository.updateProductById(null, 1)
        );
    }

    @Test
    void can_not_update_not_updated_at_database() {
        doNothing().when(productValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> productRepository.updateProductById(product, 1)
        );
    }

    @Test
    void can_update_product() {
        doNothing().when(productValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> productRepository.updateProductById(product, 1)
        );
    }

    @Test
    void can_not_delete_product_not_deleted_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> productRepository.deleteProductById(1)
        );
    }

    @Test
    void can_delete_product_by_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> productRepository.deleteProductById(1)
        );
    }

    @Test
    void can_get_products() {
        List<Product> products = List.of(product);
        doReturn(products).when(jdbcTemplate).query(anyString(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProducts()
        );
        assertEquals(products, productRepository.getProducts());
    }

    @Test
    void can_get_product_by_id() {
        doReturn(List.of(product))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductById(1)
        );
        assertEquals(product, productRepository.getProductById(1));
    }

    @Test
    void can_get_null_product_by_wrong_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductById(1)
        );
        assertNull(productRepository.getProductById(1));
    }

    @Test
    void can_get_products_orders_sorted_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(products)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductsOrdersSortedWithPagination(1, 1, List.of(1))
        );
        assertEquals(
            products,
            productRepository.getProductsOrdersSortedWithPagination(1, 1, List.of(1))
        );
    }

    @Test
    void can_get_products_name_sorted_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(products)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductsNameSortedWithPagination(1, 1, List.of(1))
        );
        assertEquals(
            products,
            productRepository.getProductsNameSortedWithPagination(1, 1, List.of(1))
        );
    }

    @Test
    void can_get_products_rating_sorted_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(products)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductsRatingSortedWithPagination(1, 1, List.of(1))
        );
        assertEquals(
            products,
            productRepository.getProductsRatingSortedWithPagination(1, 1, List.of(1))
        );
    }

    @Test
    void can_get_products_price_sorted_with_pagination() {
        List<Product> products = List.of(product);
        doReturn(products)
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productRepository.getProductsPriceSortedWithPagination(1, 1, List.of(1))
        );
        assertEquals(
            products,
            productRepository.getProductsPriceSortedWithPagination(1, 1, List.of(1))
        );
    }
}
