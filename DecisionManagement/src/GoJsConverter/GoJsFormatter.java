package GoJsConverter;

import org.json.simple.*;
import org.json.simple.parser.*;




public class GoJsFormatter {
	
	//private String[] colors ={"green", "orange", "black", "red"}; //used later for color rotation
	private static JSONArray goJSConnects = new JSONArray();
	private static JSONArray goJSNodes = new JSONArray();
	private static String drawDirection = "";
	private static int xPosFactor = 1;
	
	
	
	@SuppressWarnings("unchecked")
	private static void addConnection(int from, int to){
		JSONObject obj = new JSONObject();
		  obj.put("from", from);
		  obj.put("to", to);
		  goJSConnects.add(obj);
		  //System.out.println(obj);
	}
		
	@SuppressWarnings("unchecked")
	private static void addNode(int id, int parent, String name, String pos) {

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
	
	private static JSONObject getSampleData(){
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
	
	
	public static void setDrawDirection(int level, int n){
		if (level == 1 && n > 1) {
			xPosFactor = -1; // draw on left side
			drawDirection = "left";
		
		}
	}
	
	public static int getXposition(String name, int xpos, int level, int n, int levelsize){
		int x = xpos + xPosFactor * (50 + name.length() * 5);
		
		if (level == 0 && (n == 0 || n == 1)) x = 50 + name.length() * 5;
		if (level == 0 && (n == 2 || n == 3)) x = -50;
		
		//System.out.println("name:" + name + " length:" + name.length() * 5 + " xpos:");
		return x;
	}
	
	public static int getYposition(String name, int ypos, int level, int n, int levelsize){
		
		int y = ypos - (levelsize * 10) + (30 * n);
		//System.out.println("debug: levellen" + levelsize + " n:" + n + " level:" + level +" ypos:" + ypos + " name: " + name);
		
		if (level == 0 && (n == 0 || n == 2)) y = - 100;
		if (level == 0 && (n == 1 || n == 3)) y = 100 ;
		if (level == 0 && (n == 4 || n == 6)) y = 200 ;
		if (level == 0 && (n == 5 || n == 7)) y = -200;
		
		return y;
	}
	
	
	public static void printJsonObject(JSONObject jsonObj, int level, int parent, int nodeid, int xpos, int ypos, int arraylength, int nth) {

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
	
	public static String formatGoJs(JSONObject jsonObj) {
		drawDirection = "right";
		goJSConnects = new JSONArray(); //reset arrays
		goJSNodes = new JSONArray();
		xPosFactor = 1;
		printJsonObject(jsonObj, 0,0,0,0,0,0,0);
		String result = goJSNodes.toString() + ", " + goJSConnects.toString();
		return result;
	}
	
	public static JSONObject getGoJsObject(String json) {
		
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
		return (JSONObject) array;
	}
	
	
	public static void main(String[] args) {
		
		
		//usage example 1 with jsonObj
		JSONObject jsonObj = getSampleData();
		String json = formatGoJs(jsonObj);
		System.out.println(json);
		
		
		//usage example 2 with json string as input
		String jsonstring = "{\"name\":\"decision1 very very long name\", \"children\": ["+ 
				"{\"name\":\"factor 1\", \"children\":[{\"name\":\"f1 child 1\"},{\"name\":\"f1 child 2\"},{\"name\":\"f1 child 3\"},{\"name\":\"f1 child 4\"} ]},"+
				"{\"name\":\"factor 2 longname\", \"children\":[{\"name\":\"f2 child 1 very long name\"},{\"name\":\"f2 child 2\"},{\"name\":\"f2 child 3\"} ]},"+
				"{\"name\":\"factor 3\", \"children\":[{\"name\":\"f3 child 1\"},{\"name\":\"f3 child 2\"},{\"name\":\"f3 child 3\"} ]},"+
				"{\"name\":\"factor 4 very long name loooong\", \"children\":[{\"name\":\"shorty\"},{\"name\":\"f4 child 2\"},{\"name\":\"f4 child 3\","+ 
				"	\"children\":[{\"name\":\"f4 child 3 level3 1\"}, {\"name\":\"f4 child 3 level3 2\"}, {\"name\":\"f4 child 3 level3 3\"}]} ]}"+
				"]}";
		JSONObject jsonObj2 = getGoJsObject(jsonstring);
		json = formatGoJs(jsonObj2);
		System.out.println(json);
		
    }
	
}
