package hcmute.kltn.vtv.model.data.manager.request;

import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class RegisterManagerShippingRequest {


    private RegisterRequest registerRequest;
}
