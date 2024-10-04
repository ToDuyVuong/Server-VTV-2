package hcmute.kltn.vtv.service.admin;

import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.authentication.response.RegisterResponse;

public interface IAdminService {
    RegisterResponse register(RegisterRequest customerRequest);
}
