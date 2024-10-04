package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.model.entity.wallet.Transaction;
import hcmute.kltn.vtv.model.entity.wallet.Wallet;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.repository.wallet.TransactionRepository;
import hcmute.kltn.vtv.repository.wallet.WalletRepository;
import hcmute.kltn.vtv.service.wallet.ITransactionService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;


    @Override
    @Transactional
    public Transaction addNewTransaction(Wallet wallet, UUID orderId, Long money, String type) {
        checkExistWalletById(wallet.getWalletId());
        checkExistOrderByOrderId(orderId);
        try {
            return transactionRepository.save(createTransaction(wallet, orderId, money, type));
        } catch (Exception e) {
            throw new IllegalArgumentException("Thêm mới giao dịch thất bại! " + e.getMessage());
        }
    }


    @Async
    @Override
    @Transactional
    public void addNewTransaction(String username, UUID orderId, Long money, String type) {
        Wallet wallet = walletRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví theo username: " + username));
        checkExistOrderByOrderId(orderId);
        try {
             transactionRepository.save(createTransaction(wallet, orderId, money, type));
        } catch (Exception e) {
            throw new IllegalArgumentException("Thêm mới giao dịch thất bại! " + e.getMessage());
        }
    }



    private Transaction createTransaction(Wallet wallet, UUID orderId, Long money, String type) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setOrderId(orderId);
        transaction.setMoney(money);
        transaction.setType(type);
        transaction.setStatus(Status.ACTIVE);
        transaction.setCreateAt(LocalDateTime.now());

        return transaction;
    }


    private void checkExistWalletById(Long walletId) {
        if (!walletRepository.existsById(walletId)) {
            throw new NotFoundException("Không tìm thấy ví theo id: " + walletId);
        }
    }

    private void checkExistOrderByOrderId(UUID orderId) {
        if (!orderRepository.existsByOrderId(orderId)) {
            throw new NotFoundException("Không tìm thấy đơn hàng theo id: " + orderId);
        }
    }






}
