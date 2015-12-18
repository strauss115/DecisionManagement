package at.jku.se.auth;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import at.jku.se.model.User;

public class Session {

	private Date expiredate;
	private User user;

	// ------------------------------------------------------------------------

	public Session(User user) {
		this.user = user;
		this.expiredate = DateUtils.addHours(new Date(), 1);
	}

	// ------------------------------------------------------------------------

	public Date getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(Date expiredate) {
		this.expiredate = expiredate;
	}

	public User getUser() {
		return user;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Session [expiredate=" + expiredate + ", user=" + user + "]";
	}
}
