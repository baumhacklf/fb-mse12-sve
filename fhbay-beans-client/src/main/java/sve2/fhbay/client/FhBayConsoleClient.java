package sve2.fhbay.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import sve2.fhbay.domain.Address;
import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.CreditCard;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.domain.PaymentData;
import sve2.fhbay.domain.Phone;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.util.DateUtil;
import sve2.util.JndiUtil;
import sve2.util.LoggingUtil;

public class FhBayConsoleClient {

	private static CustomerAdminRemote custAdmin;
	private static ArticleAdminRemote artAdmin;
	private static AuctionRemote auctionAdmin;

	/*
	public static void main(String[] args) throws NamingException {
		LoggingUtil.initJdkLogging("logging.properties");

		custAdmin = JndiUtil
				.getRemoteObject(
						"fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote",
						CustomerAdminRemote.class);

		artAdmin = JndiUtil
				.getRemoteObject(
						"fhbay-beans/ArticleAdminBean!sve2.fhbay.interfaces.ArticleAdminRemote",
						ArticleAdminRemote.class);

		auctionAdmin = JndiUtil.getRemoteObject(
				"fhbay-beans/AuctionBean!sve2.fhbay.interfaces.AuctionRemote",
				AuctionRemote.class);

		testCustomerAdmin();
		System.out
				.println("ARTICLE TEST METHOD ------------------------------");
		testArticleAdmin();

		System.out
				.println("=============== testArticleProcessor ===============");
		testArticleProcessor();
	}
*/
	private static void testCustomerAdmin() {
		try {
			System.out.println("--------------- save customer ---------------");
			Customer cust1 = new Customer("Jaquira", "Hummelbrunner", "jaqui",
					"pwd", "Johann.Heinzelreiter@fh-hagenberg.at");
			cust1.setBillingAddress(new Address("4232", "Hagenberg",
					"Hauptstraﬂe 117"));
			cust1.addPhone("mobile", new Phone("+43", "(0) 555 333"));
			cust1.addShippingAddress(new Address("5555", "Mostbusch",
					"Linzerstraﬂe 15"));
			cust1.addShippingAddress(new Address("8050", "Kˆnigsbrunn",
					"Maisfeld 15"));
			cust1.addPaymentData(new CreditCard("Himmelbrunner", "010448812",
					DateUtil.getDate(2007, 07, 1)));

			Customer cust2 = new Customer("Maggi", "Weibold", "maggi", "wei",
					"Johann.Heinzelreiter@fh-hagenberg.at");
			cust2.setBillingAddress(new Address("4020", "Linz",
					"Hauptstraﬂe 117"));

			System.out
					.println("--------------- saveOrUpdateCustomer ---------------");

			Long cust1Id = custAdmin.saveCustomer(cust1);
			@SuppressWarnings("unused")
			Long cust2Id = custAdmin.saveCustomer(cust2);

			System.out
					.println("--------------- addShippingAddress ---------------");
			cust1 = custAdmin.findCustomerById(cust1Id);
			cust1.addShippingAddress(new Address("1000", "Wien",
					"Haudumgasse 87a"));
			cust1.addShippingAddress(new Address("5000", "Salzburg",
					"Moritzwinkel 5"));
			custAdmin.saveCustomer(cust1);

			System.out
					.println("--------------- findAllCustomers ---------------");
			for (Customer c : custAdmin.findAllCustomers()) {
				System.out.println(c);

				if (!c.getPhones().isEmpty()) {
					System.out.println("  phone numbers:");
					for (Entry<String, Phone> entry : c.getPhones().entrySet())
						System.out.println("     " + entry.getKey() + ": "
								+ entry.getValue());
				}
				if (!c.getShippingAddresses().isEmpty()) {
					System.out.println("  shipping addresses:");
					for (Address a : c.getShippingAddresses())
						System.out.println("     " + a);
				}
				if (!c.getPaymentData().isEmpty()) {
					System.out.println("  payment data:");
					for (PaymentData pd : c.getPaymentData())
						System.out.println("     " + pd);
				}
			}

			System.out
					.println("--------------- findCustomerById ---------------");
			System.out.println(custAdmin.findCustomerById(cust1.getId()));
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void testArticleAdmin() throws NamingException {
		Customer[] customers = custAdmin.findAllCustomers().toArray(
				new Customer[] {});
		if (customers.length <= 1)
			return;
		Long cust1Id = customers[0].getId();
		Long cust2Id = customers[1].getId();

		System.out.println("-------------------- start category saving ------");

		Category cat1 = new Category("Category 1");
		Category cat11 = new Category("Category 1.1");
		Category cat12 = new Category("Category 1.2");
		Category cat13 = new Category("Category 1.3");
		Category cat131 = new Category("Category 1.3.1");
		Category cat132 = new Category("Category 1.3.2");
		Category cat133 = new Category("Category 1.3.3");
		cat1.addSubCategory(cat11);
		cat1.addSubCategory(cat12);
		cat1.addSubCategory(cat13);
		cat13.addSubCategory(cat131);
		cat13.addSubCategory(cat132);
		cat13.addSubCategory(cat133);

		Category cat2 = new Category("Category 2");

		artAdmin.saveCategory(cat1);
		artAdmin.saveCategory(cat2);

		System.out.println("------------- saveArticle ----------------------");

		try {
			Date now = DateUtil.now();
			Article art1 = new Article("DDR2 ECC",
					"Neuwertiger Speicherbaustein", 100, now,
					DateUtil.addSeconds(now, 3));
			Long art1Id = artAdmin.offerArticle(art1, cust1Id);

			Article art2 = new Article("Samsung T166",
					"Samsung Spinpoint T166, 500GB, SATA", 150.99, now,
					DateUtil.addSeconds(now, 4));
			artAdmin.offerArticle(art2, cust2Id);

			Article art3 = new Article("OCZ Agility 2 T166",
					"SSD mit neuartiger Flash-Technologie", 768.99, now,
					DateUtil.addSeconds(now, 5));
			artAdmin.offerArticle(art3, cust2Id);

			System.out
					.println("------------- findArticleById ------------------");
			System.out.printf("art1=%s\n", artAdmin.findArticleById(art1Id));

			System.out
					.println("------------- findAllMatchingArticles ----------");
			System.out.println("Articles matching \"neu\"");
			Category category = artAdmin.findCategoryByName(cat1.getName());
			Collection<Article> matchingArticles = artAdmin
					.findAllMatchingArticles(category.getId(), "neu", true);

			if (matchingArticles != null && !matchingArticles.isEmpty())
				for (Article art : matchingArticles)
					System.out.printf("%s - %s%n", art, art.getDescription());
			else
				System.out.println("No matching artilces found.");

			System.out.println("------------- findCategoryById ----------");
			System.out.println("Category: "
					+ artAdmin.findCategoryById(category.getId()).toString());

			System.out.println("------------- findAllRootCategories----------");
			System.out.println("RootCategory Size: "
					+ artAdmin.findAllRootCategories().size());

			System.out
					.println("------------- Add Category to Article----------");
			ArrayList<Article> articlesCopy = new ArrayList<Article>(
					matchingArticles);
			artAdmin.assignArticleToCategory(articlesCopy.get(0).getId(),
					category.getId());
			System.out.println("RootCategory Size: "
					+ artAdmin.findAllRootCategories().size());

			System.out
					.println("------------- Find Tree Category By Id----------");
			Category lastCategory = artAdmin
					.findCategoryByName(cat13.getName());
			Category tree = artAdmin.findCategoryTree(lastCategory.getId());
			System.out.println(String.format("Category Tree from <%s> is <%s>",
					lastCategory, tree));

			System.out.println("------------- Find Article by name ---------");
			System.out.println(String.format("Article: <%s>",
					artAdmin.findArticleByName(art2.getName())));

			System.out
					.println("------------------ increase price -------------");
			// System.out.println(String.format("Price Before: <%s>", ));
			System.out.println(art1.getName());
			auctionAdmin.placeBid(art1.getName(), art1.getCurrentPrice() + 10,
					customers[0].getId());
//			System.out.println(String.format(
//					"New Price for Article <%s> is <%s>", auctionAdmin
//							.getBidInfo(art1.getId()).getArticleName(),
//					auctionAdmin.getBidInfo(art1.getId()).getHighestBid()));
			Article increasedArticle = artAdmin.findArticleByName(art1.getName());
			System.out.println(auctionAdmin.getBidInfo(increasedArticle.getId()));

		} catch (IdNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void articleToMessage(MapMessage msg, Article article)
			throws JMSException {
		msg.setString("name", article.getName());
		msg.setString("description", article.getDescription());
		msg.setDouble("initialPrice", article.getInitialPrice());
		msg.setLong("startDate", article.getStartDate().getTime());
		msg.setLong("endDate", article.getStartDate().getTime());
	}

	// add user (ApplicationRealm):
	// username=jboss,
	// password=ejb,
	// group=guest
	private static void testArticleProcessor() throws NamingException {
		String username = JndiUtil.getProperty(Context.SECURITY_PRINCIPAL);
		String password = JndiUtil.getProperty(Context.SECURITY_CREDENTIALS);

		try {
			Customer[] customers = custAdmin.findAllCustomers().toArray(
					new Customer[] {});
			if (customers.length == 0)
				return;
			Long sellerId = customers[0].getId();

			ConnectionFactory factory = JndiUtil.getRemoteObject(
					"jms/RemoteConnectionFactory", ConnectionFactory.class);
			Queue fhBayQueue = JndiUtil.getRemoteObject("jms/queue/FhBayQueue",
					Queue.class);

			Connection conn = factory.createConnection(username, password);
			Session session = conn.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(fhBayQueue);

			for (int i = 0; i < 30; i++) {
				Article article = new Article("Article " + i,
						"Superior Article-" + 1, 200.0 + i * 5, DateUtil.now(),
						DateUtil.addSeconds(DateUtil.now(), 5 + i));

				MapMessage articleMessage = session.createMapMessage();
				articleToMessage(articleMessage, article);

				articleMessage.setLong("sellerId", sellerId);

				producer.send(articleMessage);
				System.out.println(String.format(
						"Article <%s> sent to server.", article.getName()));
			}

			producer.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
