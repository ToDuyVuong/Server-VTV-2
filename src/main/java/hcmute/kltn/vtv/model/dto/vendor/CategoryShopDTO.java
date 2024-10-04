package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import lombok.*;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryShopDTO {

    private Long categoryShopId;

    private Long shopId;

    private String name;

    private String image;

    private int countProduct;

    private List<ProductDTO> productDTOs;



    public static CategoryShopDTO convertEntityToDTO(CategoryShop categoryShop) {
        CategoryShopDTO categoryShopDTO = new CategoryShopDTO();
        categoryShopDTO.setCategoryShopId(categoryShop.getCategoryShopId());
        categoryShopDTO.setShopId(categoryShop.getShop().getShopId());
        categoryShopDTO.setName(categoryShop.getName());
        categoryShopDTO.setImage(categoryShop.getImage());
        categoryShopDTO.setCountProduct(categoryShop.getProducts().size());

        return categoryShopDTO;
    }


    public static CategoryShopDTO convertDetailEntityToDTO(CategoryShop categoryShop) {
        CategoryShopDTO categoryShopDTO = convertEntityToDTO(categoryShop);
        categoryShopDTO.setProductDTOs(!categoryShop.getProducts().isEmpty() ? ProductDTO.convertEntitiesToDTOs(categoryShop.getProducts()) : new ArrayList<>());

        return categoryShopDTO;
    }


    public static List<CategoryShopDTO> convertEntitiesToDTOs(List<CategoryShop> categoryShops) {
        List<CategoryShopDTO> categoryShopDTOs = new ArrayList<>();
        for (CategoryShop categoryShop : categoryShops) {
            categoryShopDTOs.add(convertEntityToDTO(categoryShop));
        }
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Use the appropriate Locale for your case
        categoryShopDTOs.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));

        return categoryShopDTOs;
    }

}
