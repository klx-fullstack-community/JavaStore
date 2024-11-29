package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.CartItemProduct;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.CartItemRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public List<ProductCartItemDTO> addProductToCartItem(ProductDTO productDTO) {
        // Constant ID for the CartItem (always working with cart_item ID = 1)
        final Long cartItemId = 1L;

        // Find the CartItem with ID = 1
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));

        // Check if the product exists in the products table
        Optional<Product> optionalProduct = productService.findById(productDTO.getId());
        if (optionalProduct.isEmpty()) {
            // If the product does not exist, return the current list without making changes
            return cartItem.getCartItemProducts().stream()
                    .map(cartItemProduct -> toProductCartItemDTO(cartItemProduct.getProduct(), cartItemProduct.getQuantity()))
                    .collect(Collectors.toList());
        }

        Product product = optionalProduct.get();

        // Find if the product already exists in the CartItem
        CartItemProduct existingCartItemProduct = cartItem.getCartItemProducts().stream()
                .filter(cartItemProduct -> cartItemProduct.getProduct().getId().equals(productDTO.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItemProduct != null) {
            // If the product exists, increment its quantity
            existingCartItemProduct.setQuantity(existingCartItemProduct.getQuantity() + 1);
        } else {
            // If the product does not exist in the CartItem, add it with quantity = 1
            CartItemProduct newCartItemProduct = new CartItemProduct();
            newCartItemProduct.setCartItem(cartItem);
            newCartItemProduct.setProduct(product);
            newCartItemProduct.setQuantity(1);
            cartItem.getCartItemProducts().add(newCartItemProduct);
        }

        // Save the updated CartItem
        cartItemRepository.save(cartItem);

        // Return the updated list of ProductCartItemDTO
        return cartItem.getCartItemProducts().stream()
                .map(cartItemProduct -> toProductCartItemDTO(cartItemProduct.getProduct(), cartItemProduct.getQuantity()))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<ProductCartItemDTO> deleteProductFromCartItem(Long productId) {
        // Constant ID for the CartItem (always working with cart_item ID = 1)
        final Long cartItemId = 1L;

        // Fetch the CartItem with ID = 1
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));

        // Find the product in the cart_item_product table
        CartItemProduct existingCartItemProduct = cartItem.getCartItemProducts().stream()
                .filter(cartItemProduct -> cartItemProduct.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingCartItemProduct != null) {
            if (existingCartItemProduct.getQuantity() > 1) {
                // Decrease quantity by 1
                existingCartItemProduct.setQuantity(existingCartItemProduct.getQuantity() - 1);
            } else {
                // Remove the product from the CartItem if quantity becomes 0
                cartItem.getCartItemProducts().remove(existingCartItemProduct);
            }
        }

        // Save the updated CartItem
        cartItemRepository.save(cartItem);

        // Return the updated list of ProductCartItemDTO
        return cartItem.getCartItemProducts().stream()
                .map(cartItemProduct -> toProductCartItemDTO(cartItemProduct.getProduct(), cartItemProduct.getQuantity()))
                .collect(Collectors.toList());
    }



    @Override
    public List<ProductCartItemDTO> findAll() {
    
    List<CartItem> cartItems = cartItemRepository.findAll();

    return cartItems.stream()
            .filter(cartItem -> !cartItem.getCartItemProducts().isEmpty()) // Exclude CartItems with no products
            .flatMap(cartItem -> cartItem.getCartItemProducts().stream())
            .map(cartItemProduct -> {
                Product product = cartItemProduct.getProduct();
                Integer quantity = cartItemProduct.getQuantity();
                return toProductCartItemDTO(product, quantity);
            })
            .collect(Collectors.toList());
    }

    private ProductCartItemDTO toProductCartItemDTO(Product product, Integer quantity) {
        ProductDTO productDTO = productService.toProductDTO(product);
        return ProductCartItemDTO.builder()
                .product(productDTO)
                .quantity(quantity)
                .build();
    }

}
