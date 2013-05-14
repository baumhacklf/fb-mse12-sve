package sve2.fhbay.interfaces.dao;

import javax.ejb.Local;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

@Local
public interface CustomerDao extends Dao<Customer, Long> {
	Customer findByName(String name) throws NameNotFoundException;
	Customer findByUsername(String username) throws NameNotFoundException;
}
