package at.jku.se.decisiondocu.restclient.client.model;

public class MsgWrapper {

	private CustomDate mTimestamp;
	private String mCreator;
	private String mCreatorEmail;
	private String mMessage;
	private NodeInterface mNode;
	
	public MsgWrapper() {}

	public CustomDate getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(CustomDate mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	public String getCreator() {
		return mCreator;
	}

	public void setCreator(String mCreator) {
		this.mCreator = mCreator;
	}

	public String getCreatorEmail() {
		return mCreatorEmail;
	}

	public void setCreatorEmail(String mCreatorEmail) {
		this.mCreatorEmail = mCreatorEmail;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public NodeInterface getNode() {
		return mNode;
	}

	public void setNode(NodeInterface mNode) {
		this.mNode = mNode;
	}
}
