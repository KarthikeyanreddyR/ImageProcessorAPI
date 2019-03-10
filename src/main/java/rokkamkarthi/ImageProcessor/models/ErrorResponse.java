package rokkamkarthi.ImageProcessor.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponse {
	private String error;
	private String message;
	private String timestamp;

	public ErrorResponse(String error, String message) {
		this.error = error;
		this.message = message;
		this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
