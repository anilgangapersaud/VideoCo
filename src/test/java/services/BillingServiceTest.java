package services;

import database.TestConfigs;
import model.payments.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BillingServiceTest {

    private BillingService billingService;

    @BeforeEach
    void setup() {
        BillingService.setCsvPath(TestConfigs.ADDRESS_CSV_TEST_PATH);
        billingService = BillingService.getInstance();
    }

    @Test
    void getCreditCard() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCsv("2342");
        c.setExpiry("3423");
        c.setCardNumber("2034234");
        c.setBalance(0);

        billingService.saveCreditCard(c);

        assertThat(billingService.getCreditCard(c.getUsername())).isEqualTo(c);
    }

    @Test
    void deleteCreditCard() {
        billingService.deleteCreditCard("username");
    }

    @Test
    void updateCreditCard() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCsv("2342");
        c.setExpiry("3423");
        c.setCardNumber("2034234");
        c.setBalance(0);

        billingService.saveCreditCard(c);

        c.setBalance(40);

        billingService.updateCreditCard(c);
    }

    @Test
    void refundCustomer() {
        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setCsv("2342");
        c.setExpiry("3423");
        c.setCardNumber("2034234");
        c.setBalance(50);

        billingService.saveCreditCard(c);

        billingService.refundCustomer("username",25);

        billingService.getCreditCard("username");
        assertThat(billingService.getCreditCard("username").getBalance()).isEqualTo(25);
    }

}