package sve2.fhbay.dto;

import java.io.Serializable;
import java.util.Date;

import sve2.fhbay.domain.Article;

public class BidInfoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String articleName;
	private int bidCount;
	private double highestBid;
	private Date endDate;
	private String sellerName;

	public BidInfoDto(Article article) {
		super();
		this.articleName = article.getName();
		this.bidCount = article.getBids().size();
		this.highestBid = article.getHighestBid();
		this.endDate = article.getEndDate();
		this.sellerName = article.getSeller().getUsername();
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getBidCount() {
		return bidCount;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	public double getHighestBid() {
		return highestBid;
	}

	public void setHighestBid(double highestBid) {
		this.highestBid = highestBid;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + articleName);
		sb.append(" Highest Bid: " + highestBid);
		return sb.toString();
	}

}
