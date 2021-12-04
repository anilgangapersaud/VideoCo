package database;

import model.payments.CreditCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BillingRepositoryTest {

    private static BillingRepository underTest;

    @BeforeAll
    static void setup() {
        underTest = BillingRepository.getInstance();
    }

    @Test
    void getCreditCard() {
        CreditCard expected = new CreditCard();
        expected.setUsername("username");
        expected.setCardNumber("123");
        expected.setExpiry("12/01");
        expected.setCsv("123");
        expected.setBalance(10);

        underTest.saveCreditCard(expected);

        CreditCard result = underTest.getCreditCard("username");
        assertThat(result).isEqualTo(expected);

        underTest.deleteCreditCard("username");
    }

    @Test
    void saveCreditCardSuccess() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCardNumber("123");
        c.setExpiry("12/01");
        c.setCsv("123");
        c.setBalance(10);

        boolean result = underTest.saveCreditCard(c);
        assertThat(result).isEqualTo(true);

        underTest.deleteCreditCard(c.getUsername());
    }

    @Test
    void saveCreditCardFailure() {
        CreditCard c = new CreditCard();
        c.setUsername("username");

        boolean result = underTest.saveCreditCard(c);
        assertThat(result).isEqualTo(false);

        underTest.deleteCreditCard(c.getUsername());
    }

    @Test
    void deleteCreditCard() {
        underTest.deleteCreditCard("username");
    }

    @Test
    void updateCreditCardSuccess() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCardNumber("123");
        c.setExpiry("12/01");
        c.setCsv("123");
        c.setBalance(10);

        underTest.saveCreditCard(c);

        CreditCard expected = new CreditCard();
        expected.setUsername("username");
        expected.setCardNumber("1234");
        expected.setExpiry("12/11");
        expected.setCsv("12356");
        expected.setBalance(10);

        underTest.updateCreditCard(expected);

        CreditCard result = underTest.getCreditCard("username");

        assertThat(result).isEqualTo(expected);

        underTest.deleteCreditCard(expected.getUsername());
    }

    @Test
    void updateCreditCardFailure() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCardNumber("123");
        c.setExpiry("12/01");
        c.setCsv("123");
        c.setBalance(10);

        underTest.saveCreditCard(c);

        CreditCard expected = new CreditCard();
        expected.setUsername("username");

        boolean result = underTest.updateCreditCard(expected);

        assertThat(result).isEqualTo(false);

        underTest.deleteCreditCard("username");
    }

    @Test
    void refundCustomer() {
        double amount = 10;
        double refund = 5;
        double expected = 5;
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCardNumber("123");
        c.setExpiry("12/01");
        c.setCsv("123");
        c.setBalance(amount);

        underTest.saveCreditCard(c);

        underTest.refundCustomer("username", refund);

        CreditCard result = underTest.getCreditCard("username");
        assertThat(result.getBalance()).isEqualTo(expected);

        underTest.deleteCreditCard("username");
    }

    @Test
    void chargeCustomer() {
        double charge = 10;
        double expected = 10;
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCardNumber("123");
        c.setExpiry("12/01");
        c.setCsv("123");
        c.setBalance(0);

        underTest.saveCreditCard(c);

        underTest.chargeCustomer("username", charge);

        CreditCard result = underTest.getCreditCard("username");

        assertThat(result.getBalance()).isEqualTo(expected);

        underTest.deleteCreditCard("username");
    }

}