package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;

    private String name;

    private String image;

    private String description;

    private boolean child;

    private Status status;

    private Long parentId;


    public static CategoryDTO convertEntityToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setChild(category.isChild());
        categoryDTO.setStatus(category.getStatus());
        categoryDTO.setParentId(!category.isChild() ? null : category.getParent().getCategoryId());

        return categoryDTO;
    }

    public static List<CategoryDTO> convertEntitiesToDTOs(List<Category> categories) {
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOs.add(convertEntityToDTO(category));
        }
//        categoryDTOs.sort(Comparator.comparing(CategoryDTO::getName));
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Use the appropriate Locale for your case
        categoryDTOs.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));

        return categoryDTOs;
    }


    public static List<Long> convertEntitiesToIds(List<Category> categories) {
        List<Long> categoryIds = new ArrayList<>();
        for (Category category : categories) {
            categoryIds.add(category.getCategoryId());
        }
        return categoryIds;
    }


}
