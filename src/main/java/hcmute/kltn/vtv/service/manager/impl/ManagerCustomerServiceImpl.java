package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.PageCustomerResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.manager.ManagerShopRepository;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerCustomerServiceImpl implements IManagerCustomerService {

    private final CustomerRepository customerRepository;
    private final ManagerShopRepository managerShopRepository;

    @Override
    public PageCustomerResponse getPageCustomerByStatus(int size, int page, Status status) {
        Page<Customer> customers = customerRepository.findAllByStatusOrderByUsername(status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách khách hàng"));

        return PageCustomerResponse.pageCustomerResponse(customers, "Lấy danh sách khách hàng theo trạng thái thành công!");
    }

    @Override
    public PageCustomerResponse getPageCustomerByStatusAndSort(int size, int page, Status status, String sort) {

        Page<Customer> customers;
        String message;

        switch (sort) {
            case "name-asc" -> {
                customers = customerRepository.findAllByStatusOrderByFullName(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo tên tăng dần và trạng thái thành công!";
            }
            case "name-desc" -> {
                customers = customerRepository
                        .findAllByStatusOrderByFullNameDesc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo tên giảm dần và trạng thái thành công!";
            }
            case "at-asc" -> {
                customers = customerRepository.findAllByStatusOrderByCreateAtAsc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo ngày tạo tăng dần và trạng thái thành công!";
            }
            case "at-desc" -> {
                customers = customerRepository
                        .findAllByStatusOrderByCreateAtDesc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo ngày tạo giảm dần và trạng thái thành công!";
            }
            default -> {
                return getPageCustomerByStatus(size, page, status);
            }
        }

        return PageCustomerResponse.pageCustomerResponse(customers, message);

    }

    @Override
    public PageCustomerResponse searchPageCustomerByFullNameAndStatus(int size, int page, Status status, String search) {

        Page<Customer> customers = customerRepository.findAllByFullNameContainingAndStatus(search, status,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
        String message = "Tìm kiếm danh sách khách hàng theo tên và trạng thái thành công!";

        return PageCustomerResponse.pageCustomerResponse(customers, message);
    }

    @Override
    public ProfileCustomerResponse getCustomerDetailByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng không tồn tại với mã: " + customerId));

        return ProfileCustomerResponse.profileCustomerResponse(customer, "Lấy thông tin khách hàng thành công!", "ok");
    }


    @Override
    @Transactional
    public void updateRoleWithCustomer(Long customerId, Role role) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng cập nhật quyền không tồn tại!"));
        customer.addRole(role);
        customer.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật quyền cho tài khoản thất bại!");
        }
    }


    @Override
    @Transactional
    public ProfileCustomerResponse updateCustomerRoleByCustomerId(Long customerId, Role role) {
        updateRoleWithCustomer(customerId, role);
        return getCustomerDetailByCustomerId(customerId);
    }




}
