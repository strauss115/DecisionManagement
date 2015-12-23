package at.jku.se.decisiondocu.restclient.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomDate {
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd-HH:mm:ss.SSS")
	private Date date;
	
	public CustomDate(){
		date = new Date();
	}
	
	public CustomDate(Date date) {
		super();
		this.date = date;
	}

	public CustomDate(long l) {
		date = new Date(l);
	}
	
	public long dateToLong(){
		return date.getTime();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {

		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
		return formate.format(getDate());
	}

	public String yyyyMMdd() {
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formate.format(getDate());
	}

}
