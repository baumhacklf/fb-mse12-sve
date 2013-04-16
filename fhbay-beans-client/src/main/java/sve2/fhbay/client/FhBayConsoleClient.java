package sve2.fhbay.client;

import java.util.Map.Entry;

import javax.naming.NamingException;

import sve2.fhbay.domain.Address;
import sve2.fhbay.domain.CreditCard;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.domain.PaymentData;
import sve2.fhbay.domain.Phone;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.util.DateUtil;
import sve2.util.JndiUtil;
import sve2.util.LoggingUtil;

public class FhBayConsoleClient {

	private static CustomerAdminRemote custAdmin;

	public static void main(String[] args) throws NamingException {
		LoggingUtil.initJdkLogging("logging.properties");

		// java:jboss/exported/fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote
		custAdmin = JndiUtil
				.getRemoteObject(
						"fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote",
						CustomerAdminRemote.class);

		testCustomerAdmin();
		// testSimpleCustomerAdmin();
	}

	// private static void testSimpleCustomerAdmin() throws NamingException {
	// /*
	// * JBoss output (jndi name)
	// * java:jboss/exported/fhbay-beans/SimpleCustomerAdminBean
	// * !sve2.fhbay.interfaces.SimpleCustomerAdminRemote
	// */
	// // Context ctx = new InitialContext();
	// // Object ref =
	// //
	// ctx.lookup("fhbay-beans/SimpleCustomerAdminBean!sve2.fhbay.interfaces.SimpleCustomerAdminRemote");
	// // SimpleCustomerAdminRemote custAdminProxy =
	// // (SimpleCustomerAdminRemote) ref;
	//
	// SimpleCustomerAdminRemote custAdminProxy = JndiUtil
	// .getRemoteObject(
	// "fhbay-beans/SimpleCustomerAdminBean!sve2.fhbay.interfaces.SimpleCustomerAdminRemote",
	// SimpleCustomerAdminRemote.class);
	//
	// Customer cust1 = new Customer("Franz", "Klammer", "fklammer", "passwd",
	// "fklammer@gmail.com");
	// System.out.println("Client: save customer");
	// custAdminProxy.saveCustomer(cust1);
	// }

	private static void testCustomerAdmin() {
		try {
			System.out.println("--------------- save customer ---------------");
			Customer cust1 = new Customer("Jaquira", "Hummelbrunner", "jaqui",
					"pwd", "Johann.Heinzelreiter@fh-hagenberg.at");
			cust1.setBillingAddress(new Address("4232", "Hagenberg",
					"Hauptstraße 117"));
			cust1.addPhone("mobile", new Phone("+43", "(0) 555 333"));
			// cust1.addPhone(new Phone("mobile", "+43", "(0) 555 333"));
			cust1.addShippingAddress(new Address("5555", "Mostbusch",
					"Linzerstraße 15"));
			cust1.addShippingAddress(new Address("8050", "Königsbrunn",
					"Maisfeld 15"));
			cust1.addPaymentData(new CreditCard("Himmelbrunner", "010448812",
					DateUtil.getDate(2007, 07, 1)));

			Customer cust2 = new Customer("Maggi", "Weibold", "maggi", "wei",
					"Johann.Heinzelreiter@fh-hagenberg.at");
			cust2.setBillingAddress(new Address("4020", "Linz",
					"Hauptstraße 117"));

			System.out
					.println("--------------- saveOrUpdateCustomer ---------------");

			Long cust1Id = custAdmin.saveCustomer(cust1);
			@SuppressWarnings("unused")
			Long cust2Id = custAdmin.saveCustomer(cust2);

			System.out
					.println("--------------- addShippingAddress ---------------");
			cust1 = custAdmin.findCustomerById(cust1Id);
			cust1.addShippingAddress(new Address("1000", "Wien",
					"Haudumgasse 87a"));
			cust1.addShippingAddress(new Address("5000", "Salzburg",
					"Moritzwinkel 5"));
			custAdmin.saveCustomer(cust1);

			System.out
					.println("--------------- findAllCustomers ---------------");
			for (Customer c : custAdmin.findAllCustomers()) {
				System.out.println(c);

				if (!c.getPhones().isEmpty()) {
					System.out.println("  phone numbers:");
					// for (Phone phone : c.getPhones())
					// System.out.println("     " + phone);
					for (Entry<String, Phone> entry : c.getPhones().entrySet())
						System.out.println("     " + entry.getKey() + ": "
								+ entry.getValue());
				}
				if (!c.getShippingAddresses().isEmpty()) {
					System.out.println("  shipping addresses:");
					for (Address a : c.getShippingAddresses())
						System.out.println("     " + a);
				}
				if (!c.getPaymentData().isEmpty()) {
					System.out.println("  payment data:");
					for (PaymentData pd : c.getPaymentData())
						System.out.println("     " + pd);
				}
			}

			System.out
					.println("--------------- findCustomerById ---------------");
			System.out.println(custAdmin.findCustomerById(cust1.getId()));
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}
	}
}
