package services;

import database.AddressRepository;
import database.Observer;
import model.Address;

public class AddressServiceImpl {

    private static String ADDRESS_CSV_PATH;

    private final AddressRepository addressRepository;

    private volatile static AddressServiceImpl instance;

    private AddressServiceImpl() {
        addressRepository = AddressRepository.getInstance(ADDRESS_CSV_PATH);
    }

    public static AddressServiceImpl getInstance() {
        if (instance == null) {
            synchronized (AddressServiceImpl.class) {
                if (instance == null) {
                    instance = new AddressServiceImpl();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String path) {
        ADDRESS_CSV_PATH = path;
    }

    public boolean saveAddress(Address address) {
        return addressRepository.saveAddress(address);
    }

    public void deleteAddress(String username) {
        addressRepository.deleteAddress(username);
    }

    public Address getAddress(String username) {
        return addressRepository.getAddress(username);
    }

    public boolean updateAddress(Address address) {
        return addressRepository.updateAddress(address);
    }

    public boolean checkAddressExists(String username) {
        return addressRepository.checkAddressExists(username);
    }

    public void registerObserver(Observer o) {
        addressRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        addressRepository.removeObserver(o);
    }
}
