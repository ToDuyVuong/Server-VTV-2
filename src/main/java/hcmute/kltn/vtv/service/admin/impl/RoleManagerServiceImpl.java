package hcmute.kltn.vtv.service.admin.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.dto.manager.ManagerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.manager.ManagerRepository;
import hcmute.kltn.vtv.service.admin.IRoleManagerService;
import hcmute.kltn.vtv.service.user.impl.CustomerServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleManagerServiceImpl implements IRoleManagerService {

    private final CustomerRepository customerRepository;
    private final CustomerServiceImpl customerService;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ManagerResponse addRoleManager(String usernameAdded, String usernameCustomer) {

        if (managerRepository.existsByManagerUsernameAndStatus(usernameCustomer, Status.ACTIVE)) {
            throw new BadRequestException("Nhân viên đã có quyền quản lý!");
        }


        Customer customer = customerService.getCustomerByUsername(usernameCustomer);
        customer.getRoles().add(Role.MANAGER);

        Manager manager = createManagerByCustomer(customer, usernameAdded);

        try {
            managerRepository.save(manager);
            customerRepository.save(customer);

            return ManagerResponse.managerResponse(manager, "Thêm quyền quản lý cho nhân viên thành công!", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Thêm quyền quản lý thất bại!" + e.getMessage());
        }
    }




    @Override
    @Transactional
    public ManagerResponse removeRoleManager(String usernameAdded, String usernameCustomer) {
        if (!managerRepository.existsByManagerUsername(usernameCustomer)) {
            throw new BadRequestException("Nhân viên không có quyền quản lý!");
        }

        Customer customer = customerService.getCustomerByUsername(usernameCustomer);
        customer.getRoles().remove(Role.MANAGER);

        Manager manager = checkManager(usernameCustomer, usernameAdded);
        manager.setStatus(Status.DELETED);
        manager.setUpdateAt(LocalDateTime.now());

        try {
            managerRepository.save(manager);
            customerRepository.save(customer);


            return ManagerResponse.managerResponse(manager, "Xóa quyền quản lý cho nhân viên thành công!", "Delete");
        } catch (Exception e) {
            throw new BadRequestException("Xóa quyền quản lý thất bại!" + e.getMessage());
        }
    }

    @Override
    public ManagerPageResponse getManagerPage(int page, int size) {
        int totalManager = managerRepository.countAllBy();
        int totalPage = (int) Math.ceil((double) totalManager / size);
        Page<Manager> managerPage = managerRepository.findAllByOrderByManagerFullName(PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý!"));
        String message = "Lấy danh sách quản lý thành công!";

        return ManagerPageResponse.managerPageResponse(managerPage, message);
    }

    @Override
    public ManagerPageResponse getManagerPageByUsername(int page, int size, String username) {
        int totalManager = managerRepository.countAllByManagerUsernameContainsAndStatus(username, Status.ACTIVE);
        int totalPage = (int) Math.ceil((double) totalManager / size);
        Page<Manager> managerPage = managerRepository
                .findAllByManagerUsernameContainingAndStatusOrderByManagerFullName(username, Status.ACTIVE,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý!"));
        String message = "Lấy danh sách quản lý thành công!";

        return ManagerPageResponse.managerPageResponse(managerPage, message);
    }

    @Override
    public ManagerPageResponse getManagerPageByStatus(int page, int size, Status status) {
        int totalManager = managerRepository.countAllByStatus(status);
        int totalPage = (int) Math.ceil((double) totalManager / size);
        Page<Manager> managerPage = managerRepository
                .findAllByStatusOrderByManagerFullName(status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý!"));
        String message = "Lấy danh sách quản lý theo trạng thái thành công!";

        return ManagerPageResponse.managerPageResponse(managerPage, message);
    }

    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
        }
        if (size > 500) {
            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
        }
    }

    private Manager checkManager(String usernameCustomer, String usernameAdded) {
        Manager manager = managerRepository.findByManagerUsername(usernameCustomer)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy quản lý!"));

        if (!manager.getUsernameAdded().equals(usernameAdded)) {
            throw new BadRequestException("Bạn không có quyền xóa quản lý này!");
        }

        if (manager.getStatus() == Status.DELETED) {
            throw new BadRequestException("Nhân viên đã bị xóa quyền quản lý!");
        }

        return manager;
    }


    private Manager createManagerByCustomer(Customer customer, String usernameAdded) {
        Manager manager = new Manager();
        if (managerRepository.existsByManagerUsername(customer.getUsername())) {
            manager = managerRepository.findByManagerUsername(customer.getUsername())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy quản lý!"));
            if (!manager.getUsernameAdded().equals(usernameAdded)) {
                throw new BadRequestException("Bạn không có quyền thêm quyền quản lý cho nhân viên này!");
            }
        } else {
            manager.setUsernameAdded(usernameAdded);
            manager.setManager(customer);
            manager.setCreateAt(LocalDateTime.now());
        }
        manager.setUpdateAt(LocalDateTime.now());
        manager.setStatus(Status.ACTIVE);
        return manager;
    }





}
