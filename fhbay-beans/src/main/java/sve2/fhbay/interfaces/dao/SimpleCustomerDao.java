package sve2.fhbay.interfaces.dao;

import java.util.Collection;

import javax.ejb.Local;

import sve2.fhbay.domain.Customer;

@Local
public interface SimpleCustomerDao {
	void persist(Customer customer);
	Customer findById(Long id);
	Collection<Customer> findAll();
}
