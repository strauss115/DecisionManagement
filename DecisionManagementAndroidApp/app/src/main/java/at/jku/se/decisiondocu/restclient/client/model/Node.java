package at.jku.se.decisiondocu.restclient.client.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public abstract class Node implements NodeInterface {
	
	private long id;
	private String name;
	private Map<String,List<RelationshipInterface>> relationships;
	//private List<RelationshipInterface> relationships;
	private CustomDate creationDate;
	private Map<String, String> directProperties; //Properties directly stored in the Node (email, pw...)
	
	
	public Node(String name) {
		super();
		this.name = name;
	}
	
	public Node(String name, Map<String,List<RelationshipInterface>> relations) {
		super();
		this.name = name;
		this.relationships=relations;
	}
	//Necessary for Object mapping
	public Node(){
		super();
	}

	@Override
	public void addRelation(String type, RelationshipInterface relation){
		if (relationships == null){
			relationships = new HashMap<String,List<RelationshipInterface>>();
		}
		if(relationships.containsKey(type)){
			relationships.get(type).add(relation);
		}else{
			ArrayList<RelationshipInterface> rels = new ArrayList<RelationshipInterface>();
			rels.add(relation);
			relationships.put(type, rels);
		}
	}
	
	@Override
	public void addRelation(String type, NodeInterface relatedNode, boolean direction){
		RelationshipInterface rel = new Relationship(relatedNode, direction);
		if (relationships == null){
			relationships = new HashMap<String,List<RelationshipInterface>>();
		}
		if(relationships.containsKey(type)){
			relationships.get(type).add(rel);
		}else{
			ArrayList<RelationshipInterface> rels = new ArrayList<RelationshipInterface>();
			rels.add(rel);
			relationships.put(type, rels);
		}
	}
	
	

	public Map<String, List<RelationshipInterface>> getRelationships() {
		return relationships;
	}

	public void setRelationships(Map<String, List<RelationshipInterface>> relationships) {
		this.relationships = relationships;
	}

	@Override
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public CustomDate getCreationDate() {
		return creationDate;
	}
	@Override
	public void setCreationDate(CustomDate creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getDirectProperties() {
		return directProperties;
	}
	
	public Map<String, String> getAllDirectProperties() {
		return directProperties;
	}

	public void setDirectProperties(Map<String, String> directProperties) {
		this.directProperties = directProperties;
	}
	
	public void addDirectProperty(String key, String value){
		if (directProperties == null){
			directProperties = new HashMap<String, String>();
		}
		directProperties.put(key, value);
	}

	@Override
	public boolean updateNodeInDatabase(){
		//TODO
		return true;
	}
	
	public NodeInterface createNodeInDatabase(){
		//TODO
		return null;
	}
	
	@Override
	public String getNodeType(){
		return this.getClass().getSimpleName();
	}
	

	@Override
	public String toString() {
		/*StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()+" { ");
		builder.append("\t"+"id=" + id);
		builder.append("\t"+"name=" + name);
		builder.append("\t"+"creationdate=" + creationDate);*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception e) {
			return "Node [id=" + id + ", name=" + name + ", relationships=" + relationships + ", creationDate="
			+ creationDate + ", directProperties=" + directProperties + "]";
		}
	}
	
	

}
