package rokkamkarthi.ImageProcessor.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rokkamkarthi
 *
 */
public class ResponseTemplate {

	private String version = "1.0";
	private int status;
	private ResponseData data;
	private List<Action> actions;

	public ResponseTemplate() {
		actions = new ArrayList<>();
		data = null;
		status = 0;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public void addAction(Action action) {
		this.actions.add(action);
	}

	public void addActions(List<Action> actions) {
		this.actions.addAll(actions);
	}

}
