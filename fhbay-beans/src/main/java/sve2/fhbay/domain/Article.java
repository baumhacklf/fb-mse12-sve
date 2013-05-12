package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name = "qryFindByCategoryAndPattern", query = "select a from Article a where lower(a.name) like :pattern or "
		+ "lower(a.description) like :pattern")
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
}
