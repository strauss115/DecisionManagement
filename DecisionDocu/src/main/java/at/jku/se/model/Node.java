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

/**
 * Abstract class
 * @author August
 *
 */
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

	/**
	 * Constructor
	 * @param name Name of the node
	 */
	public Node(String name) {
		super();
		this.name = name;
	}

	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * @param name Name of the node
	 * @param relations Relations of the node
	 */
	public Node(String name, Map<String, List<RelationshipInterface>> relations) {
		super();
		this.name = name;
		this.relationships = relations;
	}

	/** 
	 * Default constructor of node 
	 * Necessary for Object mapping
	 */
	public Node() {
		super();
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Returns the hashcode of the node
	 * @return The hashcode of the node
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((directProperties == null) ? 0 : directProperties.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((relationships == null) ? 0 : relationships.hashCode());
		return result;
	}

	/**
	 * @param obj the node obejct to compare with the node
	 * @return true if the given node is equal to the node, false if not equal to the given node
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (directProperties == null) {
			if (other.directProperties != null)
				return false;
		} else if (!directProperties.equals(other.directProperties))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (relationships == null) {
			if (other.relationships != null)
				return false;
		} else if (!relationships.equals(other.relationships))
			return false;
		return true;
	}

	/**
	 * Adds a relationship to the node
	 * @param type
	 * @param relation
	 */
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

	/**
	 * Adds a relationship to the node
	 * @param type
	 * @param relatedNode
	 * @param direction
	 */
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

	/**
	 * Returns all relationships of the node
	 * @return All relationships of the node as Map object
	 */
	public Map<String, List<RelationshipInterface>> getRelationships() {
		if (relationships == null)
			relationships = new HashMap<String, List<RelationshipInterface>>();

		return relationships;
	}
	
	/**
	 * Sets the relationships of the node
	 * @param relationships
	 */
	public void setRelationships(Map<String, List<RelationshipInterface>> relationships) {
		this.relationships = relationships;
	}

	/**
	 * Returns the Id of the node object
	 * @return The Id of the node object
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * Sets the Id of the node object
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the creation date of the node object
	 * @return The creation date of the node object
	 */
	@Override
	public CustomDate getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date of the node object
	 * @param creationDate
	 */
	@Override
	public void setCreationDate(CustomDate creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the name of the node object
	 * @return The name of the node object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the node object
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the direct properties of the node object
	 * @return The direct properties of the node object as Map<String, String> object
	 */
	public Map<String, String> getDirectProperties() {
		return directProperties;
	}

	/**
	 * Returns all direct properties of the node object
	 * @return A Map<String, String> of all direct properties of the node object
	 */
	public Map<String, String> getAllDirectProperties() {
		return directProperties;
	}

	/**
	 * Sets direct properties
	 * @param directProperties
	 */
	public void setDirectProperties(Map<String, String> directProperties) {
		this.directProperties = directProperties;
	}

	/**
	 * Adds a direct property
	 * @param key
	 * @param value
	 */
	public void addDirectProperty(String key, String value) {
		if (directProperties == null) {
			directProperties = new HashMap<String, String>();
		}
		directProperties.put(key, value);
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
					NodeInterface n = rel.getRelatedNode();
					
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

	/**
	 * Checks if this node has a direct relationship (1 level) to the other
	 * given node id
	 * 
	 * @param otherNodeId
	 *            Other node to check relationship
	 * @return True if node has a relationship to other node
	 */
	public boolean isRelatedToNode(long otherNodeId) {
		Map<String, List<RelationshipInterface>> allRelations = getRelationships();

		for (List<RelationshipInterface> relations : allRelations.values()) {
			for (RelationshipInterface relation : relations) {
				if (relation.getRelatedNode().getId() == otherNodeId) {
					return true;
				}
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------

	/**
	 * Helper method to get a list of Ids for a list of nodes
	 * 
	 * @param nodes
	 *            Nodes to get it
	 * @return List of Ids as string
	 */
	public static <T extends Node> List<String> getListOfIds(List<T> nodes) {
		List<String> result = new LinkedList<String>();
		for (T node : nodes) {
			result.add(String.valueOf(node.getId()));
		}
		return result;
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Returns the type of the node
	 * @return Returns the type of the node as String
	 */
	@Override
	public String getNodeType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Returns a String represantation of the node object
	 * @return A String represantation of the node object
	 */
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
