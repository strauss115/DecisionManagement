package at.jku.se.decisiondocu.restclient.client.model;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonSubTypes({ @Type(value = Decision.class, name = "Decision"), @Type(value = Property.class, name = "property")
		, @Type(value = Project.class, name = "project"), @Type(value = User.class, name = "user"), @Type(value = Comment.class, name = "comment")
		, @Type(value = Alternative.class, name = "alternative"), @Type(value = DecisionGroup.class, name = "decisiongroup")
		, @Type(value = InfluenceFactor.class, name = "influencefactor"), @Type(value = QualityAttribute.class, name = "qualityattribute")})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@javaref")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public interface NodeInterface {

	public long getId();
	public void setId(long id);
	public String getName();
	public void setName(String name);

	public CustomDate getCreationDate();
	public void setCreationDate(CustomDate creationDate);

	public void addRelation(String type, RelationshipInterface relation);
	public void addRelation(String type, NodeInterface relatedNode, boolean direction);
	public Map<String, List<RelationshipInterface>> getRelationships();
	public void setRelationships(Map<String, List<RelationshipInterface>> relationships);

	public boolean updateNodeInDatabase();
	public NodeInterface createNodeInDatabase();

	@JsonIgnore
	public String getNodeType();
	public Map<String, String> getDirectProperties();
	public void setDirectProperties(Map<String, String> directProperties);
	public void addDirectProperty(String key, String value);

}
