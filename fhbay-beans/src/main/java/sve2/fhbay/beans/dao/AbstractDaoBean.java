package sve2.fhbay.beans.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sve2.fhbay.interfaces.dao.Dao;

public class AbstractDaoBean<T, ID extends Serializable> implements Dao<T, ID> {

	@PersistenceContext
	private EntityManager em;

	private Class<T> entityType;

	@SuppressWarnings("unchecked")
	public AbstractDaoBean() {
		ParameterizedType thisType = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		entityType = (Class<T>)thisType.getActualTypeArguments()[0];
	}

	@Override
	public void persist(T entity) {
		em.persist(entity);
	}

	@Override
	public T findById(ID id) {
		return em.find(entityType, id);
	}

	@Override
	public Collection<T> findAll() {
		return em.createQuery(
				String.format("select entity from %s entity",
						entityType.getName()), entityType).getResultList();
	}

	@Override
	public T merge(T entity) {
		return em.merge(entity);
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	protected Class<T> getEntityType() {
		return entityType;
	}
}