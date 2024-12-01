package klx.tech.community.workshop.request;

import jakarta.validation.constraints.*;
import klx.tech.community.workshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest extends Product {
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @PositiveOrZero(message = "Discount must be zero or greater")
    private Double discount;

    @NotNull(message = "Favorite status must be specified")
    private Boolean favorite;

    @NotBlank(message = "Image cannot be empty")
    private String urlImage;
}
