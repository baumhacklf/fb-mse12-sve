package sve2.fhbay.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.naming.NamingException;

import sve2.fhbay.domain.Address;
import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.CreditCard;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.domain.Phone;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.exceptions.ArticleBidException;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;
import sve2.util.DateUtil;
import sve2.util.JndiUtil;

public class ConsoleClient {

	private static BufferedReader br;

	private static CustomerAdminRemote custAdmin;
	private static ArticleAdminRemote artAdmin;
	private static AuctionRemote auctionAdmin;

	private static Customer user;

	public static void main(String[] args) throws IOException, ParseException {
		try {
			jndiSetup();
		} catch (NamingException n) {
			System.out.println("JNDI Lookup unsuccessful.");
		}

		userSetup();
		mainPage();
	}

	private static void mainPage() throws IOException, ParseException {
		System.out.println("Welcome to FHBay Console Client.");
		br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder menu = new StringBuilder();

		while (true) {
			if (user == null) {
				menu.append("You are not logged in.\n");
				menu.append("\tR Login\n");
				menu.append("\tK list all root categories with sub categories\n");
				menu.append("\tP find article by pattern and (sub) category\n");
				menu.append("\tS select a specific article\n");
				menu.append("\tE exit programm\n");
				
				System.out.println(menu.toString());
				System.out.println("Your Choice: ");
				String choice = br.readLine();
				
				if(choice.equalsIgnoreCase("r"))
					login();
				else if(choice.equalsIgnoreCase("k"))
					listCategories(true);
				else if(choice.equalsIgnoreCase("p"))
					findArticle();
				else if(choice.equalsIgnoreCase("s"))
					showConcreteArticle();
				else if(choice.equalsIgnoreCase("e"))
					quitProgram();
			} else {
				menu.append("Following options are available: \n");
				menu.append("\tC for listing all categories\n");
				menu.append("\tR for all root categories\n");
				menu.append("\tS for search all auctions\n");
				menu.append("\tL for list all auctions\n");
				menu.append("\tP find article by pattern and (sub) category\n");
				menu.append("\tE logg off");
				menu.append("\tQ quits the program\n");
				
				System.out.println(menu.toString());
				System.out.println("Your Choice: ");
				String choice = br.readLine();

				if (choice.equalsIgnoreCase("q"))
					quitProgram();
				else if (choice.equalsIgnoreCase("e"))
					loggOff();
				else if (choice.equalsIgnoreCase("c"))
					listCategories(false);
				else if(choice.equalsIgnoreCase("r"))
					listCategories(true);
				else if(choice.equalsIgnoreCase("s"))
					showArticles();
				else if(choice.equalsIgnoreCase("p"))
					findArticle();
				else if(choice.equalsIgnoreCase("l"))
					showConcreteArticle();
			}
		}
	}
	
	private static void login() throws IOException {
		System.out.println("Enter your username: ");
		String username = br.readLine();
		System.out.println("Enter your password: ");
		String password = br.readLine();
		
		try {
			user = custAdmin.loginCustomer(username, password);
		} catch (Exception e) {
			System.out.println("Authentication failed.");
		}
	}

	private static void listCategories(boolean showRootCategories) throws IOException, ParseException {
		StringBuilder sb = new StringBuilder();
		
		Collection<Category> categories;
		if(showRootCategories) {
			categories = artAdmin.findAllRootCategories();
		} else {
			categories = artAdmin.findAllCategories();
		}

		for (Category c : categories) {
			sb.append(c.getName());
			sb.append("\n");
		}
		
		sb.append("\tR returns to main menu\n");
//		sb.append("\tA add a category\n");
//		sb.append("\tR remove a category\n");
//		sb.append("\tB show all subcategories of a specific category\n");
//		sb.append("\tS show all articles of the category and subcategories\n");
		sb.append("\tQ quits the program\n");
		sb.append("\tE logg off\n");
		System.out.println(sb.toString());
		System.out.println("Your Choice: ");
		String choice = br.readLine();

		if (choice.equalsIgnoreCase("q"))
			quitProgram();
		else if (choice.equalsIgnoreCase("r"))
			mainPage();
		else if (choice.equalsIgnoreCase("e"))
			loggOff();
//		else if (choice.equalsIgnoreCase("s"))
//			showArticles();
//		else if (choice.equalsIgnoreCase("r"))
//			removeCategory();
//		else if (choice.equalsIgnoreCase("a"))
//			addCategory();
//		else if(choice.equalsIgnoreCase("b"))
//			showSubcategories();

	}

	private static void findArticle() throws IOException {
		System.out.println("Define your pattern: ");
		String pattern = br.readLine();
		System.out.println("Specify your category: ");
		String categoryName = br.readLine();
		
		StringBuilder sb = new StringBuilder();
		
		try {
			Category category = artAdmin.findCategoryByName(categoryName);
			Collection<Article> articles = artAdmin.findAllMatchingArticles(category.getId(), pattern, true);
			for(Article a : articles) {
				sb.append(a.getName() + "\n");
			}
		} catch (NameNotFoundException e) {
			System.out.println("Category Name wasn't found.");
		} catch (IdNotFoundException e) {
			System.out.println("Category id wasn't found.");
		}

		sb.append("\n");
		sb.append("\tR returns to main menu\n");
		sb.append("\tQ quits the program\n");
		sb.append("\tE logg off\n");
		System.out.println(sb.toString());
	}

	private static void showSubcategories() throws IOException {
		StringBuilder sb = new StringBuilder();
		
		System.out.println("Name category to list subcategories; ");
		String categoryName = br.readLine();
		
		try {
			Category category = artAdmin.findCategoryByName(categoryName);
			for(Category c : category.getSubCategories()) {
				sb.append("\t" + c.getName() + "\n");
			}
		} catch (NameNotFoundException e) {
			System.out.println("category wasn't found.");
		}
		sb.append("\n");
//		sb.append("\tR returns to main menu\n");
//		sb.append("\tA add a category\n");
//		sb.append("\tR remove a category\n");
//		sb.append("\tB show all subcategories of a specific category\n");
//		sb.append("\tS show all articles of the category and subcategories\n");
//		sb.append("\tQ quits the program\n");
//		sb.append("\tE logg off\n");
		System.out.println(sb.toString());
	}

	private static void addCategory() throws IOException {
		System.out.println("Name the new category: \n");
		String categoryName = br.readLine();

		Category category = new Category();
		category.setName(categoryName);

		System.out
				.println("Is there a parent category? (type the name of the parent category)");
		String parentCategory = br.readLine();
		if (parentCategory.length() > 0) {
			Category parent;
			try {
				parent = artAdmin.findCategoryByName(parentCategory);
			} catch (Exception e) {
				System.out.println("No parent category found.");
				parent = null;
			}
			if (parent != null)
				category.setParentCategoryId(parent);
		}
		artAdmin.saveCategory(category);
	}

	private static void showArticles() throws IOException, ParseException {
		StringBuilder sb = new StringBuilder();
		Collection<Article> articles = artAdmin.findAllArticles();
		for(Article a : articles) {
			sb.append("In " + a.getCategories().size() + " different categories. ");
			sb.append(a.getName() + ": ");
			sb.append(a.getEndDate() + "(End-Date)\n");
		}
		sb.append("\tS buy a specific article\n");
		sb.append("\tA add article\n");
		sb.append("\tG get concrete article\n");
		sb.append("\tF search article by pattern and category\n");
		sb.append("\tB bid on article\n");
		sb.append("\tQ quits the program\n");
		sb.append("\tE logg off\n");
		System.out.println(sb.toString());
		
		String choice = br.readLine();
		
		if(choice.equalsIgnoreCase("a"))
			addArticle();
		else if(choice.equalsIgnoreCase("g"))
			showConcreteArticle();
		else if(choice.equalsIgnoreCase("f"))
			findArticle();
		else if(choice.equalsIgnoreCase("b"))
			bidOnArticle();
		else if(choice.equalsIgnoreCase("q"))
			quitProgram();
		else if(choice.equalsIgnoreCase("e"))
			loggOff();
		else if(choice.equalsIgnoreCase("s"))
			bidOnArticle();
//		else if(choice.equalsIgnoreCase("r"))
//		removeArticle();
	}

	private static void showConcreteArticle() throws IOException {
		System.out.println("Select concrete article");
		StringBuilder sb = new StringBuilder();
		Collection<Article> articles = artAdmin.findAllArticles();
		for(Article a : articles) {
			sb.append("Name: " + a.getName() + "\n");
		}
		System.out.println(sb.toString());
		
		String articleName = br.readLine();
		sb = new StringBuilder();
		
		try {
			Article a = artAdmin.findArticleByName(articleName);
			sb.append("Current Price: " + a.getCurrentPrice() + "\n");
			sb.append("Bid Size: " + a.getBids().size() + "\n");
			
			int totalMinutes = (int) ((a.getEndDate().getTime() - DateUtil.now().getTime()) / 60000);
			int days = totalMinutes / (24 * 60);
			totalMinutes = totalMinutes % (24 * 60);
			int hours = totalMinutes / 60;
			int minutes = totalMinutes % 60;
			
			sb.append("Remaining Time: " + days + "d " + hours + "h " + minutes + "m\n");
			sb.append("Seller: " + a.getSeller());
		} catch(NameNotFoundException e) {
			System.out.println("Article with given name wasn't found.");
		}
		
		sb.append("\n");
//		sb.append("\tR returns to main menu\n");
		sb.append("\tQ quits the program\n");
		sb.append("\tE logg off\n");
		System.out.println(sb.toString());
		
		String choice = br.readLine();
		
//		if(choice.equalsIgnoreCase("r"))
//			removeArticle();
		if(choice.equalsIgnoreCase("q"))
			quitProgram();
		else if(choice.equalsIgnoreCase("e"))
			loggOff();
		
	}

	private static void bidOnArticle() throws IOException {
		System.out.println("Which article you want to bid.");
		String articleName = br.readLine();
		try {
			Article article = artAdmin.findArticleByName(articleName);
			System.out.println("Minimum Bid: " + article.getMinimumBid() + " your bid:");
			Double bid = Double.parseDouble(br.readLine());
			auctionAdmin.placeBid(articleName, bid, user.getId());
		} catch (NameNotFoundException e) {
			System.out.println("Article wasn't found with the given name.");
		} catch (ArticleBidException e) {
			System.out.println("Your bid was to low.");
		} catch (IdNotFoundException e) {
			System.out.println("Article wasn't found.");
		}
	}

	private static void addArticle() throws IOException, ParseException {
		System.out.println("Name your article: ");
		String articleName = br.readLine();
		System.out.println("Write your description: ");
		String articleDescription = br.readLine();
		System.out.println("Initial Price: ");
		Double articleInitialPrice = Double.parseDouble(br.readLine());
		System.out.println("Start Date: ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date articleStartDate = sdf.parse(br.readLine());
		System.out.println("End Date: ");
		Date articleEndDate = sdf.parse(br.readLine());
		
		Article article = new Article(articleName, articleDescription, articleInitialPrice, 
				articleStartDate, articleEndDate);
		try {
			artAdmin.offerArticle(article, user.getId());
		} catch (IdNotFoundException e) {
			System.out.println("User not found.");
		}
		
		System.out.println("Article was added successfully. Which category should be added?");
		String articleCategoryName = br.readLine();
		Article savedArticle;
		try {
			savedArticle = artAdmin.findArticleByName(articleName);
			Category articleCategory = artAdmin.findCategoryByName(articleCategoryName);
			artAdmin.assignArticleToCategory(savedArticle.getId(), articleCategory.getId());
		} catch (NameNotFoundException e1) {
			System.out.println("article name is incorrect.");
		} catch (IdNotFoundException e) {
			System.out.println("Id wasn't found.");
		}
	}

	private static void jndiSetup() throws NamingException {
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
	}

	private static void userSetup() {
		Customer temp;
		try {
			temp = custAdmin.findCustomerByUsername("maxi");
		} catch (Exception e) {
			temp = null;
		}

		if (temp == null) {
			Customer customer = new Customer("Max", "Mustermann", "maxi",
					"pwd", "max@mustermann.at");
			customer.setBillingAddress(new Address("4232", "Hagenberg",
					"Hauptstraße 117"));
			customer.addPhone("mobile", new Phone("+43", "(0) 555 333"));
			customer.addShippingAddress(new Address("5555", "Mostbusch",
					"Linzerstraße 15"));
			customer.addShippingAddress(new Address("8050", "Königsbrunn",
					"Maisfeld 15"));
			customer.addPaymentData(new CreditCard("Himmelbrunner",
					"010448812", DateUtil.getDate(2007, 07, 1)));

			custAdmin.saveCustomer(customer);
		}
	}

	private static void quitProgram() {
		user = null;
		System.out.println("Successfully logged off.");
		System.exit(0);
	}

	private static void loggOff() {
		user = null;
	}

}
