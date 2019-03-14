package rokkamkarthi.ImageProcessor.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author rokkamkarthi
 *
 */
public class Utils {

	// boolean res = ImageIO.write(img, _fileType, new
	// File("C:\\Users\\rkkr9\\grayscale." + _fileType));

	/*
	 * ImageIO.write(rotated, ImageProcessorDataStore.getUploadedImageType(), new
	 * File("C:\\Users\\rkkr9\\Desktop\\test\\rotate_" +
	 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss")
	 * ) + "." + ImageProcessorDataStore.getUploadedImageType()));
	 */

	public static final int thumbnailWidth = 100;
	public static final int thumbnailHeight = 100;

	// Top req obj
	public static final String Req_Image = "image_string";
	public static final String Req_Transformations = "transformations";

	// transformation obj
	public static final String Transform_Type = "transform_type";
	public static final String Req_Parameters = "options";

	// params obj
	public static final String Params_Width = "width";
	public static final String Params_Height = "height";
	public static final String Params_Degree = "degrees";
	public static final String Params_Ori = "orientation";

	/**
	 * Creates error {@link ResponseTemplate} for given error, message.
	 * 
	 * @param error   - unique error code
	 * @param message - error message
	 * @param status  - optional parameter. default is 500
	 * @return {@link ResponseTemplate}
	 */
	public static ResponseTemplate getErrorResponse(String error, String message) {
		ResponseTemplate responseTemplate = new ResponseTemplate();
		ResponseData responseData = new ResponseData(null, new ErrorResponse(error, message));
		responseTemplate.setData(responseData);
		responseTemplate.setStatus(500);
		responseTemplate.addActions(getActions());
		responseTemplate.setModel(getReqObj());
		return responseTemplate;
	}

	/**
	 * Returns {@link ResponseTemplate} for landing page.
	 * 
	 * @return {@link ResponseTemplate}
	 */
	public static ResponseTemplate landingPageResponse() {
		ResponseTemplate responseTemplate = new ResponseTemplate();
		ResponseData responseData = new ResponseData("Success", null);
		responseTemplate.setData(responseData);
		responseTemplate.setStatus(200);
		responseTemplate.addActions(getActions());
		responseTemplate.setModel(getReqObj());
		return responseTemplate;
	}

	/**
	 * Create {@link ResponseTemplate} object for given byte array. Byte array data
	 * is encoded to string.
	 * 
	 * @param successData - byte array
	 * @return valid {@link ResponseTemplate} with status set to 200
	 */
	public static ResponseTemplate transformationSuccessResponse(byte[] successData) {
		ResponseTemplate responseTemplate = new ResponseTemplate();
		ResponseData responseData = new ResponseData(convertByteArrayToString(successData), null);
		responseTemplate.setData(responseData);
		responseTemplate.setStatus(200);
		responseTemplate.addActions(getActions());
		responseTemplate.setModel(getReqObj());
		return responseTemplate;
	}

	/**
	 * Convert byte array data to Base64 encoded string
	 * 
	 * @param data - byte array
	 * @return encoded string
	 */
	public static String convertByteArrayToString(byte[] data) {
		byte[] encodedBytes = Base64.getEncoder().withoutPadding().encode(data);
		return new String(encodedBytes);
	}
	
	/**
	 * Return List of {@link Action} based on Image processor state
	 * {@link ImageStates}
	 * 
	 * @return ArrayList of actions
	 */
	public static List<Action> getActions() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(new Action("/transform", "POST",
				"Apply image transformations. check model for sample request objects."));
		return actions;
	}

	public static boolean validateRequestData(ReqTransform inputData) {
		if (inputData == null)
			return false;
		if (inputData.getImageAsBase64String().isEmpty())
			return false;
		if (inputData.getImageAsBase64String().trim().isEmpty())
			return false;
		if (inputData.getTransformations() == null)
			return false;
		if (inputData.getTransformations().isEmpty())
			return false;

		return true;
	}

	public static List<Orientation> getOrientationEnumValues() {
		return new ArrayList<Orientation>(Arrays.asList(Orientation.values()));
	}

	public static List<TransformType> getTransformTypeEnumValues() {
		return new ArrayList<TransformType>(Arrays.asList(TransformType.values()));
	}

	public static ObjectNode getReqObj() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		json.put(Req_Image, "image as base64 encoded string");
		ArrayNode arr = mapper.createArrayNode();
		for (TransformType type : getTransformTypeEnumValues()) {
			switch (type) {
			case GRAYSCALE:
				arr.add(grayscale(mapper));
				break;
			case FLIP:
				arr.add(flip(mapper, Orientation.HORIZONTAL));
				arr.add(flip(mapper, Orientation.VERTICAL));
				break;
			case ROTATE:
				arr.add(rotate(mapper));
				arr.add(rotate(mapper, Orientation.LEFT));
				arr.add(rotate(mapper, Orientation.RIGHT));
				break;
			case THUMBNAIL:
				arr.add(thumbnail(mapper));
				break;
			case RESIZE:
				arr.add(resize(mapper));
				break;
			default:
				break;
			}
		}
		json.putPOJO(Req_Transformations, arr);
		return json;
	}

	public static ObjectNode grayscale(ObjectMapper mapper) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.GRAYSCALE.name());
		return json;
	}

	public static ObjectNode thumbnail(ObjectMapper mapper) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.THUMBNAIL.name());
		return json;
	}

	public static ObjectNode resize(ObjectMapper mapper) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.RESIZE.name());
		ObjectNode json1 = mapper.createObjectNode();
		json1.put(Params_Width, 0);
		json1.put(Params_Height, 0);
		json.putPOJO(Req_Parameters, json1);
		return json;
	}

	public static ObjectNode rotate(ObjectMapper mapper) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.ROTATE.name());
		ObjectNode json1 = mapper.createObjectNode();
		json1.put(Params_Degree, 0);
		json.putPOJO(Req_Parameters, json1);
		return json;
	}

	public static ObjectNode rotate(ObjectMapper mapper, Orientation orientation) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.ROTATE.name());
		ObjectNode json1 = mapper.createObjectNode();
		json1.put(Params_Ori, orientation.name());
		json.putPOJO(Req_Parameters, json1);
		return json;
	}

	public static ObjectNode flip(ObjectMapper mapper, Orientation orientation) {
		ObjectNode json = mapper.createObjectNode();
		json.put(Transform_Type, TransformType.FLIP.name());
		ObjectNode json1 = mapper.createObjectNode();
		json1.put(Params_Ori, orientation.name());
		json.putPOJO(Req_Parameters, json1);
		return json;
	}
}
