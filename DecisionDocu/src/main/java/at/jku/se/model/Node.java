package at.jku.se.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.database.DBService;

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
		if (relationships == null)
			relationships = new HashMap<String, List<RelationshipInterface>>();

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
			if (relations != null) {
				for (RelationshipInterface rel : relations) {
					result.add(type.cast(rel.getRelatedNode()));
				}
			}
		} catch (Exception e) {
			log.error("Unable to get relationship nodes '" + relationship + "' for node '" + getId() + "': " + e);
		}
		return result;
	}

	/**
	 * Returns a single related node with a given relationship
	 * 
	 * @param relationship
	 *            Name of the relationship as string
	 * @param type
	 *            Type of expected related node
	 * @return Related node of given type or null if there is no relationship
	 */
	protected <T extends Node> T getSingleNodeByRelationship(String relationship, Class<T> type) {
		List<RelationshipInterface> relations = this.getRelationships().get(relationship);
		if (relations == null || relations.size() == 0) {
			log.info("Node '" + getId() + "' has no '" + relationship + "'");
			return null;
		}
		if (relations.size() > 1) {
			log.warn("Node '" + getId() + "' has multiple '" + relationship
					+ "' relationships although there should be only one");
		}
		return type.cast(relations.get(0).getRelatedNode());
	}

	/**
	 * Adds a single node relationship, all other (old) relationships will be
	 * deleted (without deleting the old node)
	 * 
	 * @param relationship
	 *            Name of the relationship as string
	 * @param relatedNode
	 *            Related node to create relationship
	 */
	protected <T extends Node> void setSingleNodeRelationship(String relationship, T relatedNode) {
		List<RelationshipInterface> relations = getRelationships().get(relationship);
		if (relations.isEmpty()) {
			log.info("Updating relationship '" + relationship + "' for node '" + getName() + "'");
			for (RelationshipInterface rel : relations) {
				DBService.deleteRelationship(rel.getId());
			}
		}
		this.addRelation(relationship, relatedNode, true);
	}

	/**
	 * Gets a direct property as string
	 * 
	 * @param propertyName
	 *            Name of the property to get
	 * @return Property as string or an empty string if direct property was not
	 *         found
	 */
	protected String getDirectProperty(String propertyName) {
		try {
			String value = getDirectProperties().get(propertyName);
			if (value != null)
				return value;
		} catch (Exception e) {
			log.info("Unable to get direct property '" + propertyName + "' for node '" + getName() + "':" + e);
		}
		return "";
	}

	/**
	 * Deletes a relation to another node (other node still remains in database)
	 * 
	 * @param relationship
	 *            Name of the relationship as string
	 * @param relatedNode
	 *            Related node to delete relationship
	 * @return True if relationship was successfully deleted
	 */
	protected <T extends Node> boolean deleteRelationByRelatedNode(String relationship, T relatedNode) {
		List<RelationshipInterface> relations = getRelationships().get(relationship);
		for (RelationshipInterface relation : relations) {
			if (relation.getRelatedNode().getId() == relatedNode.getId()) {
				return DBService.deleteRelationship(relation.getId());
			}
		}
		return false;
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
