package hcmute.kltn.vtv.model.dto.location;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeRegionDTO {
    private Integer administrativeRegionId;
    private String name;
    private String nameEn;
    private String codeName;
    private String codeNameEn;
}
