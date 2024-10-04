package hcmute.kltn.vtv.service.shipping.impl;


import hcmute.kltn.vtv.model.data.shipping.request.TransportHandleRequest;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.shipping.TransportHandle;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.repository.shipping.TransportHandleRepository;
import hcmute.kltn.vtv.repository.shipping.TransportRepository;
import hcmute.kltn.vtv.service.shipping.ITransportHandleService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportHandleServiceImpl implements ITransportHandleService {

    private final TransportHandleRepository transportHandleRepository;
    private final TransportRepository transportRepository;


    @Override
    @Transactional
    public void addNewTransportHandle(TransportHandleRequest transportHandleRequest) {
        TransportHandle transportHandle = createTransportHandleByRequest(transportHandleRequest);

        try {
            transportHandleRepository.save(transportHandle);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi tạo mới một bước xử lý vận chuyển mới!");
        }
    }


    @Override
    @Transactional
    public void addNewTransportHandleByOrderId(UUID orderId, String wardCode, String username, boolean handled, TransportStatus transportStatus) {
        Transport transport = getTransportByOrderId(orderId);
        transport.setStatus(transportStatus);

        TransportHandleRequest transportHandleRequest = TransportHandleRequest
                .createTransportHandleRequest(transport.getTransportId(), username, wardCode, true, handled, transportStatus);
        try {
            addNewTransportHandle(transportHandleRequest);

            transportRepository.save(transport);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn vận chuyển! " + e.getMessage());
        }
    }




    @Override
    public List<TransportHandle> getAllTransportHandleByTransportId(UUID transportId) {
        return transportHandleRepository.findAllByTransportTransportId(transportId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy bước xử lý vận chuyển với id: " + transportId));
    }


    private TransportHandle createTransportHandleByRequest(TransportHandleRequest request) {
        TransportHandle transportHandle = new TransportHandle();
        transportHandle.setTransport(getTransportById(request.getTransportId()));
        transportHandle.setUsername(request.getUsername());
        transportHandle.setWardCode(request.getWardCode());
        transportHandle.setActive(request.isActive());
        transportHandle.setHandled(request.isHandled());
        transportHandle.setTransportStatus(request.getTransportStatus());
        transportHandle.setCreateAt(LocalDateTime.now());
        transportHandle.setUpdateAt(LocalDateTime.now());

        return transportHandle;
    }

    private TransportHandle createTransportHandleByOrderId(UUID orderId, String wardCode, String username, boolean handled, TransportStatus status) {

        TransportHandle transportHandle = new TransportHandle();
        transportHandle.setTransport(getTransportByOrderId(orderId));
        transportHandle.setUsername(username);
        transportHandle.setWardCode(wardCode);
        transportHandle.setActive(true);
        transportHandle.setHandled(handled);
        transportHandle.setTransportStatus(status);
        transportHandle.setCreateAt(LocalDateTime.now());
        transportHandle.setUpdateAt(LocalDateTime.now());

        return transportHandle;
    }


    private Transport getTransportById(UUID transportId) {
        return transportRepository.findByTransportId(transportId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển với id: " + transportId));
    }

    private Transport getTransportByOrderId(UUID orderId) {
        return transportRepository.findByOrderId(orderId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển theo mã đơn hàng: " + orderId));
    }


}
