package sve2.fhbay.interfaces.exceptions;


public class NameNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NameNotFoundException(String name, String entity) {
		super(String.format("Name: <%s> for entity <%s> not found", name, entity));
	}
}
