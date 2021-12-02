package services;

import database.AddressRepository;
import model.Address;

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl() {
        addressRepository = AddressRepository.getInstance();
    }

    @Override
    public Address getAddress(String username) {
        return addressRepository.getAddress(username);
    }

    @Override
    public boolean saveAddress(Address a) {
        return addressRepository.saveAddress(a);
    }

    @Override
    public void deleteAddress(String username) {
        addressRepository.deleteAddress(username);
    }

    @Override
    public boolean updateAddress(Address a) {
        return addressRepository.updateAddress(a);
    }

}
