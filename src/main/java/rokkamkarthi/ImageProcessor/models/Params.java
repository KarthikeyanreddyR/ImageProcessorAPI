package rokkamkarthi.ImageProcessor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Params {

	@JsonProperty(required = false, value = Utils.Params_Width)
	private int width = -1;
	@JsonProperty(required = false, value = Utils.Params_Height)
	private int height = -1;
	@JsonProperty(required = false, value = Utils.Params_Degree)
	private int degrees = 1000;
	@JsonProperty(required = false, value = Utils.Params_Ori)
	private Orientation orientation = Orientation.UNKNOWN;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDegrees() {
		return degrees;
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

}
