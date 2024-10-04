package hcmute.kltn.vtv.model.dto.wallet;

import hcmute.kltn.vtv.model.entity.wallet.Transaction;
import hcmute.kltn.vtv.model.entity.wallet.Wallet;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private Long walletId;
    private Long balance;
    private Status status;
    private LocalDateTime updateAt;
    private List<TransactionDTO> transactionDTOs;

    public static WalletDTO convertEntityToDTO(Wallet wallet) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setWalletId(wallet.getWalletId());
        walletDTO.setBalance(wallet.getBalance());
        walletDTO.setStatus(wallet.getStatus());
        walletDTO.setUpdateAt(wallet.getUpdateAt());
        walletDTO.setTransactionDTOs(TransactionDTO.convertEntitiesToDTOs(wallet.getTransactions()));

        return walletDTO;
    }

    public static List<WalletDTO> convertEntitiesToDTOs(List<Wallet> wallets) {
        return wallets.stream()
                .map(WalletDTO::convertEntityToDTO)
                .toList();
    }
}