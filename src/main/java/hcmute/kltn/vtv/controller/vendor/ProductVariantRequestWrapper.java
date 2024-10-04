package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductVariantRequestWrapper {
    private List<ProductVariantRequest> requests;

    // getters and setters
    public List<ProductVariantRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<ProductVariantRequest> requests) {
        this.requests = requests;
    }
}
