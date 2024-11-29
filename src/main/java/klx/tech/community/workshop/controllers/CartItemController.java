package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.services.CartItemService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<ProductCartItemDTO> showProductsInCartItem() {
        return cartItemService.findAll();
    }

    @PostMapping
    public ResponseEntity<List<ProductCartItemDTO>> addProductToCartItem(@RequestBody ProductDTO productDTO) {
        try {
            List<ProductCartItemDTO> updatedCartItems = cartItemService.addProductToCartItem(productDTO);
            return ResponseEntity.ok(updatedCartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ProductCartItemDTO>> deleteProductFromCartItem(@PathVariable Long id) {
        try {
            // Call the service method to delete the product and get the updated list
            List<ProductCartItemDTO> updatedCartItems = cartItemService.deleteProductFromCartItem(id);
            return ResponseEntity.ok(updatedCartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}
