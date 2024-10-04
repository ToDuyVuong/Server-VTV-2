package hcmute.kltn.vtv.service.vtv;

public interface IPageService {
    void checkRequestSortCustomerParams(String sort);

    void validatePageNumberAndSize(int page, int size);

    void checkRequestProductPageParams(int page, int size);

    void checkRequestOrderPageParams(int page, int size);

    void checkRequestPriceRangeParams(Long minPrice, Long maxPrice);

    void checkRequestSortParams(String sort);
}
