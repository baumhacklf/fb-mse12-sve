package sve2.fhbay.interfaces;

import java.util.Collection;

import javax.ejb.Remote;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;

@Remote
public interface CustomerAdminRemote {
	Long saveCustomer(Customer customer);
	Collection<Customer> findAllCustomers();
	Customer findCustomerById(Long id) throws IdNotFoundException;
	Customer loginCustomer(String name, String password) throws Exception;
	Customer findCustomerByUsername(String username);
}
