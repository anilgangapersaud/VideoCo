package services;

import database.TestConfigs;
import model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AddressServiceTest {

    private AddressService addressService;

    @BeforeEach
    void setup() {
        AddressService.setCsvPath(TestConfigs.ADDRESS_CSV_TEST_PATH);
        addressService = AddressService.getInstance();
    }

    @Test
    void saveAddress() {
        Address a = new Address();
        a.setUsername("username");
        a.setLineAddress("110 Driftwood");
        a.setPostalCode("34234");
        a.setProvince("Ontario");
        a.setCity("toronto");

        assertThat(addressService.saveAddress(a)).isTrue();
    }

    @Test
    void deleteAddress() {
        addressService.deleteAddress("username");
    }

    @Test
    void getAddress() {
        Address a = new Address();
        a.setUsername("username");
        a.setLineAddress("110 Driftwood");
        a.setPostalCode("34234");
        a.setProvince("Ontario");
        a.setCity("toronto");

        addressService.saveAddress(a);
        assertThat(addressService.getAddress("username")).isEqualTo(a);
    }

    @Test
    void updateAddress() {
        Address a = new Address();
        a.setUsername("username");
        a.setLineAddress("110 Driftwood");
        a.setPostalCode("34234");
        a.setProvince("Ontario");
        a.setCity("toronto");

        addressService.saveAddress(a);
        a.setCity("New York");
        boolean result = addressService.updateAddress(a);
        assertThat(result).isTrue();
    }
}