package sve2.fhbay.interfaces.dao;

import javax.ejb.Local;

import sve2.fhbay.domain.Customer;

@Local
public interface CustomerDao extends Dao<Customer, Long> {
}
