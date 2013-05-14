package sve2.fhbay.beans.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import sve2.fhbay.domain.Category;
import sve2.fhbay.interfaces.dao.CategoryDao;

@Stateless
public class CategoryDaoBean extends AbstractDaoBean<Category, Long> implements CategoryDao {

	@Override
	public Category findByName(String name) {
		TypedQuery<Category> qry = getEntityManager().createNamedQuery("qryFindByName", Category.class);
		qry.setParameter("pattern", "%" + name.toLowerCase() + "%");
		return qry.getSingleResult();
	}

	@Override
	public List<Category> findAllRootCategories() {
		TypedQuery<Category> qry = getEntityManager().createNamedQuery("qryFindRootCategories", Category.class);
		return qry.getResultList();
	}
	
}
