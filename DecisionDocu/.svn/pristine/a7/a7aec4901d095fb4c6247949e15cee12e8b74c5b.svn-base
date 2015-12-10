package at.jku.se.auth;

import java.util.Calendar;
import java.util.Date;

import at.jku.se.model.User;

public class Session {

	Date expiredate;
	User user;
	
	public Session(User user){
		this.user = user;
		expiredate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(expiredate);
		cal.add(Calendar.HOUR_OF_DAY, 1);
		expiredate = cal.getTime();
	}

	public Date getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(Date expiredate) {
		this.expiredate = expiredate;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "Session [expiredate=" + expiredate + ", user=" + user + "]";
	}
}
