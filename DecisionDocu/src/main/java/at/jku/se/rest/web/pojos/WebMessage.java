package at.jku.se.rest.web.pojos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.se.model.Message;
import at.jku.se.rest.response.ResponseData;

/**
 * Data container class to represent a message for web application
 */
public class WebMessage extends ResponseData {

	private static final Logger log = LogManager.getLogger(WebMessage.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	// ------------------------------------------------------------------------

	private String content;
	private String dateString;
	private String authorMail;

	// ------------------------------------------------------------------------

	public WebMessage() {
		this.content = "";
		this.dateString = "";
		this.authorMail = "";
	}

	// ------------------------------------------------------------------------

	/**
	 * Static helper method to generate a WebMessage with a given message
	 * 
	 * @param message
	 *            Message to convert to WebMessage
	 * @return WebMessage object
	 */
	public static WebMessage getWebMessage(Message message) {
		WebMessage result = new WebMessage();

		try {
			result.setId(String.valueOf(message.getId()));
			result.setContent(message.getName());
			result.setDateString(dateFormat.format(message.getCreationDate().getDate()));
			result.setAuthorMail(message.getAuthorEmail());
		} catch (Exception e) {
			log.error("Unable to convert message", e);
		}
		
		return result;
	}

	// ------------------------------------------------------------------------

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getAuthorMail() {
		return authorMail;
	}

	public void setAuthorMail(String authorMail) {
		this.authorMail = authorMail;
	}

}
