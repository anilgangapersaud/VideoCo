package services;

import model.Address;

public interface AddressService {

    public Address getAddress(String username);

    public boolean saveAddress(Address a);

    public boolean updateAddress(Address a);

    public void deleteAddress(String username);
}
