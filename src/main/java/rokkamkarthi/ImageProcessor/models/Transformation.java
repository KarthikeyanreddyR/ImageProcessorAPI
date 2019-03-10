package rokkamkarthi.ImageProcessor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transformation {

	@JsonProperty(required = true, value = Utils.Transform_Type)
	private TransformType transformType = TransformType.UNKNOWN;
	@JsonProperty(required = false, value = Utils.Req_Parameters)
	private Params parameters = new Params();

	public Params getParameters() {
		return parameters;
	}

	public void setParameters(Params parameters) {
		this.parameters = parameters;
	}

	public TransformType getTransformType() {
		return transformType;
	}

	public void setTransformType(TransformType transformType) {
		this.transformType = transformType;
	}

}
