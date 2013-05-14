package sve2.fhbay.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.dao.CustomerDao;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

@Stateless
public class CustomerAdminBean implements CustomerAdminRemote {

	@EJB
	private CustomerDao customerDao;

	public Long saveCustomer(Customer customer) {
		System.out.println("saveCustomer(" + customer.toString() + ")");

		// customer doesn't get id - overwrite old customer
		customer = customerDao.merge(customer);

		return customer.getId();
	}

	public Collection<Customer> findAllCustomers() {
		System.out.println("findAllCustomers()");
		return customerDao.findAll();
	}

	public Customer findCustomerById(Long id) throws IdNotFoundException {
		System.out.println("findCustomerById(" + id + ")");
		Customer customer = customerDao.findById(id);
		if (customer == null)
			throw new IdNotFoundException(id, "Customer");
		return customer;
	}

	@Override
	public Customer loginCustomer(String name, String password)
			throws IdNotFoundException, Exception {
		
		Customer customer = customerDao.findByUsername(name);
		
		if(customer == null)
			throw new NameNotFoundException(name, "Customer");
		
		if(customer.getPassword().equals(password))
			return customer;
		
		return null;
	}

	@Override
	public Customer findCustomerByUsername(String username) {
		Customer customer;
		try {
			customer = customerDao.findByUsername(username);
		} catch (NameNotFoundException e) {
			System.out.println("Username not fount. Return null value");
			customer = null;
		}
		return customer;
	}
}
