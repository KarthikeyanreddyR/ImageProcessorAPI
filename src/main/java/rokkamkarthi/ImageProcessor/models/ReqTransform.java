package rokkamkarthi.ImageProcessor.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqTransform {

	@JsonProperty(required = true, value = Utils.Req_Image)
	private String imageAsBase64String;
	@JsonProperty(required = true, value = Utils.Req_Transformations)
	private List<Transformation> transformations;

	public String getImageAsBase64String() {
		return imageAsBase64String;
	}

	public void setImageAsBase64String(String imageAsBase64String) {
		this.imageAsBase64String = imageAsBase64String;
	}

	public List<Transformation> getTransformations() {
		return transformations;
	}

	public void setTransformations(List<Transformation> transformations) {
		this.transformations = transformations;
	}

}
