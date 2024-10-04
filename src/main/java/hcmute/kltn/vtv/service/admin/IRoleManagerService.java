package hcmute.kltn.vtv.service.admin;

import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

public interface IRoleManagerService {
    @Transactional
    ManagerResponse addRoleManager(String username, String usernameCustomer);

    @Transactional
    ManagerResponse removeRoleManager(String username, String usernameCustomer);

    ManagerPageResponse getManagerPage(int page, int size);

    ManagerPageResponse getManagerPageByUsername(int page, int size, String username);

    ManagerPageResponse getManagerPageByStatus(int page, int size, Status status);

    void checkRequestPageParams(int page, int size);
}
