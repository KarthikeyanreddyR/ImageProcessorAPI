package rokkamkarthi.ImageProcessor.models;

/**
 * @author rokkamkarthi
 *
 */
public class Action {

	private String href;
	private String method;
	private String description;

	public Action(String href, String method, String description) {
		this.href = href;
		this.method = method;
		this.description = description;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
