package at.jku.se.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class Relationship implements RelationshipInterface
 * @author August
 *
 */
@JsonTypeName("Relationship")
public class Relationship implements RelationshipInterface {
	
	private long id;
	private NodeInterface relatedNode;
	private boolean direction; //true = from Supernode to relatedNode; false = from relatedNode to Supernode

	/**
	 * Constructor
	 * @param relatedNode
	 * @param direction
	 */
	public Relationship(NodeInterface relatedNode, boolean direction) {
		super();
		this.relatedNode=relatedNode;
		this.direction = direction;
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param relatedNode
	 * @param direction
	 */
	public Relationship(long id, NodeInterface relatedNode, boolean direction) {
		super();
		this.id = id;
		this.relatedNode=relatedNode;
		this.direction = direction;
	}
	
	/**
	 * Default constructor
	 */
	public Relationship(){
		
	};
	
	/**
	 * True if relationship is a direction
	 * @return true if relationship is a direction, false if relationship is no direction
	 */
	public boolean isDirection() {
		return direction;
	}
	/**
	 * Sets true if is direction
	 * @param direction
	 */
	public void setDirection(boolean direction) {
		this.direction = direction;
	}
	/**
	 * {@inheritDoc}
	 */
	public NodeInterface getRelatedNode() {
		return relatedNode;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setRelatedNode(NodeInterface relatedNode) {
		this.relatedNode = relatedNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return id;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(long id) {
		this.id = id;
		
	}
	
	

}
