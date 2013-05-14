package sve2.fhbay.interfaces;

import java.util.Collection;

import javax.ejb.Remote;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;

@Remote
public interface ArticleAdminRemote extends ArticleAdmin {
	Collection<Category> findAllCategories();
	Collection<Article> findAllArticles();
}
