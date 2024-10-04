package hcmute.kltn.vtv.model.extra;


import lombok.Getter;

@Getter
public enum TypeWork {
    SHIPPER, // DELIVERED, IN_TRANSIT
    MANAGER, //
    PROVIDER, //
    WAREHOUSE, //
    TRANSIT, // IN_TRANSIT
    PICKUP; // PICKED_UP
}
