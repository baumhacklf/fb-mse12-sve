package sve2.fhbay.beans;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.domain.Bid;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.dto.BidInfoDto;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CustomerDao;
import sve2.fhbay.interfaces.exceptions.ArticleBidException;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

@Stateless
public class AuctionBean implements AuctionLocal, AuctionRemote {

	@Resource
	private TimerService timerService;

	@EJB
	private ArticleDao articleDao;

	@EJB
	private CustomerDao customerDao;

	@Override
	public void addAuctionFinishTimer(Article article) {
		timerService.createTimer(article.getEndDate(), article.getId());
	}

	@Timeout
	public void finishAuction(Timer timer) {
		Long articleId = (Long) timer.getInfo();
		Article article = articleDao.findById(articleId);

		Customer winner = article.getWinner();

		if (winner == null)
			article.setState(ArticleState.UNSALEABLE);
		else {
			article.setBuyer(winner);
			article.setFinalPrice(article.getCurrentPrice());
			article.setState(ArticleState.SOLD);
		}

		articleDao.persist(article);

		System.out.println(String.format("###>Article <%s> finished...",
				article.getName()));
	}

	@Override
	public void placeBid(String articleName, double amount, Long bidderId)
			throws NameNotFoundException, ArticleBidException,
			IdNotFoundException {
		
		if(articleName == null || bidderId == null) 
			throw new IllegalArgumentException();

		Article article = articleDao.findByName(articleName);

		if (article == null)
			throw new NameNotFoundException(articleName, "Article");

		Customer customer = customerDao.findById(bidderId);

		if (customer == null)
			throw new IdNotFoundException(bidderId, "Customer");

		if (article.getMinimumBid() > amount)
			throw new ArticleBidException(articleName,
					"Bid was lower than initial price.");

		Bid bid = new Bid();
		bid.setAmount(amount);
		bid.setArticleId(article.getId());
		bid.setBidder(customer);

		article.addBid(bid);
		articleDao.persist(article);
	}

	@Override
	public BidInfoDto getBidInfo(Long articleId) throws IdNotFoundException {
		if (articleId == null)
			throw new IllegalArgumentException();

		Article article = articleDao.findById(articleId);

		if (article == null)
			throw new IdNotFoundException(articleId, "Article");

		BidInfoDto bidInfo = new BidInfoDto(article);
		return bidInfo;
	}

}
