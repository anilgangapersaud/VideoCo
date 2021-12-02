package services;

import database.RentedRepository;

public class RentedServiceImpl implements RentedService {

    private final RentedRepository rentedRepository;

    public RentedServiceImpl() {
        rentedRepository = RentedRepository.getInstance();
    }

    @Override
    public RentedRepository getRentedRepository() {
        return rentedRepository;
    }
}
