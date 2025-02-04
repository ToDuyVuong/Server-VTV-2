package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateDeliverWorkRequest;
import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ListDeliverResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerDeliverService {




    @Transactional
    void addNewDeliverWithProviderRegister(Deliver deliver);

    DeliverResponse getDeliverById(String username, Long deliverId);

    @Transactional
    DeliverResponse addNewDeliverByManager(DeliverRequest request, String usernameAdded);


    @Transactional
    DeliverResponse addNewDeliverManagerByProvider(DeliverRequest request, String usernameAdded);

    @Transactional
    DeliverResponse updateDeliverWork( Long deliverId, UpdateDeliverWorkRequest request, String usernameAdded);



    @Transactional
    DeliverResponse updateStatusDeliver(Long deliverId, Status status, String usernameAdded);

    ListDeliverResponse getListDeliverByStatus (String username, Status status);


    ListDeliverResponse getListDeliverByStatusAndTypeWork(String username, Status status, TypeWork typeWork);
}
