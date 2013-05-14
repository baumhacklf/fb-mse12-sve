package sve2.fhbay.interfaces;

import javax.ejb.Local;

import sve2.fhbay.domain.Article;

@Local
public interface AuctionLocal extends Auction {
	void addAuctionFinishTimer(Article article);
}
