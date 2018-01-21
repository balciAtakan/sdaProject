package sda.web.exception;

public class SDAException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	
	public SDAException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
