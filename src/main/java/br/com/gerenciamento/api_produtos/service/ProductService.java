package br.com.gerenciamento.api_produtos.service;

import br.com.gerenciamento.api_produtos.dto.ProductCreateDTO;
import br.com.gerenciamento.api_produtos.dto.ProductResponseDTO;
import br.com.gerenciamento.api_produtos.dto.ProductUpdateDTO;
import br.com.gerenciamento.api_produtos.exception.ResourceNotFoundException;
import br.com.gerenciamento.api_produtos.model.Product;
import br.com.gerenciamento.api_produtos.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        // Usando o nome padrão do repositório
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        return toResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        Product newProduct = new Product();
        mapDtoToEntity(newProduct, createDTO);

        Product savedProduct = productRepository.save(newProduct);
        return toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, @Valid ProductCreateDTO updateDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));

        mapDtoToEntity(existingProduct, updateDTO);

        Product updatedProduct = productRepository.save(existingProduct);
        return toResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));

        productRepository.deleteById(id);
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

    // Métodos de mapeamento unificados pelo mesmo nome (sobrecarga)
    private void mapDtoToEntity(Product product, ProductCreateDTO dto) {
        product.setProductName(dto.productName());
        product.setProductDescription(dto.productDescription());
        product.setProductCategory(dto.productCategory());
        product.setProductPrice(dto.productPrice());
        product.setProductStockQuantity(dto.productStockQuantity());
    }

    private void mapDtoToEntity(Product product, ProductUpdateDTO dto) {
        product.setProductName(dto.productName());
        product.setProductDescription(dto.productDescription());
        product.setProductCategory(dto.productCategory());
        product.setProductPrice(dto.productPrice());
        product.setProductStockQuantity(dto.productStockQuantity());
    }
}