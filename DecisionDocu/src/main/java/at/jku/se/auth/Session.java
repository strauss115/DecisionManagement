package at.jku.se.auth;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import at.jku.se.model.User;

/**
 * Classe for user sessions
 * @author August
 *
 */
public class Session {

	private Date expiredate;
	private User user;

	// ------------------------------------------------------------------------

	/**
	 * Constructor of Session
	 * @param user User of the session
	 */
	public Session(User user) {
		this.user = user;
		this.expiredate = DateUtils.addHours(new Date(), 1);
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the Expiredate of the session
	 * @return The expiredate of the session
	 */
	public Date getExpiredate() {
		return expiredate;
	}

	/**
	 * Sets the expiredate of the session
	 * @param expiredate The expiredate of the session
	 */
	public void setExpiredate(Date expiredate) {
		this.expiredate = expiredate;
	}

	/**
	 * Returns the user of the session
	 * @return The user object of the session
	 */
	public User getUser() {
		return user;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the String representation of the session object
	 * @return The string representation of the session object
	 */
	@Override
	public String toString() {
		return "Session [expiredate=" + expiredate + ", user=" + user + "]";
	}
}
