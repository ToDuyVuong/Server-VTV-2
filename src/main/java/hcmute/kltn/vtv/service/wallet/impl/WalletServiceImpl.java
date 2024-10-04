package hcmute.kltn.vtv.service.wallet.impl;


import hcmute.kltn.vtv.model.data.wallet.response.WalletResponse;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.wallet.Transaction;
import hcmute.kltn.vtv.model.entity.wallet.Wallet;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.repository.wallet.WalletRepository;
import hcmute.kltn.vtv.service.wallet.ITransactionService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private final WalletRepository walletRepository;
    private final ITransactionService transactionService;
    private final OrderRepository orderRepository;


    @Async
    @Override
    @Transactional
    public void addNewWalletAfterRegister(String username) {
        if (!walletRepository.existsByUsername(username)) {
            try {
                walletRepository.save(createWalletByUsername(username));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    @Transactional
    public Transaction updateWalletAndCreateTransaction(Wallet wallet, UUID orderId, Long money, String type) {
        updateBalance(wallet, money);
        try {
            walletRepository.save(wallet);

            return transactionService.addNewTransaction(wallet, orderId, money, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cập nhật ví thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void updateWalletByUsername(String username, UUID orderId, Long money, String type) {
        Wallet wallet = getWalletByUsername(username);
        updateBalance(wallet, money);
        try {

            walletRepository.save(wallet);
            transactionService.addNewTransaction(wallet, orderId, money, type);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật ví tiền thất bại! " + e.getMessage());
        }
    }


    @Async
    @Override
    @Transactional
    public void updateWalletByOrderId(UUID orderId,  String type) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng " + orderId));
        Long money = order.getTotalPrice() * 96 / 100;

        try {
            updateWalletByUsername(order.getShop().getCustomer().getUsername(), orderId, money, type);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật ví tiền thất bại! " + e.getMessage());
        }
    }






    @Override
    public WalletResponse getWalletResponseByUsername(String username) {
        checkExistWalletByUsername(username);
        Wallet wallet = getWalletByUsername(username);

        return WalletResponse.walletResponse(wallet, "Lấy thông tin ví thành công", "OK");
    }


    @Override
    public Long getBalanceByUsername(String username) {
        Wallet wallet = getWalletByUsername(username);
        return wallet.getBalance();
    }


    @Override
    public void checkBalanceByUsernameAndMoney(String username, Long money) {
        Wallet wallet = getWalletByUsername(username);
        if (wallet.getBalance() < money) {
            throw new IllegalArgumentException("Số dư trong ví không đủ để thực hiện giao dịch");
        }
    }







    private void updateBalance(Wallet wallet, Long money) {
        if (wallet.getBalance() < -money) {
            throw new IllegalArgumentException("Số dư trong ví không đủ để thực hiện giao dịch này!");
        }
        wallet.setBalance(wallet.getBalance() + money);
        wallet.setUpdateAt(LocalDateTime.now());
    }


    private Wallet getWalletByUsername(String username) {
        return walletRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin ví của tài khoản " + username));
    }


    private void checkExistWalletByUsername(String username) {
        if (!walletRepository.existsByUsername(username)) {
            throw new NotFoundException("Không tìm thấy ví của tài khoản " + username);
        }
    }


    private Wallet createWalletByUsername(String username) {
        Wallet wallet = new Wallet();
        wallet.setUsername(username);
        wallet.setBalance(0L);
        wallet.setStatus(Status.ACTIVE);
        wallet.setCreateAt(LocalDateTime.now());
        wallet.setUpdateAt(LocalDateTime.now());

        return wallet;
    }


}
