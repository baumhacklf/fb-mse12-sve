package sve2.fhbay.beans.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import sve2.fhbay.domain.Article;
import sve2.fhbay.interfaces.dao.ArticleDao;

@Stateless
public class ArticleDaoBean extends AbstractDaoBean<Article, Long> implements
		ArticleDao {

	@Override
	public Set<Article> findByCategoryAndPattern(Long categoryId, String pattern) {
//		TypedQuery<Article> qry = getEntityManager().createQuery(
				// "select a from article a, in(a.categories) c ... where c.id = :catId AND ( .. )
//				 "select a from article a, inner join a.categories c where c.id = :catId AND ( .. )
//				"select a from Article a where lower(a.name) like :pattern or "
//						+ "lower(a.description) like :pattern", Article.class);
		
		TypedQuery<Article> qry = getEntityManager().createNamedQuery("qryFindByCategoryAndPattern", Article.class);
		qry.setParameter("pattern", "%" + pattern.toLowerCase() + "%");
		
		return new HashSet<>(qry.getResultList());
	}

	@Override
	public Article findByName(String name) {
		TypedQuery<Article> qry = getEntityManager().createNamedQuery("qryFindArticleByName", Article.class);
		qry.setParameter("pattern", "%" + name.toLowerCase() + "%");
		return qry.getResultList().get(0);
	}

}
