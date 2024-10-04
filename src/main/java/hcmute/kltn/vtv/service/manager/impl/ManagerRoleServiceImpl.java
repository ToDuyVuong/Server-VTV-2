package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.dto.manager.ManagerDTO;
import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.manager.ManagerRepository;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.service.manager.IManagerRoleService;
import hcmute.kltn.vtv.service.user.impl.CustomerServiceImpl;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerRoleServiceImpl implements IManagerRoleService {

    private final CustomerRepository customerRepository;
    private final CustomerServiceImpl customerService;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ManagerResponse managerAddRole(String usernameAdded, String usernameCustomer, Role role) {

        checkRoleManager(role);

        if (managerRepository.existsByManagerUsernameAndStatus(usernameCustomer, Status.ACTIVE)) {
            throw new BadRequestException("Nhân viên đã có quyền quản lý!");
        }

        Customer customer = customerService.getCustomerByUsername(usernameCustomer);
        customer.addRole(role);

        Manager manager = checkManager(usernameAdded, customer);

        try {
            managerRepository.save(manager);
            customerRepository.save(customer);

            String message = "Thêm quyền quản lý " + getMessageByRole(role) + " thành công!";

            return managerResponse(manager, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Thêm quyền quản lý thất bại!");
        }
    }

    @Override
    @Transactional
    public ManagerResponse managerUpdateRole(String usernameAdded, String usernameCustomer, Role role) {

        checkRoleManager(role);
        Customer customer = customerService.getCustomerByUsername(usernameCustomer);
        checkRoleCustomer(customer, role, false);
        customer.addRole(role);

        Manager manager = checkUsernameAdded(usernameAdded, customer);
        manager.setUpdateAt(LocalDateTime.now());

        try {
            managerRepository.save(manager);
            customerRepository.save(customer);

            String message = "Cập nhật quyền quản lý " + getMessageByRole(role) + " thành công!";

            return managerResponse(manager, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật quyền quản lý thất bại!");
        }
    }

    @Override
    @Transactional
    public ManagerResponse managerDeleteRole(String usernameAdded, String usernameCustomer, Role role) {

        checkRoleManager(role);
        Customer customer = customerService.getCustomerByUsername(usernameCustomer);
        checkRoleCustomer(customer, role, true);
        customer.removeRole(role);

        Manager manager = checkUsernameAdded(usernameAdded, customer);
        manager.setUpdateAt(LocalDateTime.now());

        try {
            managerRepository.save(manager);
            customerRepository.save(customer);

            String message = "Xóa quyền quản lý " + getMessageByRole(role) + " thành công!";

            return managerResponse(manager, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Xóa quyền quản lý thất bại!");
        }
    }





    @Override
    public ManagerPageResponse getListManagerPageByRole(Role role, int page, int size) {

        checkRoleManager(role);
        Page<Manager> managers = managerRepository
                .findAllByManagerRolesAndStatus(role, Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý " + getMessageByRole(role) + "!"));


        String message = "Lấy danh sách quản lý " + getMessageByRole(role) + " thành công!";

        return listManagerPageResponse(managers.getContent(), message, managers.getTotalPages(), page, size);
    }

    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page <= 0) {
            throw new BadRequestException("Trang phải lớn hơn 0.");
        }
        if (size <= 0) {
            throw new BadRequestException("Kích thước trang phải lớn hơn 0.");
        }
        if (size > 500) {
            throw new BadRequestException("Kích thước trang phải nhỏ hơn 500.");
        }
    }


    private ManagerPageResponse listManagerPageResponse(List<Manager> managers, String message,
                                                        int totalPage, int page, int size) {
        ManagerPageResponse managerPageResponse = new ManagerPageResponse();
        managerPageResponse.setTotalPage(totalPage);
        managerPageResponse.setPage(page);
        managerPageResponse.setSize(size);
        managerPageResponse.setCount(managers.size());
        managerPageResponse.setManagerDTOs(ManagerDTO.convertEntitiesToDTOs(managers));
        managerPageResponse.setStatus("OK");
        managerPageResponse.setCode(200);
        managerPageResponse.setMessage(message);

        return managerPageResponse;


    }

    private void checkRoleCustomer(Customer customer, Role role, boolean isDelete) {
        if (isDelete) {
            if (!customer.getRoles().contains(role)) {
                throw new BadRequestException(
                        "Không tìm thấy quyền quản lý cần xóa! Quyền quản lý cần xóa là " + role + ".");
            }
        } else {
            if (customer.getRoles().contains(role)) {
                throw new BadRequestException("Nhân viên đã có quyền quản lý! Quyền quản lý là " + role + ".");
            }
        }
    }

    private String getMessageByRole(Role role) {
        if (role.equals(Role.MANAGERSHIPPING)) {
            return "vận chuyển";
        } else if (role.equals(Role.MANAGERVENDOR)) {
            return "cửa hàng";
        } else {
            return "khách hàng";
        }
    }

    private void checkRoleManager(Role role) {
        if (!role.equals(Role.MANAGERSHIPPING) && !role.equals(Role.MANAGERVENDOR)
                && !role.equals(Role.MANAGERCUSTOMER)) {
            throw new BadRequestException(
                    "Quyền quản lý không hợp lệ! Quyền quản lý phải là MANAGER_SHIPPING, MANAGER_VENDOR hoặc MANAGER_CUSTOMER.");
        }
    }

    private ManagerResponse managerResponse(Manager manager, String message, String status) {
        ManagerResponse managerResponse = new ManagerResponse();
        managerResponse.setManagerDTO(ManagerDTO.convertEntityToDTO(manager));
        managerResponse.setStatus(status);
        managerResponse.setCode(200);
        managerResponse.setMessage(message);
        return managerResponse;
    }


    private Manager checkManager(String usernameAdded, Customer customer) {

        Manager manager = new Manager();

        if (managerRepository.existsByManagerUsername(customer.getUsername())) {
            manager = checkUsernameAdded(usernameAdded, customer);
        } else {
            manager.setUsernameAdded(usernameAdded);
            manager.setManager(customer);
            manager.setCreateAt(LocalDateTime.now());
        }
        manager.setUpdateAt(LocalDateTime.now());
        manager.setStatus(Status.ACTIVE);

        return manager;
    }

    private Manager checkUsernameAdded(String usernameAdded, Customer customer) {

        Manager manager = managerRepository.findByManagerUsername(customer.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy quản lý!"));
        if (!manager.getUsernameAdded().equals(usernameAdded)) {
            throw new BadRequestException("Bạn không có quyền thêm quyền quản lý cho nhân viên này!");
        }
        return manager;
    }

}
