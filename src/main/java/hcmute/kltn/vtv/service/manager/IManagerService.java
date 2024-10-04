package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerService {
    ManagerResponse getManagerByUserName(String username);

    ManagerPageResponse getManagerPageByUsernameAddedAndStatus(String usernameAdded, Status status, int page, int size);

    ManagerPageResponse getManagerPageByStatus(Status status, int page, int size);

    @Transactional
    ManagerResponse deleteManager(String usernameAdded, Long managerId);

    void checkRequestPageParams(int page, int size);
}
