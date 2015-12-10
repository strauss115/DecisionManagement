package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonSubTypes({ @Type(value = Relationship.class, name = "relationship")})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public interface RelationshipInterface {
	
	public NodeInterface getRelatedNode();
	public void setRelatedNode(NodeInterface relatedNode);
	
	public long getId();
	public void setId(long id);
	
	

}
