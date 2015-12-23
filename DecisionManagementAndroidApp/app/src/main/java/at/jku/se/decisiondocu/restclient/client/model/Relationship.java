package at.jku.se.decisiondocu.restclient.client.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Relationship")
public class Relationship implements RelationshipInterface {
	
	private long id;
	private NodeInterface relatedNode;
	private boolean direction; //true = from Supernode to relatedNode; false = from relatedNode to Supernode

	public Relationship(NodeInterface relatedNode, boolean direction) {
		super();
		this.relatedNode=relatedNode;
		this.direction = direction;
	}
	
	public Relationship(long id, NodeInterface relatedNode, boolean direction) {
		super();
		this.id = id;
		this.relatedNode=relatedNode;
		this.direction = direction;
	}
	
	public Relationship(){
		
	};
	
	public boolean isDirection() {
		return direction;
	}
	public void setDirection(boolean direction) {
		this.direction = direction;
	}
	public NodeInterface getRelatedNode() {
		return relatedNode;
	}
	public void setRelatedNode(NodeInterface relatedNode) {
		this.relatedNode = relatedNode;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}
	
	

}
