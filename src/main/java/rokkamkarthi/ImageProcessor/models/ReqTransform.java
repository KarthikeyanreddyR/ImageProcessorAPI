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

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(imageAsBase64String.length()).append("\n")
		.append(transformations.size()).append("\n");
		for(Transformation t : transformations) {
			s.append(t.getTransformType()).append("\n");
			if(t.getParameters() != null) {
				s.append("Degree: ").append(t.getParameters().getDegrees()).append("\n");
				s.append("Width: ").append(t.getParameters().getWidth()).append("\n");
				s.append("Height: ").append(t.getParameters().getHeight()).append("\n");
				s.append("Orientation: ").append(t.getParameters().getOrientation()).append("\n");
			}
		}
		return s.toString();
	}
	
	

}
