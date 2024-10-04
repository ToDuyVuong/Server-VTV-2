package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.data.wallet.response.WalletResponse;
import hcmute.kltn.vtv.model.entity.wallet.Transaction;
import hcmute.kltn.vtv.model.entity.wallet.Wallet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IWalletService {
    @Async
    @Transactional
    void addNewWalletAfterRegister(String username);

    @Transactional
    Transaction updateWalletAndCreateTransaction(Wallet wallet, UUID orderId, Long money, String type);


    @Transactional
    void updateWalletByUsername(String username, UUID orderId, Long money, String type);

    @Async
    @Transactional
    void updateWalletByOrderId(UUID orderId, String type);

    WalletResponse getWalletResponseByUsername(String username);

    Long getBalanceByUsername(String username);

    void checkBalanceByUsernameAndMoney(String username, Long money);
}
