package model.user;

import database.AddressRepository;

public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    public AddressServiceImpl() {
        addressRepository = new AddressRepository();
    }

    @Override
    public Address getAddress(String username) {
        return addressRepository.getAddress(username);
    }

    @Override
    public boolean saveAddress(Address a) {
        return addressRepository.saveAddress(a);
    }

    public void deleteAddress(String username) {
        addressRepository.deleteAddress(username);
    }

    @Override
    public boolean updateAddress(Address a) {
        return addressRepository.updateAddress(a);
    }
}
