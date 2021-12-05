package warehouses;

import model.Address;
import model.Order;
import services.AddressService;

public class WarehouseFactory {

    private final AddressService addressService;

    public WarehouseFactory() {
        addressService = AddressService.getInstance();
    }

    public Warehouse getWarehouse(Order o) {
        Address a = addressService.getAddress(o.getUsername());

        if (a.getProvince().equalsIgnoreCase("ONTARIO")) {
            return new OntarioWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("ALBERTA")) {
            return new AlbertaWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("QUEBEC")) {
            return new QuebecWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("BRITISH COLUMBIA")) {
            return new BritishColumbiaWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("YUKON")) {
            return new YukonWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("MANITOBA")) {
            return new ManitobaWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("NORTHWEST TERRITORIES")) {
            return new NorthwestTerritoriesWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("NUNAVUT")) {
            return new NunavutWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("NEW BRUNSWICK")) {
            return new NewBrunswickWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("NEWFOUNDLAND AND LABRADOR")) {
            return new NewfoundlandAndLabradorWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("NOVA SCOTIA")) {
            return new NovaScotiaWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("SASKATCHEWAN")) {
            return new SaskatchewanWarehouse();
        } else if (a.getProvince().equalsIgnoreCase("PRINCE EDWARD ISLAND")) {
            return new PrinceEdwardIslandWarehouse();
        } else {
            return null;
        }
    }
}
