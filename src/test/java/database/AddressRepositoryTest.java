package database;

import model.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AddressRepositoryTest {

    private static AddressRepository underTest;

    @BeforeEach
    void setup() {
        underTest = AddressRepository.getInstance(TestConfigs.ADDRESS_CSV_TEST_PATH);
        underTest.clearCSV();
    }

    @AfterEach
    void teardown() {
        underTest.clearCSV();
    }

    @Test
    void testSaveAddressSuccess() {
        Address address = new Address();
        address.setUsername("username");
        address.setLineAddress("110 Driftwood Avenue");
        address.setPostalCode("M3N2M8");
        address.setCity("Toronto");
        address.setProvince("Ontario");

        boolean exists = underTest.saveAddress(address);

        assertThat(exists).isEqualTo(true);
    }

    @Test
    void testSaveAddressFailed() {
        Address address = new Address();
        address.setProvince("Ontario");
        boolean exists = underTest.saveAddress(address);
        assertThat(exists).isEqualTo(false);
    }

    @Test
    void testSaveNullAddressFailed() {
        Address address = null;
        boolean result = underTest.saveAddress(address);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testDeleteAddress() {
        underTest.deleteAddress("username");
    }

    @Test
    void testGetAddressSuccess() {
        Address expected = new Address();
        expected.setUsername("username");
        expected.setProvince("Ontario");
        expected.setCity("Toronto");
        expected.setPostalCode("M3N2M8");
        expected.setLineAddress("110 Driftwood Avenue");
        underTest.saveAddress(expected);

        Address result = underTest.getAddress("username");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testGetAddressFailed() {
        Address result = underTest.getAddress("expected");
        assertThat(result).isEqualTo(null);
    }

    @Test
    void testUpdateAddressSuccess() {
        Address expected = new Address();
        expected.setUsername("username");
        expected.setLineAddress("110 Driftwood Ave");
        expected.setCity("Toronto");
        expected.setProvince("Ontario");
        expected.setPostalCode("M3N2M8");

        underTest.saveAddress(expected);

        Address updatedAddress = new Address();
        updatedAddress.setUsername("username");
        updatedAddress.setLineAddress("111 Driftwood Ave");
        updatedAddress.setCity("New York");
        updatedAddress.setProvince("Belgium");
        updatedAddress.setPostalCode(expected.getPostalCode());
        boolean updateResult = underTest.updateAddress(updatedAddress);

        assertThat(updateResult).isEqualTo(true);

        Address result = underTest.getAddress("username");

        assertThat(result).isEqualTo(updatedAddress);
    }

    @Test
    void testUpdateAddressFailure() {
        Address expected = new Address();
        expected.setUsername("username");
        expected.setLineAddress("110 Driftwood Ave");
        expected.setCity("Toronto");
        expected.setProvince("Ontario");
        expected.setPostalCode("M3N2M8");

        underTest.saveAddress(expected);

        Address updatedAddress = new Address();
        updatedAddress.setUsername("username");
        updatedAddress.setLineAddress("111 Driftwood Ave");
        updatedAddress.setCity("New York");
        updatedAddress.setProvince("Belgium");
        boolean updateResult = underTest.updateAddress(updatedAddress);

        assertThat(updateResult).isEqualTo(false);
    }
}