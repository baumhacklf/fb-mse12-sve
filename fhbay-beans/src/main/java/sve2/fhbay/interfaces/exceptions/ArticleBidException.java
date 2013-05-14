package sve2.fhbay.interfaces.exceptions;


public class ArticleBidException extends Exception {
	private static final long serialVersionUID = 1L;

	public ArticleBidException(String articleName, String errorMessage) {
		super(String.format("%s by <%s>", errorMessage, articleName));
	}
}
