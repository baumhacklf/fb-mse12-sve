package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "qryFindArticleByName", query = "select a from Article a where lower(a.name) like :pattern"),
	@NamedQuery(name = "qryFindByCategoryAndPattern", query = "select a from Article a where lower(a.name) like :pattern or "
		+ "lower(a.description) like :pattern") })
public class Article implements Serializable, Comparable<Article> {

	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 5000)
	private String description;

	@ManyToOne
	private Customer seller;
	@ManyToOne
	private Customer buyer;

	@Column(nullable = false)
	private double initialPrice;
	private double finalPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	private ArticleState state = ArticleState.OFFERED;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Bid> bids = new HashSet<Bid>();

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Category> categories = new HashSet<Category>();

	public Article() {
	}

	public Article(String name, String description, double initialPrice,
			Date startDate, Date endDate) {
		this.name = name;
		this.description = description;
		this.initialPrice = initialPrice;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/*
	 * Getter & Setter
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getSeller() {
		return seller;
	}

	public void setSeller(Customer seller) {
		this.seller = seller;
	}

	public Customer getBuyer() {
		return buyer;
	}

	public void setBuyer(Customer buyer) {
		this.buyer = buyer;
	}

	public double getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(double initialPrice) {
		this.initialPrice = initialPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ArticleState getState() {
		return state;
	}

	public void setState(ArticleState state) {
		this.state = state;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}
	
	/*
	 * Inner Classes
	 */
	private class BidOrderComparator implements Comparator<Bid> {
		@Override
		public int compare(Bid arg0, Bid arg1) {
			return Double.compare(arg1.getAmount(), arg0.getAmount());
		}
	}

	/*
	 * Comfort Methods
	 */
	public String toString() {
		return String
				.format("Article (%d): name=%s, articleState=%s, initialPrice=%.2f, finalPrice=%.2f",
						getId(), getName(), getState().toString(),
						getInitialPrice(), getFinalPrice());
	}

	@Override
	public int compareTo(Article otherArticle) {
		return this.getId().compareTo(otherArticle.getId());
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	public void addBid(Bid bid) {
		this.bids.add(bid);
	}
	
	public double getMinimumBid() {
		if(bids.isEmpty())
			return initialPrice;
		
		List<Bid> sortedBids = new ArrayList<Bid>(bids);
		Collections.sort(sortedBids, new BidOrderComparator());
		
		return sortedBids.get(1).getAmount() + 1;
	}
	
	public double getHighestBid() {
		if(bids.isEmpty())
			return initialPrice;
		
		List<Bid> sortedBids = new ArrayList<Bid>(bids);
		Collections.sort(sortedBids, new BidOrderComparator());
		return sortedBids.get(0).getAmount();
	}
	
	public Customer getWinner() {
		if (bids.isEmpty())
			return null;
		
		List<Bid> sortedBids = new ArrayList<Bid>(bids);
		Collections.sort(sortedBids, new BidOrderComparator());
		
		return sortedBids.get(1).getBidder();
	}
	
	public double getCurrentPrice() {
		if (bids.isEmpty())
			return initialPrice;
		
		if (bids.size() == 1)
			return initialPrice + 1;
		
		List<Bid> sortedBids = new ArrayList<Bid>(bids);
		Collections.sort(sortedBids, new BidOrderComparator());
		
		return sortedBids.get(1).getAmount() + 1;
	}
}
