package rokkamkarthi.ImageProcessor.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Orientation {
	HORIZONTAL, VERTICAL, LEFT, RIGHT, UNKNOWN;
	
	@JsonCreator // This is the factory method and must be static
    public static Orientation fromString(String orientation) {
		return Orientation.valueOf(orientation.toUpperCase());
    }
}
