package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListProvinceResponse extends ResponseAbstract {

        private int count;
        private List<ProvinceDTO> provinceDTOs;


        public static ListProvinceResponse listProvinceResponse(List<ProvinceDTO> provinceDTOs) {
            ListProvinceResponse response = new ListProvinceResponse();
            response.setProvinceDTOs(provinceDTOs);
            response.setCount(provinceDTOs.size());
            response.setCode(200);
            response.setMessage("Lấy danh sách tỉnh thành phố thành công.");
            response.setStatus("OK");
            return response;
        }
}
