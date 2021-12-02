package services;

import model.Address;

public interface AddressService {

    Address getAddress(String username);

    boolean saveAddress(Address a);

    boolean updateAddress(Address a);

    void deleteAddress(String username);

}
