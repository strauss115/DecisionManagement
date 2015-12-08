package GoJsConverter;

public class launcher_how_to_use {

	public static void main(String[] args) {
		
		String jsonstring = "{\"name\":\"decision1 very very long name\", \"children\": ["+ 
				"{\"name\":\"factor 1\", \"children\":[{\"name\":\"f1 child 1\"},{\"name\":\"f1 child 2\"},{\"name\":\"f1 child 3\"},{\"name\":\"f1 child 4\"} ]},"+
				"{\"name\":\"factor 2 longname\", \"children\":[{\"name\":\"f2 child 1 very long name\"},{\"name\":\"f2 child 2\"},{\"name\":\"f2 child 3\"} ]},"+
				"{\"name\":\"factor 3\", \"children\":[{\"name\":\"f3 child 1\"},{\"name\":\"f3 child 2\"},{\"name\":\"f3 child 3\"} ]},"+
				"{\"name\":\"factor 4 very long name loooong\", \"children\":[{\"name\":\"shorty\"},{\"name\":\"f4 child 2\"},{\"name\":\"f4 child 3\","+ 
				"	\"children\":[{\"name\":\"f4 child 3 level3 1\"}, {\"name\":\"f4 child 3 level3 2\"}, {\"name\":\"f4 child 3 level3 3\"}]} ]}"+
				"]}";
		
		GoJsFormatter f = new GoJsFormatter();
		String result = f.formatGoJsString(jsonstring);
		
		System.out.println(result);
		
    }
}
