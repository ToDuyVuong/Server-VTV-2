package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.entity.vtv.Category;
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
public class BrandDTO {

    private Long brandId;

    private String name;

    private String image;

    private String description;

    private String information;

    private String origin;

    private boolean allCategories;

    private List<Long> categoryIds;


    public static BrandDTO convertEntityToDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandId(brand.getBrandId());
        brandDTO.setName(brand.getName());
        brandDTO.setImage(brand.getImage());
        brandDTO.setDescription(brand.getDescription());
        brandDTO.setInformation(brand.getInformation());
        brandDTO.setOrigin(brand.getOrigin());
        brandDTO.setAllCategories(brand.isAllCategories());
        brandDTO.setCategoryIds(brandDTO.isAllCategories() ? new ArrayList<>() :
                brand.getCategories().stream().map(Category::getCategoryId).toList());


        return brandDTO;
    }

    public static List<BrandDTO> convertEntitiesToDTOs(List<Brand> brands) {
        List<BrandDTO> brandDTOS = new ArrayList<>();
        for (Brand brand : brands) {
            brandDTOS.add(convertEntityToDTO(brand));
        }
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Use the appropriate Locale for your case
        brandDTOS.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));
        return brandDTOS;
    }
}
