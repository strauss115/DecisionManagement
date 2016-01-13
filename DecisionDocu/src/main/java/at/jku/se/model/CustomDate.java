package at.jku.se.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Class CustomDate
 * @author August
 *
 */
public class CustomDate {
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd-HH:mm:ss.SSS")
	private Date date;
	
	/**
	 * Default constructor
	 */
	public CustomDate(){
		date = new Date();
	}
	
	/**
	 * Constructor
	 * @param date Date as date object
	 */
	public CustomDate(Date date) {
		super();
		this.date = date;
	}

	/**
	 * Constructor
	 * @param l Date as long value
	 */
	public CustomDate(long l) {
		date = new Date(l);
	}
	
	/**
	 * Returns the date as long value
	 * @return The date as long value
	 */
	public long dateToLong(){
		return date.getTime();
	}

	/**
	 * Returns the date
	 * @return Returns the date as date object
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the date object as string reprasentation
	 * @return The date object as string reprasentation
	 */
	@Override
	public String toString() {

		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
		return formate.format(getDate());
	}

}
