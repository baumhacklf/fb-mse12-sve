package sve2.fhbay.interfaces;

import java.util.List;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.interfaces.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.exceptions.NameNotFoundException;

public interface ArticleAdmin {
	Article findArticleById(Long id) throws IdNotFoundException;
	
	Article findArticleByName(String name) throws NameNotFoundException;

	List<Article> findAllMatchingArticles(Long categoryId, String pattern,
			boolean includeSubCategories) throws IdNotFoundException;

	Long offerArticle(Article article, Long sellerId)
			throws IdNotFoundException;

	void assignArticleToCategory(Long articleId, Long categoryId)
			throws IdNotFoundException;

	Long saveCategory(Category category);

	Category findCategoryById(Long id) throws IdNotFoundException;

	Category findCategoryByName(String name) throws NameNotFoundException;

	List<Category> findAllRootCategories();

	Category findCategoryTree(Long categoryId) throws IdNotFoundException;
}