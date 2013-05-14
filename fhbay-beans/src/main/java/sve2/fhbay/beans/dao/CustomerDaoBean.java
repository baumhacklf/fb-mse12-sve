package sve2.fhbay.beans.dao;


import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.dao.CustomerDao;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

@Stateless
public class CustomerDaoBean extends AbstractDaoBean<Customer, Long> implements CustomerDao {

	@Override
	public Customer findByName(String name) throws NameNotFoundException {
		TypedQuery<Customer> qry = getEntityManager().createNamedQuery("qryFindCustomerByName", Customer.class);
		qry.setParameter("pattern", "%" + name.toLowerCase() + "%");
		return qry.getSingleResult();
	}

	@Override
	public Customer findByUsername(String username)
			throws NameNotFoundException {
		TypedQuery<Customer> qry = getEntityManager().createNamedQuery("qryFindCustomerByUsername", Customer.class);
		qry.setParameter("pattern", "%" + username.toLowerCase() + "%");
		return qry.getSingleResult();
	}
	
}
