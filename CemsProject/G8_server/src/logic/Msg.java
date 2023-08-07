package logic;

import java.io.Serializable;

@SuppressWarnings("serial")
/**
 * Used to pass messages between the server and the client
 * msg contains the wanted operation/request/notification name 
 * msgData contains the object needed to pass the information back and forth 
 */
public class Msg implements Serializable{

	private String msg;
	private MsgType msgType=null;
	private Object data = null; 
	

	public Msg(String msg, MsgType msgType, Object data) {
		super();
		this.msg = msg;
		this.msgType = msgType;
		this.data = data;
	}
	public Msg(String msg, Object data) {
		super();
		this.msg = msg;
		this.data = data;
	}
	public MsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
