package rokkamkarthi.ImageProcessor.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rokkamkarthi.ImageProcessor.models.ResponseTemplate;
import rokkamkarthi.ImageProcessor.models.Utils;
import rokkamkarthi.ImageProcessor.service.ImageProcessorService;

/**
 * @author rokkamkarthi
 *
 */

@RestController
@RequestMapping("/api")
public class ImageProcessorController {

	@Autowired
	private ImageProcessorService service;

	@GetMapping("/")
	public ResponseEntity<ResponseTemplate> root() {
		ResponseTemplate res = service.landingPage();
		return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
	}

	@RequestMapping(path = "/uploadImage", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseTemplate> uploadImage(@RequestParam("file") MultipartFile file) {

		// check if uploaded file is empty
		if (file == null || file.isEmpty()) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("NO_IMAGE", "Please upload Image.."),
					HttpStatus.BAD_REQUEST);
		}

		// save uploaded file as input stream in service
		try {
			service.setUploadFile(file);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// check if file is proper image or not - checks for BMP, GIF, JPG and PNG types
		boolean isValid = service.validateFileTypes();
		if (!isValid) {
			service.clearUploadFile();
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INVALID_IMAGE_TYPE",
					"API supports only BMP, GIF, JPG and PNG Image types."), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<ResponseTemplate>(Utils.uploadSuccessResponse(), HttpStatus.OK);
	}

	@PostMapping("/transform/grayscale")
	public ResponseEntity<ResponseTemplate> grayScale() {
		try {
			ResponseTemplate res = service.grayscale();
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transform/resize")
	public ResponseEntity<ResponseTemplate> resize(@RequestParam("width") int width,
			@RequestParam("height") int height) {
		try {
			ResponseTemplate res = service.resize(width, height);
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transform/thumbnail")
	public ResponseEntity<ResponseTemplate> thumbnail(@RequestParam("thumbWidth") int thumbWidth,
			@RequestParam("thumbHeight") int thumbHeight) {
		try {
			ResponseTemplate res = service.thumbnail(thumbWidth, thumbHeight);
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transform/flip/horizontal")
	public ResponseEntity<ResponseTemplate> flipHorizontally() {
		try {
			ResponseTemplate res = service.flipHorizontally();
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transform/flip/vertical")
	public ResponseEntity<ResponseTemplate> flipVertically() {
		try {
			ResponseTemplate res = service.flipVertically();
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transform/rotate")
	public ResponseEntity<ResponseTemplate> rotate(@RequestParam("degree") int degree) throws IOException {

		// degree must be between -360 to 360
		if (degree < -361 || degree > 361) {
			return new ResponseEntity<ResponseTemplate>(
					Utils.getErrorResponse("INVALID_DEGREE", "degree value is invalid.."), HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseTemplate res = service.rotate(degree);
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/downloadImage")
	public ResponseEntity<ResponseTemplate> downloadImage() {
		try {
			ResponseTemplate res = service.downloadImage();
			return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<ResponseTemplate>(Utils.getErrorResponse("INTERNAL_ERROR", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
