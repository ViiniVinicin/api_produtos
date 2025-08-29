package br.com.gerenciamento.api_produtos.service;

import br.com.gerenciamento.api_produtos.dto.ProductCreateDTO;
import br.com.gerenciamento.api_produtos.dto.ProductResponseDTO;
import br.com.gerenciamento.api_produtos.dto.ProductUpdateDTO;
import br.com.gerenciamento.api_produtos.exception.ResourceNotFoundException;
import br.com.gerenciamento.api_produtos.model.Product;
import br.com.gerenciamento.api_produtos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {

        List<Product> allProducts = productRepository.findAllProducts();

        return allProducts.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findProductById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
    }

    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        Product newProduct = new Product();
        mapDtoToEntity(newProduct, createDTO);

        Product savedProduct = productRepository.saveProduct(newProduct);
        return toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updateDTO) {
        Product existingProduct = findProductById(id);

        mapDtoToEntity(existingProduct, updateDTO);

        Product updatedProduct = productRepository.saveProduct(existingProduct);
        return toResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {

        findProductById(id);
        productRepository.deleteProductById(id);
    }

    private Product findProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductCategory(),
                product.getProductDescription()
        );
    }

    private void mapDtoToEntity(Product product, ProductCreateDTO createDTO) {
        product.setProductName(createDTO.productName());
        product.setProductPrice(createDTO.productPrice());
        product.setProductCategory(createDTO.productCategory());
        product.setProductDescription(createDTO.productDescription());
        product.setProductStockQuantity(createDTO.productStockQuantity());
    }

    private void mapDtoToEntity(Product product, ProductUpdateDTO updateDTO) {
        product.setProductName(updateDTO.productName());
        product.setProductPrice(updateDTO.productPrice());
        product.setProductCategory(updateDTO.productCategory());
        product.setProductDescription(updateDTO.productDescription());
        product.setProductStockQuantity(updateDTO.productStockQuantity());
    }
}