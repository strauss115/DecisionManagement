package at.jku.se.rest.web.pojos;

public class DecisionGraphAssociation {

	private int from;
	private int to;

	/** 
	 * Constructor
	 * @param from
	 * @param to
	 */
	public DecisionGraphAssociation(int from, int to) {
		super();
		this.from = from;
		this.to = to;
	}

	/**
	 * Getter for from
	 * @return {@link Integer}
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * Setter for from
	 * @param from
	 */
	public void setFrom(int from) {
		this.from = from;
	}

	/**
	 * Getter for to
	 * @return {@link Integer}
	 */
	public int getTo() {
		return to;
	}

	/**
	 * Setter for to
	 * @param to
	 */
	public void setTo(int to) {
		this.to = to;
	}

}
