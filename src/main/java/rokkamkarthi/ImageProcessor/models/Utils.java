package rokkamkarthi.ImageProcessor.models;

import java.util.Base64;

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
		Action action = new Action("/uploadImage", "POST", "Upload an Image.");
		responseTemplate.addAction(action);
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
		Action action = new Action("/uploadImage", "POST", "Upload an Image.");
		responseTemplate.addAction(action);
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
		Action action = new Action("/uploadImage", "POST", "Upload an Image.");
		responseTemplate.addAction(action);
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
		Action action = new Action("/uploadImage", "POST", "Upload an Image.");
		responseTemplate.addAction(action);
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
}
