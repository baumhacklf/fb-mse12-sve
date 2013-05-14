package sve2.fhbay.beans;

import java.util.Date;
import java.util.Random;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import sve2.fhbay.domain.Article;
import sve2.fhbay.interfaces.ArticleAdminLocal;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/FhBayQueue") })
public class ArticleProcessorBean implements MessageListener {

	@EJB
	private ArticleAdminLocal articleAdmin;

	private Random random = new Random();

	@Override
	public void onMessage(Message message) {
		MapMessage articleMsg = (MapMessage) message;
		try {
			Article article = messageToArticle(articleMsg);
			Long sellerId = articleMsg.getLong("sellerId");

			// TESTCODE
			System.out.println(String.format(
					"==========> started processing of article <%s> started",
					article.getName()));
			Thread.sleep(5000 + random.nextInt(3000));
			// TESTCODE END

			Long categoryId = articleMsg.getLong("categoryId");

			articleAdmin.offerArticle(article, sellerId);
			articleAdmin.assignArticleToCategory(article.getId(), categoryId);
			
			System.out.println(String.format(
					"<=========== processing of article <%s> with id <%s> finished",
					article.getName(), article.getId()));

		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	private Article messageToArticle(MapMessage msg) throws JMSException {
		Article article = new Article();
		article.setName(msg.getString("name"));
		article.setDescription(msg.getString("description"));
		article.setStartDate(new Date(msg.getLong("startDate")));
		article.setEndDate(new Date(msg.getLong("endDate")));
		article.setInitialPrice(msg.getDouble("initialPrice"));
		return article;
	}

}
