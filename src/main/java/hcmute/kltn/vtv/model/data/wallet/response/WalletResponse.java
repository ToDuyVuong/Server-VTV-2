package hcmute.kltn.vtv.model.data.wallet.response;

import hcmute.kltn.vtv.model.dto.wallet.WalletDTO;
import hcmute.kltn.vtv.model.entity.wallet.Wallet;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse  extends ResponseAbstract{
    private WalletDTO walletDTO;


    public static WalletResponse walletResponse(Wallet wallet, String message, String status) {
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setWalletDTO(WalletDTO.convertEntityToDTO(wallet));
        walletResponse.setMessage(message);
        walletResponse.setStatus(status);
        walletResponse.setCode(200);

        return walletResponse;
    }
}
