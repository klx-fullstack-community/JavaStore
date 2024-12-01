package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.request.ProductRequest;
import klx.tech.community.workshop.services.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        List<ProductDTO> products = productService.findAllDTO();
        return ResponseEntity.ok(products); // Devuelve la lista de ProductDTO con estado 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return productService.findByIdDTO(id)
                .map(ResponseEntity::ok) // Devuelve el ProductDTO en la respuesta
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 si no existe
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.save(productRequest));
    }

}
