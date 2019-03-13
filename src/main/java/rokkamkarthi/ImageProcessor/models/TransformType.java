package rokkamkarthi.ImageProcessor.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransformType {

	GRAYSCALE, FLIP, ROTATE, RESIZE, THUMBNAIL, UNKNOWN;
	
	@JsonCreator // This is the factory method and must be static
    public static TransformType fromString(String transformType) {
		return TransformType.valueOf(transformType.toUpperCase());
    }

}
