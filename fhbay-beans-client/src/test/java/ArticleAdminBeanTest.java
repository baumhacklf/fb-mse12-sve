import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.exceptions.ArticleBidException;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;
import sve2.util.DateUtil;
import sve2.util.JndiUtil;

public class ArticleAdminBeanTest {
	private static ArticleAdminRemote artAdmin;
	private static CustomerAdminRemote custAdmin;
	private static AuctionRemote auctionAdmin;
	
	private static Long sellerId;
	private static Long articleId;
	private static Long categoryId;
	
	private static final String ARTICLE_NAME = "Monitor";
	private static final String CATEGORY_NAME = "Computer";

	@BeforeClass
	public static void setup() {
		try {
			artAdmin = JndiUtil
					.getRemoteObject(
							"fhbay-beans/ArticleAdminBean!sve2.fhbay.interfaces.ArticleAdminRemote",
							ArticleAdminRemote.class);
			custAdmin = JndiUtil
					.getRemoteObject(
							"fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote",
							CustomerAdminRemote.class);
			auctionAdmin = JndiUtil.getRemoteObject(
					"fhbay-beans/AuctionBean!sve2.fhbay.interfaces.AuctionRemote",
					AuctionRemote.class);
		} catch (NamingException e) {
			System.out.println("Wasn't able to get Beans.");
		}
		
		/*
		 * Create Customer
		 */
		Customer customer = new Customer("Max", "Mustermann", "maxi", "pwd", "max@mustermann.at");
		sellerId = custAdmin.saveCustomer(customer);
	}
	
	@Test
	public void offerAndFindArticle() {
		Assert.assertNotNull(offerArticle());
		Assert.assertNotNull(createCategory());
		Assert.assertNotNull(findCategoryByName());
		Assert.assertEquals(1, assignArticleToCategory());
		Assert.assertNotNull(findArticle());
		Assert.assertNotNull(findArticleWithPattern());
		/*
		 * 125 = starting value
		 */
		Assert.assertTrue(126 == bidOnArticle());
		
		/*
		 * 1 article added
		 */
		Assert.assertTrue(1 == listAllArticles());
		
		/*
		 * 1 group added
		 */
		Assert.assertTrue(1 == showAllCategories());
		
		/*
		 * only 1 group is root group too
		 */
		Assert.assertTrue(1 == showRootCategories());
	}
	
	private int assignArticleToCategory() {
		try {
			artAdmin.assignArticleToCategory(articleId, categoryId);
		} catch (IdNotFoundException e) {
			System.out.println("Entity with given id not found.");
		}
		int result = 0;
		try {
			result = artAdmin.findArticleById(articleId).getCategories().size();
		} catch (IdNotFoundException e) {
			System.out.println("Id not found...");
		}
		return result;
	}

	private Object findCategoryByName() {
		Category category = null;
		try {
			category = artAdmin.findCategoryByName(CATEGORY_NAME);
		} catch (NameNotFoundException e) {
			System.out.println("No category found with given name.");
		}
		return category;
	}

	private Long createCategory() {
		Category category = new Category(CATEGORY_NAME);
		categoryId = artAdmin.saveCategory(category);
		return categoryId;
	}

	private Long offerArticle() {
		articleId = null;
		@SuppressWarnings("deprecation")
		Article article = new Article(ARTICLE_NAME, "Computer Monitor", 125.0, DateUtil.now(), new Date(2013, 05, 20));
		try {
			articleId = artAdmin.offerArticle(article, sellerId);
		} catch (IdNotFoundException e) {
			System.out.println("Article not found.");
		}
		return articleId;
	}
	
	private Article findArticle() {
		Article result = null;
		try {
			result = artAdmin.findArticleByName(ARTICLE_NAME);
		} catch (NameNotFoundException e) {
			System.out.println("name not found");
		}
		return result;
	}
	
	private Article findArticleWithPattern() {
		List<Article> result = null;
		
		try {
			result = artAdmin.findAllMatchingArticles(categoryId, "mon", true);
		} catch (IdNotFoundException e) {
			System.out.println("Category Id Not found");
		}
		
		return result.get(0);
	}
	
	private double bidOnArticle() {
		double result = 0;
		try {
			auctionAdmin.placeBid(ARTICLE_NAME, 130, sellerId);
			result = artAdmin.findArticleByName(ARTICLE_NAME).getCurrentPrice();
		} catch (NameNotFoundException e) {
			System.out.println("article with name wasn't found.");
		} catch (ArticleBidException e) {
			System.out.println("bid was to low.");
		} catch (IdNotFoundException e) {
			System.out.println("user id not found.");
		}
		
		return result;
	}
	
	private int showRootCategories() {
		return artAdmin.findAllRootCategories().size();
	}
	
	private int showAllCategories() {
		return artAdmin.findAllCategories().size();
	}
	
	private int listAllArticles() {
		return artAdmin.findAllArticles().size();
	}
}
