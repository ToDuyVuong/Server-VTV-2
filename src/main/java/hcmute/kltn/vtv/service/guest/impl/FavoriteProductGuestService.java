package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.repository.user.FavoriteProductRepository;
import hcmute.kltn.vtv.service.guest.IFavoriteProductGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteProductGuestService implements IFavoriteProductGuestService {

    private final FavoriteProductRepository favoriteProductRepository;

    @Override
    public int countFavoriteProduct(Long productId) {
        return favoriteProductRepository.countByProductProductId(productId);
    }

}
