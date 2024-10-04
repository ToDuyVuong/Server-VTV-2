package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.request.UpdateTransportProviderRequest;
import hcmute.kltn.vtv.model.data.shipping.response.ListTransportProviderResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportProviderResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.shipping.TransportProviderRepository;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportProviderServiceImpl implements ITransportProviderService {
    private final TransportProviderRepository transportProviderRepository;
    private final IDeliverService deliverService;


    @Transactional
    @Override
    public TransportProviderResponse updateTransportProviderResponse(UpdateTransportProviderRequest request,
                                                                     Long transportProviderId, String username) {


        TransportProvider transportProvider = updateTransportProvider(request, transportProviderId);


        checkOwnerUpdateExist(username, transportProviderId);
        checkEmailUpdateExist(request.getEmail(), transportProviderId);
        checkPhoneUpdateExist(request.getPhone(), transportProviderId);

        try {
            transportProviderRepository.save(transportProvider);
            return TransportProviderResponse.transportProviderResponse(transportProvider, "Cập nhật nhà vận chuyển thành công.", "Success");
        } catch (Exception e) {
            throw new NotFoundException("Cập nhật nhà vận chuyển thất bại.");
        }
    }


    @Override
    public TransportProvider getTransportProviderByShortName(String shortName) {
        return transportProviderRepository.findByShortName(shortName)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển."));
    }


    @Override
    public void checkExistByTransportProviderId(Long transportProviderId) {
        if (!transportProviderRepository.existsByTransportProviderId(transportProviderId)) {
            throw new NotFoundException("Không tồn tại nhà vận chuyển theo mã nhà vận chuyển!");
        }
    }


    @Override
    public List<TransportProvider> getTransportProvidersByProvince(String provinceCodeShop, String provinceCodeCustomer) {
        return transportProviderRepository.findAllByProvinceCodeShopAndProvinceCodeCustomerAndStatus(provinceCodeShop, provinceCodeCustomer, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển nào."));
    }


    @Override
    public TransportProviderResponse getTransportProviderById(Long id) {
        TransportProvider transportProvider = getTransportProviderByTransportProviderId(id);

        return TransportProviderResponse.transportProviderResponse(transportProvider, "Lấy thông tin nhà vận chuyển thành công.", "OK");
    }


    @Override
    public TransportProviderResponse getTransportProviderByUsername(String username) {

//        TransportProvider transportProvider = transportProviderRepository.findByCustomerUsername(username)
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển theo tên đăng nhập!"));
        Deliver deliver = deliverService.getDeliverByUsername(username);


        return TransportProviderResponse.transportProviderResponse(deliver.getTransportProvider(), "Lấy thông tin nhà vận chuyển thành công.", "OK");
    }


    @Override
    public TransportProvider getTransportProviderByTransportProviderId(Long id) {

        return transportProviderRepository.findByTransportProviderId(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển theo mã nhà vận chuyển!"));
    }


    @Override
    public ListTransportProviderResponse getAllTransportProvidersNotProvince() {

        List<TransportProvider> transportProviders = transportProviderRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển nào."));


        return ListTransportProviderResponse.listTransportProvidersNotProvinceResponse(transportProviders);
    }

    @Override
    public ListTransportProviderResponse getAllTransportProviders() {

        List<TransportProvider> transportProviders = transportProviderRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển nào."));


        return ListTransportProviderResponse.listTransportProvidersResponse(transportProviders);
    }


    private void checkOwnerUpdateExist(String username, Long transportProviderId) {
        TransportProvider transportProvider = transportProviderRepository.findByCustomerUsername(username)
                .orElse(null);

        if (transportProvider == null || !transportProvider.getTransportProviderId().equals(transportProviderId)) {
            throw new BadRequestException("Tài khoản không có quyền cập nhật nhà vận chuyển này.");
        }
    }

    private void checkEmailUpdateExist(String email, Long transportProviderId) {
        TransportProvider transportProvider = transportProviderRepository.findByEmail(email)
                .orElse(null);
        if (transportProvider != null && !transportProvider.getTransportProviderId().equals(transportProviderId)) {
            throw new BadRequestException("Email nhà vận chuyển đã tồn tại.");
        }
    }


    private void checkPhoneUpdateExist(String phone, Long transportProviderId) {
        TransportProvider transportProvider = transportProviderRepository.findByPhone(phone)
                .orElse(null);
        if (transportProvider != null && !transportProvider.getTransportProviderId().equals(transportProviderId)) {
            throw new BadRequestException("Số điện thoại nhà vận chuyển đã tồn tại.");
        }
    }

    private TransportProvider updateTransportProvider(UpdateTransportProviderRequest request, Long transportProviderId) {
        TransportProvider transportProvider = getTransportProviderByTransportProviderId(transportProviderId);

        transportProvider.setFullName(request.getFullName());
        transportProvider.setShortName(request.getShortName());
        transportProvider.setEmail(request.getEmail());
        transportProvider.setPhone(request.getPhone());
        transportProvider.setUpdateAt(LocalDateTime.now());

        return transportProvider;
    }


}