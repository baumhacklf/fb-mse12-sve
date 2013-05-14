package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	private double amount;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Customer bidder;

	@Column(nullable = false)
	private Long articleId;
	
	private Date bidDate;
	
	private double lastBid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Customer getBidder() {
		return bidder;
	}

	public void setBidder(Customer bidder) {
		this.bidder = bidder;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Date getBidDate() {
		return bidDate;
	}

	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}

	public double getLastBid() {
		return lastBid;
	}

	public void setLastBid(double lastBid) {
		this.lastBid = lastBid;
	}
}
