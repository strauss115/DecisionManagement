package at.jku.se.rest.web.pojos;

public class DecisionGraphNode {

	private int key;
	private int parent;
	private String text;
	private String brush;
	private String dir;
	private String loc;

	/**
	 * Constructor
	 * @param key
	 * @param parent
	 * @param text
	 * @param bruch
	 * @param dir
	 * @param loc
	 */
	public DecisionGraphNode(int key, int parent, String text, String bruch, String dir, String loc) {
		this.key = key;
		this.parent = parent;
		this.text = text;
		this.brush = bruch;
		this.dir = dir;
		this.loc = loc;
	}

	/**
	 * Default constructor
	 */
	public DecisionGraphNode() {
		text = "";
		brush = "";
		dir = "";
	}

	/**
	 * Getter
	 * @return {@link Integer}
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Setter
	 * @param key
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * Getter
	 * @return {@link Integer}
	 */
	public int getParent() {
		return parent;
	}

	/**
	 * Setter
	 * @param parent
	 */
	public void setParent(int parent) {
		this.parent = parent;
	}

	/**
	 * Getter
	 * @return {@link String}
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Getter
	 * @return {@link String}
	 */
	public String getBrush() {
		return brush;
	}

	/**
	 * Setter
	 * @param brush
	 */
	public void setBrush(String brush) {
		this.brush = brush;
	}

	/**
	 * Getter
	 * @return {@link String}
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * Setter
	 * @param dir
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Getter
	 * @return {@link String}
	 */
	public String getLoc() {
		return loc;
	}

	/**
	 * Setter
	 * @param loc
	 */
	public void setLoc(String loc) {
		this.loc = loc;
	}

}
