package sve2.fhbay.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.SimpleCustomerAdminRemote;
import sve2.fhbay.interfaces.dao.SimpleCustomerDao;

/*
 * @Remote(SimpleCustomerRemote.class) -> anstatt der Interface Annotation
 */
@Stateless
public class SimpleCustomerAdminBean implements SimpleCustomerAdminRemote {

	//injizieren
	@EJB
	private SimpleCustomerDao customerDao;
	
	public Long saveCustomer(Customer customer) {
		System.out.println("saveCustomer(" + customer.toString() + ")" );
		
		customerDao.persist(customer);
		
		return customer.getId();
	}

	public Collection<Customer> findAllCustomers() {
		System.out.println("findAllCustomers()");
		return customerDao.findAll();
	}
 
	public Customer findCustomerById(Long id) {
		System.out.println("findCustomerById(" + id + ")");
		return customerDao.findById(id);
	}

}
