package sve2.fhbay.interfaces;

import sve2.fhbay.dto.BidInfoDto;
import sve2.fhbay.interfaces.exceptions.ArticleBidException;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

public interface Auction {
	void placeBid(String articleName, double bid, Long bidderId)
			throws NameNotFoundException, ArticleBidException,
			IdNotFoundException;

	BidInfoDto getBidInfo(Long articleId) throws IdNotFoundException;
}
