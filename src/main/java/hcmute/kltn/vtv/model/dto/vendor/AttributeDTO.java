package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.vendor.Attribute;
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
public class AttributeDTO {

    private Long attributeId;

    private String name;

    private String value;

    private boolean active;

    public static List<AttributeDTO> convertEntitiesToDTOs(List<Attribute> attributes) {
        List<AttributeDTO> attributeDTOS = new ArrayList<>();
        for (Attribute attribute : attributes) {
            AttributeDTO attributeDTO = convertEntityToDTO(attribute);
            attributeDTOS.add(attributeDTO);
        }
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Use the appropriate Locale for your case
        attributeDTOS.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));

        return attributeDTOS;
    }

    public static AttributeDTO convertEntityToDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setAttributeId(attribute.getAttributeId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        attributeDTO.setActive(attribute.isActive());

        return attributeDTO;
    }
}
