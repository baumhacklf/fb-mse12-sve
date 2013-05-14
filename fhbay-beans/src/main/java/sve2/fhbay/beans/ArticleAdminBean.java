package sve2.fhbay.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.ArticleAdminLocal;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CategoryDao;
import sve2.fhbay.interfaces.dao.CustomerDao;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

@Stateless
public class ArticleAdminBean implements ArticleAdminLocal, ArticleAdminRemote {

	@EJB
	private CustomerDao customerDao;

	@EJB
	private ArticleDao articleDao;

	@EJB
	private AuctionLocal auction;

	@EJB
	private CategoryDao categoryDao;

	@Override
	public Article findArticleById(Long id) throws IdNotFoundException {
		Article article = articleDao.findById(id);
		if (article == null)
			throw new IdNotFoundException(id, "Article");
		return article;
	}

	@Override
	public List<Article> findAllMatchingArticles(Long categoryId,
			String pattern, boolean includeSubCategories)
			throws IdNotFoundException {

		Category category = categoryDao.findById(categoryId);
		return new ArrayList<Article>(findAllMatchingArticles(category,
				pattern, includeSubCategories));
	}

	private Set<Article> findAllMatchingArticles(Category category,
			String pattern, boolean includeSubCategories)
			throws IdNotFoundException {

		Set<Article> matchingArticles = articleDao.findByCategoryAndPattern(
				category.getId(), pattern);

		if (includeSubCategories) {
			for (Category subCat : category.getSubCategories()) {
				matchingArticles.addAll(findAllMatchingArticles(subCat.getId(),
						pattern, includeSubCategories));
			}
		}

		return matchingArticles;
	}

	@Override
	public Long offerArticle(Article article, Long sellerId)
			throws IdNotFoundException {
		Customer seller = customerDao.findById(sellerId);
		if (seller == null)
			throw new IdNotFoundException(sellerId, "Customer");

		article.setSeller(seller);
		articleDao.persist(article);

		// start auction timer
		auction.addAuctionFinishTimer(article);

		return article.getId();
	}

	@Override
	public Long saveCategory(Category category) {
		if(category.getParentCategoryId() != null)
		{
			Category parent = categoryDao.findById(category.getParentCategoryId().getId());
			parent.addSubCategory(category);
			categoryDao.persist(parent);
		}
		categoryDao.persist(category);
		return category.getId();
	}

	@Override
	public Category findCategoryByName(String name) throws NameNotFoundException {
		Category category = categoryDao.findByName(name);

		if (category == null)
			throw new NameNotFoundException(name, "Category");

		return category;
	}

	@Override
	public Category findCategoryById(Long id) throws IdNotFoundException {
		Category category = categoryDao.findById(id);

		if (category == null)
			throw new IdNotFoundException(id, "Categorie");

		return category;
	}

	@Override
	public List<Category> findAllRootCategories() {
		List<Category> rootCategories = categoryDao.findAllRootCategories();
		return rootCategories;
	}

	@Override
	public void assignArticleToCategory(Long articleId, Long categoryId)
			throws IdNotFoundException {
		Category category = findCategoryById(categoryId);
		Article article = findArticleById(articleId);

		article.addCategory(category);
		articleDao.persist(article);
	}

	@Override
	public Category findCategoryTree(Long categoryId)
			throws IdNotFoundException {

		Category currentCategory = findCategoryById(categoryId);

		if (currentCategory.getParentCategoryId() != null)
			currentCategory = findCategoryTree(currentCategory
					.getParentCategoryId().getId());

		return currentCategory;
	}

	@Override
	public Article findArticleByName(String name) throws NameNotFoundException {
		Article article = articleDao.findByName(name);
		return article;
	}

	@Override
	public Collection<Category> findAllCategories() {
		return categoryDao.findAll();
	}

	@Override
	public Collection<Article> findAllArticles() {
		return articleDao.findAll();
	}

}
