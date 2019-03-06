package rokkamkarthi.ImageProcessor.models;

public class ResponseData {

	private Object success;
	private ErrorResponse error;

	public ResponseData(Object success, ErrorResponse error) {
		this.success = success;
		this.error = error;
	}

	public Object getSuccess() {
		return success;
	}

	public void setSuccess(Object success) {
		this.success = success;
	}

	public ErrorResponse getError() {
		return error;
	}

	public void setError(ErrorResponse error) {
		this.error = error;
	}

}
