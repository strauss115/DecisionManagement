package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Interface for Relationship class
 * @author August
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonSubTypes({ @Type(value = Relationship.class, name = "relationship")})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public interface RelationshipInterface {
	
	/**
	 * Returns related node
	 * @return Related node object
	 */
	public NodeInterface getRelatedNode();
	/**
	 * Sets related node
	 * @param relatedNode
	 */
	public void setRelatedNode(NodeInterface relatedNode);
	
	/**
	 * Returns relationship node id
	 * @return Relationship node object id
	 */
	public long getId();
	/**
	 * Sets relationship node id
	 * @param id
	 */
	public void setId(long id);
	
	

}
