package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.request.ProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    List<ProductDTO> findAllDTO();
    Optional<ProductDTO> findByIdDTO(Long id);
    ProductDTO toProductDTO(Product product);
    ProductDTO save(ProductRequest productRequest);
}
