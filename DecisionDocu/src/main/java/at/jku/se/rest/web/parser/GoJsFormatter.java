package at.jku.se.rest.web.parser;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.*;

import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.InfluenceFactor;
import at.jku.se.model.QualityAttribute;
import at.jku.se.model.Rationale;
import at.jku.se.rest.web.pojos.*;


/**
 * @author apa
 *
 */


public class GoJsFormatter {
	
	//private String[] colors ={"green", "orange", "black", "red"}; //used later for color rotation
	private  JSONArray goJSConnects = new JSONArray();
	private  JSONArray goJSNodes = new JSONArray();
	private  String drawDirection = "";
	private  int xPosFactor = 1;
	private static String resultJSON = "";
	

	@SuppressWarnings("unchecked")
	private void addConnection(int from, int to){
		JSONObject obj = new JSONObject();
		  obj.put("from", from);
		  obj.put("to", to);
		  goJSConnects.add(obj);
		  //System.out.println(obj);
	}

	/**
	 * Adds a parametrized node to the Go JS graph with location attibutes and parent dependency.
	 * Also adds the connections between nodes.
	 * Parameters brush and dir are added automatically, not by passing as arguments  
	 * 
	 * @param id 		id of the Node which is added to the json array 
	 * @param parent	references parent node, omitted for the root node with id 0 
	 * @param name		text description of the graph node
	 * @param pos		position of the node in absolute format in pixels "x y". Center "0 0" is the center of the screen.
	 */
	@SuppressWarnings("unchecked")
	private void addNode(int id, int parent, String name, String pos) {

		JSONObject obj = new JSONObject();
		obj.put("loc", pos);
		if (id > 0) obj.put("dir", drawDirection);
		obj.put("brush", "black");
		obj.put("text", name);
		if (id > 0) obj.put("parent", parent);
		obj.put("key", id);

		goJSNodes.add(obj);
		if (id>0) addConnection(parent, id);
		
	}
	
	@SuppressWarnings("unused")
	private JSONObject getSampleData(){
		JSONParser parser=new JSONParser();
		JSONObject array = new JSONObject();
		
		String s = "{\"name\":\"decision1 very very long name\", \"children\": ["+ 
				"{\"name\":\"factor 1\", \"children\":[{\"name\":\"f1 child 1\"},{\"name\":\"f1 child 2\"},{\"name\":\"f1 child 3\"},{\"name\":\"f1 child 4\"} ]},"+
				"{\"name\":\"factor 2 longname\", \"children\":[{\"name\":\"f2 child 1 very long name\"},{\"name\":\"f2 child 2\"},{\"name\":\"f2 child 3\"} ]},"+
				"{\"name\":\"factor 3\", \"children\":[{\"name\":\"f3 child 1\"},{\"name\":\"f3 child 2\"},{\"name\":\"f3 child 3\"} ]},"+
				"{\"name\":\"factor 4 very long name loooong\", \"children\":[{\"name\":\"shorty\"},{\"name\":\"f4 child 2\"},{\"name\":\"f4 child 3\","+ 
				"	\"children\":[{\"name\":\"f4 child 3 level3 1\"}, {\"name\":\"f4 child 3 level3 2\"}, {\"name\":\"f4 child 3 level3 3\"}]} ]}"+
				"]}";
		
		try{
			Object obj=parser.parse(s);
			array=(JSONObject)obj;
		  }
		  catch(ParseException pe){
		    System.out.println("position: " + pe.getPosition());
		    System.out.println(pe);
		  }
		
		return array;
		
	}
	
	
	/**
	 * Resets the drawing direction after 2nd node of first level.
	 * First two nodes are drawn on the right side of the root,
	 * others on the left side.
	 * 
	 * @param level
	 * @param n
	 */
	private void setDrawDirection(int level, int n){
		if (level == 1 && n > 1) {
			xPosFactor = -1; // draw on left side
			drawDirection = "left";
		}
	}
	
	/**
	 * Sets the horizontal position of the node depending on the previous parent node.
	 * 
	 * 
	 * @param name
	 * @param xpos
	 * @param level
	 * @param n
	 * @param levelsize
	 * @return
	 */
	private int getXposition(String name, int xpos, int level, int n, int levelsize){
		int x = xpos + xPosFactor * (100 + name.length()*5);
		
		if (level == 0 && (n == 0 || n == 1)) x = 80 + name.length() * 6;
		if (level == 0 && (n == 2 || n == 3)) x = -80;
		
		//System.out.println("name:" + name + " length:" + name.length() * 5 + " xpos:");
		return x;
	}
	
	/**
	 * Sets the vertical position of the node depending on the previous parent node.
	 * First 4-8 except the root are placed hard-coded, all following nodes are placed automatically
	 * 
	 * @param name
	 * @param ypos
	 * @param level
	 * @param n
	 * @param levelsize
	 * @return
	 */
	private int getYposition(String name, int ypos, int level, int n, int levelsize){
		
		int y = ypos - (levelsize * 10) + (30 * n);
		//System.out.println("debug: levellen" + levelsize + " n:" + n + " level:" + level +" ypos:" + ypos + " name: " + name);
		
		if (level == 0 && (n == 0 || n == 2)) y = - 100;
		if (level == 0 && (n == 1 || n == 3)) y = 100 ;
		if (level == 0 && (n == 4 || n == 6)) y = 200 ;
		if (level == 0 && (n == 5 || n == 7)) y = -200;
		
		return y;
	}
	
	
	/**
	 * Recursive iterator which traverses the tree structure of the input data and generates a list of nodes with according positions.
	 * 
	 * @param jsonObj recursively the next node with its dependencies
	 * @param level	for internal use, the level of hierarchy. Root is 0, its children are 1, etc.
	 * @param parent	references the id of the parent node. Not available for the root element.
	 * @param nodeid	unique node id, generated automatically. 
	 * @param xpos		calculated horizontal position, depending on the parent's node position
	 * @param ypos		calculated vertical  position, depending on the parent's node position
	 * @param arraylength	number of children of a node
	 * @param nth		for internal use, the number of the node within the group
	 */
	private void printJsonObject(JSONObject jsonObj, int level, int parent, int nodeid, int xpos, int ypos, int arraylength, int nth) {

		String name = (String) jsonObj.get("name");
		JSONArray children = (JSONArray) jsonObj.get("children");
		int l = level;
		int id = nodeid;

		int levellen = 0;
		
		setDrawDirection(level, nth);
		
		if (children != null) levellen = children.size();
		
		
		//System.out.println("id:" + id + " Name: " + name +  " level: " + l + " parent:" + parent + " xpos:" + xpos + " ypos:" + ypos + " has children: " + levellen + "direction" + drawDirection);
		String pos = xpos +  " " + ypos;
		addNode(id, parent, name, pos);
		
		
		if (children != null) {
			l++; //next level of hierarchy
			for (int n = 0; n < levellen; n++) {
				int newXpos = getXposition(name, xpos, level, n, levellen);
				int newYpos = getYposition(name, ypos, level, n, levellen);
				//System.out.println("xpos: " + xpos + " ypos:" + ypos + " NewX:" + newXpos + " newY:" + newYpos);
				int newid = id * (int)Math.pow(10, l-1) + n + 1; // create unique id for node
				JSONObject object = (JSONObject) children.get(n);
				printJsonObject(object, l, nodeid, newid, newXpos, newYpos, levellen, n);
			}
		}
	}
	
	/**
	 * Initiation procedure for the transformation algorithm.
	 * Resets containers and calls the recursive iterator. 
	 * 
	 * @param jsonObj input data which contains the tree structure of the graph
	 * @return	Returns graph nodes with positioning data and connectors between nodes.
	 */
	public String formatGoJs(JSONObject jsonObj) {
		drawDirection = "right";
		goJSConnects = new JSONArray(); //reset arrays
		goJSNodes = new JSONArray();
		xPosFactor = 1;
		printJsonObject(jsonObj, 0,0,0,0,0,0,0);
		String result = goJSNodes.toString() + ", " + goJSConnects.toString();
		return result;
	}
	
	/**
	 * Converts a text representation of a json object to a JSONObject.
	 * The text must contain the graph nodes in a specific format.
	 * @param json
	 * @return
	 */
	public String formatGoJsString(String json) {
		
		JSONParser parser=new JSONParser();
		JSONObject array = new JSONObject();
		
		
		try{
			Object obj=parser.parse(json);
			array=(JSONObject)obj;
		  }
		  catch(ParseException pe){
		    System.out.println("position: " + pe.getPosition());
		    System.out.println(pe);
		  }
		
		String result = formatGoJs(array);
		
		return result;
	}
	
	
	public String getGoJsString(){
		return resultJSON;
	}
	
	
	public GoJsFormatter(){
		
	}
	
	
	public GoJsFormatter(WebDecision decision){
			String decisionAsJsonString = "[";
			decisionAsJsonString += "{\"key\": 0, \"text\": \"Mind Map\", \"loc\": \"0 0\"}";
			
			decisionAsJsonString += "]";
			resultJSON = decisionAsJsonString;
	}		
}
