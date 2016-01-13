package at.jku.se.model;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Interface for the node class
 * @author August
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonSubTypes({ @Type(value = Decision.class, name = "decision"), @Type(value = Property.class, name = "property") 
, @Type(value = Project.class, name = "project"), @Type(value = User.class, name = "user"), @Type(value = Message.class, name = "message")
, @Type(value = Alternative.class, name = "alternative"), @Type(value = DecisionGroup.class, name = "decisiongroup")
, @Type(value = InfluenceFactor.class, name = "influencefactor"), @Type(value = QualityAttribute.class, name = "qualityattribute")
, @Type(value = Consequence.class, name = "consequence"), @Type(value = Document.class, name = "document"), @Type(value = Rationale.class, name = "rationale")})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@javaref")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public interface NodeInterface {
	
	/**
	 * Returns the Id of the node
	 * @return The Id of the node
	 */
	public long getId();
	/**
	 * Sets the Id of the node
	 * @param id
	 */
	public void setId(long id);
	/**
	 * Returns the name of the node
	 * @return The name of the node as string
	 */
	public String getName();
	/**
	 * Sets the name of the node
	 * @param name
	 */
	public void setName(String name);
	/**
	 * Returns the customDate of the node
	 * @return The custom date of the node
	 */
	public CustomDate getCreationDate();
	/**
	 * Sets the createion date of the node
	 * @param creationDate
	 */
	public void setCreationDate(CustomDate creationDate);
	/**
	 * Adds a relation to the node
	 * @param type
	 * @param relation
	 */
	public void addRelation(String type, RelationshipInterface relation);
	/**
	 * Adds a relation to the node
	 * @param type
	 * @param relatedNode
	 * @param direction
	 */
	public void addRelation(String type, NodeInterface relatedNode, boolean direction);
	/**
	 * Returns all relationships of the node
	 */
	public Map<String, List<RelationshipInterface>> getRelationships();
	/**
	 * Sets the relationships of the node
	 * @param relationships
	 */
	public void setRelationships(Map<String, List<RelationshipInterface>> relationships);
	
	/**
	 * Returns the node type as string
	 * @return Returns the node type as string
	 */
	@JsonIgnore
	public String getNodeType();
	/**
	 * Returns the direct properties of the node
	 * @return Returns the direct proberties of the node as map object
	 */
	public Map<String, String> getDirectProperties();
	/**
	 * Returns all direct properties
	 * @return All direct properties of the node as map object
	 */
	@JsonIgnore
	public Map<String, String> getAllDirectProperties();
	/**
	 * Sets the direct properties
	 * @param directProperties
	 */
	public void setDirectProperties(Map<String, String> directProperties);
	/**
	 * Adss a direct property to the node 
	 * @param key
	 * @param value
	 */
	public void addDirectProperty(String key, String value);

}
