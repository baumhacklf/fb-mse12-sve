package sve2.fhbay.interfaces.dao;

import java.util.Set;

import javax.ejb.Local;

import sve2.fhbay.domain.Article;

@Local
public interface ArticleDao extends Dao<Article, Long> {

	Set<Article> findByCategoryAndPattern(Long categoryId, String pattern);

	Article findByName(String name);
	
}
