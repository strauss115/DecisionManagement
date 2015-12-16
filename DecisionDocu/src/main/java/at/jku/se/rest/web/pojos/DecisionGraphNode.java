package at.jku.se.rest.web.pojos;

public class DecisionGraphNode {

	private int key;
	private int parent;
	private String text;
	private String brush;
	private String dir;
	private String loc;

	public DecisionGraphNode(int key, int parent, String text, String bruch, String dir, String loc) {
		this.key = key;
		this.parent = parent;
		this.text = text;
		this.brush = bruch;
		this.dir = dir;
		this.loc = loc;
	}

	public DecisionGraphNode() {
		text = "";
		brush = "";
		dir = "";
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBrush() {
		return brush;
	}

	public void setBrush(String brush) {
		this.brush = brush;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

}
