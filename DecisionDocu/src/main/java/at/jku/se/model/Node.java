package at.jku.se.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Node implements NodeInterface {

	private static final Logger log = LogManager.getLogger(Node.class);

	// ------------------------------------------------------------------------

	private long id;
	private String name;
	private Map<String, List<RelationshipInterface>> relationships;
	// private List<RelationshipInterface> relationships;
	private CustomDate creationDate;
	// Properties directly stored in the Node (email, pw...)
	private Map<String, String> directProperties;

	// ------------------------------------------------------------------------

	public Node(String name) {
		super();
		this.name = name;
	}

	// ------------------------------------------------------------------------

	public Node(String name, Map<String, List<RelationshipInterface>> relations) {
		super();
		this.name = name;
		this.relationships = relations;
	}

	// Necessary for Object mapping
	public Node() {
		super();
	}

	// ------------------------------------------------------------------------

	@Override
	public void addRelation(String type, RelationshipInterface relation) {
		if (relationships == null) {
			relationships = new HashMap<String, List<RelationshipInterface>>();
		}
		if (relationships.containsKey(type)) {
			relationships.get(type).add(relation);
		} else {
			ArrayList<RelationshipInterface> rels = new ArrayList<RelationshipInterface>();
			rels.add(relation);
			relationships.put(type, rels);
		}
	}

	@Override
	public void addRelation(String type, NodeInterface relatedNode, boolean direction) {
		RelationshipInterface rel = new Relationship(relatedNode, direction);
		if (relationships == null) {
			relationships = new HashMap<String, List<RelationshipInterface>>();
		}
		if (relationships.containsKey(type)) {
			relationships.get(type).add(rel);
		} else {
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

	public void addDirectProperty(String key, String value) {
		if (directProperties == null) {
			directProperties = new HashMap<String, String>();
		}
		directProperties.put(key, value);
	}

	@Override
	public boolean updateNodeInDatabase() {
		// TODO
		return true;
	}

	public NodeInterface createNodeInDatabase() {
		// TODO
		return null;
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets all related nodes
	 * 
	 * @param relationship
	 *            Name of the relationship as string
	 * @param type
	 *            Type of expected related nodes
	 * @return List of related nodes
	 */
	protected <T extends Node> List<T> getNodesByRelationship(String relationship, Class<T> type) {
		List<T> result = new LinkedList<T>();
		try {
			List<RelationshipInterface> relations = this.getRelationships().get(relationship);
			for (RelationshipInterface rel : relations) {
				result.add(type.cast(rel.getRelatedNode()));
			}
		} catch (Exception e) {
			log.error("Unable to get relationship nodes '" + relationship + "' for node '" + getId() + "'");
		}
		return result;
	}

	// ------------------------------------------------------------------------

	@Override
	public String getNodeType() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String toString() {
		/*
		 * StringBuilder builder = new StringBuilder();
		 * builder.append(this.getClass().getSimpleName()+" { ");
		 * builder.append("\t"+"id=" + id); builder.append("\t"+"name=" + name);
		 * builder.append("\t"+"creationdate=" + creationDate);
		 */
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception e) {
			return "Node [id=" + id + ", name=" + name + ", relationships=" + relationships + ", creationDate="
					+ creationDate + ", directProperties=" + directProperties + "]";
		}
	}

}
