package hcmute.kltn.vtv.vnpay.model;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VNPayResponse extends ResponseAbstract implements Serializable {

    private String url;
    private LocalDateTime timeCreated;
    private LocalDateTime timeExpired;

    public static VNPayResponse vnPayResponse(String url, String message, String status) {
        VNPayResponse response = new VNPayResponse();
        response.setUrl(url);
        response.setTimeCreated(LocalDateTime.now());
        response.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }
}