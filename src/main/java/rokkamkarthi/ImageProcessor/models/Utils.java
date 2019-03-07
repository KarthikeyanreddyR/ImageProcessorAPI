package rokkamkarthi.ImageProcessor.models;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import rokkamkarthi.ImageProcessor.store.ImageProcessorDataStore;

/**
 * @author rokkamkarthi
 *
 */
public class Utils {

	// boolean res = ImageIO.write(img, _fileType, new
	// File("C:\\Users\\rkkr9\\grayscale." + _fileType));

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
		return responseTemplate;
	}

	/**
	 * Returns {@link ResponseTemplate} for successful image upload.
	 * 
	 * @return {@link ResponseTemplate}
	 */
	public static ResponseTemplate uploadSuccessResponse() {
		ResponseTemplate responseTemplate = new ResponseTemplate();
		ResponseData responseData = new ResponseData("File uploaded successfully!!!.", null);
		responseTemplate.setData(responseData);
		responseTemplate.setStatus(200);
		responseTemplate.addActions(getActions());
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
	 * Return List of {@link Action} based on Image processor state {@link ImageStates}
	 * @return ArrayList of actions
	 */
	public static List<Action> getActions() {
		List<Action> actions = new ArrayList<Action>();
		
		switch (ImageProcessorDataStore.states) {
		case IMAGE_ABSENT:
			actions.add(new Action("/uploadImage", "POST", "Upload an Image."));
			break;
		case IMAGE_PRESENT:
			actions.add(new Action("/uploadImage", "POST", "Upload an Image."));
			actions.add(new Action("/transform/grayscale", "POST", "Grayscale image."));
			actions.add(
					new Action("/transform/resize", "POST", "Resize image according to provided width and height."));
			actions.add(new Action("/transform/thumbnail", "POST",
					"Generate thumbnail according to provided width and height."));
			actions.add(new Action("/transform/flip/horizontal", "POST", "Flip image horizontally along x-axis."));
			actions.add(new Action("/transform/flip/vertical", "POST", "Flip image vertically along y-axis."));
			actions.add(new Action("/transform/rotate", "POST", "Rotate image based on provided degrees."));
			actions.add(new Action("/downloadImage", "GET", "Download transormed image."));
			actions.add(new Action("/deleteImage", "DELETE", "Delete uploaded image."));
			break;
		default:
			actions.add(new Action("/uploadImage", "POST", "Upload an Image."));
			break;
		}

		return actions;
	}
}
