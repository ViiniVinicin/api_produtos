package br.com.gerenciamento.api_produtos.repository;

import br.com.gerenciamento.api_produtos.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {


    private final List<Product> productList = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public List<Product> findAllProducts() {
        return new ArrayList<>(productList);
    }

    public Optional<Product> findProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            product.setId(idCounter.incrementAndGet());
            productList.add(product);
            return product;
        } else {
            findProductById(product.getId()).ifPresent(existingProduct -> {
                productList.remove(existingProduct);
                productList.add(product);
            });
            return product;
        }
    }

    /**
     * Deleta um produto pelo seu ID.
     */
    public void deleteProductById(Long id) {
        productList.removeIf(product -> product.getId().equals(id));
    }
}