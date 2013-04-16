package sve2.fhbay.interfaces.exceptions;

import java.io.Serializable;

public class IdNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public IdNotFoundException(Serializable id, String entity) {
		super(String.format("ID: <%s> for entity <%s> not found", id, entity));
	}
}
