package sve2.fhbay.interfaces.dao;

import java.io.Serializable;
import java.util.Collection;

public interface Dao<T, ID extends Serializable> {

	void persist(T entity);

	T findById(ID id);

	Collection<T> findAll();

	T merge(T entity);

}