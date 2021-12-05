package services;

import database.AddressRepository;
import database.Observer;
import model.Address;

public class AddressService {

    private static String ADDRESS_CSV_PATH;

    private final AddressRepository addressRepository;

    private volatile static AddressService instance;

    private AddressService() {
        addressRepository = AddressRepository.getInstance(ADDRESS_CSV_PATH);
    }

    public static AddressService getInstance() {
        if (instance == null) {
            synchronized (AddressService.class) {
                if (instance == null) {
                    instance = new AddressService();
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

    public void registerObserver(Observer o) {
        addressRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        addressRepository.removeObserver(o);
    }
}
