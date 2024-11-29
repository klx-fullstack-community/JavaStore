package klx.tech.community.workshop.services;

import java.util.List;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;

public interface CartItemService {

    List<ProductCartItemDTO> findAll();

    List<ProductCartItemDTO> addProductToCartItem(ProductDTO productDTO);

    List<ProductCartItemDTO> deleteProductFromCartItem(Long productId);

}
