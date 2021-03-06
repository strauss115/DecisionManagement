package at.jku.se.chatserver;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.jku.se.dm.shared.RelationString;
import at.jku.se.dm.shared.PropertyString;
import at.jku.se.model.CustomDate;
import at.jku.se.model.Message;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;

/**
 * Message object wrapper. Used for exchanging messages between server and client.
 * @author martin
 *
 */
public class MsgWrapper {
	
	private static final Logger log = LogManager.getLogger(MsgWrapper.class);
	
	private CustomDate mTimestamp;
	private String mCreator;
	private String mCreatorEmail;
	private String mMessage;
	private NodeInterface mNode;
	
	/**
	 * Creates a dummy MsgWrapper object
	 * @return
	 */
	public static MsgWrapper dummy() {
		MsgWrapper msg = new MsgWrapper();
		msg.setCreator("server");
		msg.setCreatorEmail(null);
		msg.setTimestamp(new CustomDate(System.currentTimeMillis()));
		return msg;
	}
	
	/**
	 * Default constructor
	 */
	public MsgWrapper() {}
	
	/**
	 * Constructor to read fields from Message object
	 * @param msg Message object
	 * @throws Exception 
	 */
	public MsgWrapper(Message msg) throws Exception {
		
		if (msg == null) {
			throw new Exception("Argument msg must not be null!");
		}
		
		mMessage = msg.getName();
		
		if (msg.getDirectProperties() == null) {
			mTimestamp = new CustomDate(System.currentTimeMillis());
		}else {
			String ts = msg.getDirectProperties().get(PropertyString.CREATIONDATE);
			if (ts == null) {
				ts = msg.getDirectProperties().get("creationdate");
			}
			mTimestamp = (ts == null ? new CustomDate(System.currentTimeMillis()) : new CustomDate(Long.parseLong(ts)));
		}
			
		List<RelationshipInterface> creators = msg.getRelationships().get(RelationString.HAS_CREATOR);
		if (creators != null && creators.size() > 0) {
			try {
                User u = (User)creators.get(0).getRelatedNode();
                mCreator = u.getName() + " " + u.getLastname();
                mCreatorEmail = u.getEmail();
            } catch (Exception e) {
            	log.error(e);
            }
		} else {
			mCreator = "server";
		}
	}
	
	/**
	 * Constructor that takes a NodeInterface
	 * @param msg Message object
	 * @param createdNode NodeInterface object that the message created
	 * @throws Exception
	 */
	public MsgWrapper(Message msg, NodeInterface createdNode) throws Exception{
		this(msg);
		mNode = createdNode;
	}
	
	/**
	 * Returns the timestamp of the message
	 * @return mTimestamp
	 */
	public CustomDate getTimestamp() {
		return mTimestamp;
	}

	/**
	 * Sets the timestamp of the message
	 * @param mTimestamp
	 */
	public void setTimestamp(CustomDate mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	/**
	 * Returns the creator of the message
	 * @return mCreator
	 */
	public String getCreator() {
		return mCreator;
	}

	/**
	 * Sets the creator of a message
	 * @param mCreator
	 */
	public void setCreator(String mCreator) {
		this.mCreator = mCreator;
	}
	
	/**
	 * Returns the email of the creator
	 * @return mCreatorEmail
	 */
	public String getCreatorEmail() {
		return mCreatorEmail;
	}

	/**
	 * Sets the email of the creator
	 * @param mCreatorEmail
	 */
	public void setCreatorEmail(String mCreatorEmail) {
		this.mCreatorEmail = mCreatorEmail;
	}

	/**
	 * Returns the message
	 * @return mMessage
	 */
	public String getMessage() {
		return mMessage;
	}

	/**
	 * Sets the message
	 * @param mMessage
	 */
	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	/**
	 * Returns the message node
	 * @return mNode
	 */
	public NodeInterface getNode() {
		return mNode;
	}

	/**
	 * Sets the message node
	 * @param mNode
	 */
	public void setNode(NodeInterface mNode) {
		this.mNode = mNode;
	}

	/**
	 * Returns the message object as a String
	 * @return String
	 */
	@Override
	public String toString() {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			log.error(e);
			return "MsgWrapper [mTimestamp=" + mTimestamp + ", mCreator=" + mCreator + ", mMessage=" + mMessage + "]";
		}
	}
}
