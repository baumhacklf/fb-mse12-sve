package sve2.fhbay.client;

import sve2.fhbay.domain.Address;
import sve2.fhbay.domain.CreditCard;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.domain.CustomerRole;
import sve2.fhbay.domain.Phone;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.util.DateUtil;

public class TestData {
	
	public static void userSetup(CustomerAdminRemote custAdmin) {
		Customer temp;
		try {
			temp = custAdmin.findCustomerByUsername("maxi");
		} catch (Exception e) {
			temp = null;
		}

		if (temp == null) {
			Customer customer = new Customer("Max", "Mustermann", "maxi",
					"pwd", "max@mustermann.at");
			customer.setBillingAddress(new Address("4232", "Hagenberg",
					"Hauptstraße 117"));
			customer.addPhone("mobile", new Phone("+43", "(0) 555 333"));
			customer.addShippingAddress(new Address("5555", "Mostbusch",
					"Linzerstraße 15"));
			customer.addShippingAddress(new Address("8050", "Königsbrunn",
					"Maisfeld 15"));
			customer.addPaymentData(new CreditCard("Himmelbrunner",
					"010448812", DateUtil.getDate(2007, 07, 1)));
			customer.setRole(CustomerRole.ADMINISTRATOR);

			custAdmin.saveCustomer(customer);
		}
	}
	
}
