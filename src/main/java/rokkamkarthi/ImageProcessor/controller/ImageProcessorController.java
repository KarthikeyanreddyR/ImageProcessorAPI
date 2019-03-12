package rokkamkarthi.ImageProcessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rokkamkarthi.ImageProcessor.models.ReqTransform;
import rokkamkarthi.ImageProcessor.models.ResponseTemplate;
import rokkamkarthi.ImageProcessor.models.Utils;
import rokkamkarthi.ImageProcessor.service.ImageProcessorServiceImpl;

/**
 * @author rokkamkarthi
 *
 */

@RestController
@RequestMapping("/api")
public class ImageProcessorController {

	@Autowired
	private ImageProcessorServiceImpl service;

	@GetMapping("/")
	public ResponseEntity<ResponseTemplate> root() {
		ResponseTemplate res = Utils.landingPageResponse();
		return new ResponseEntity<ResponseTemplate>(res, HttpStatus.OK);
	}

	@RequestMapping(path = "/transform", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseTemplate> applyTransformations(@RequestBody ReqTransform reqTransform) {
		System.out.println(reqTransform.toString());
		ResponseTemplate res = service.transform(reqTransform);
		return new ResponseEntity<ResponseTemplate>(res, HttpStatus.valueOf(res.getStatus()));
	}

	/*
	 * @GetMapping("/download") public ResponseEntity<ByteArrayResource>
	 * downloadImage() { byte[] data =
	 * ImageProcessorDataStore.getTransformedImage(); ByteArrayResource res = new
	 * ByteArrayResource(data); return
	 * ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).
	 * contentLength(data.length) .header(HttpHeaders.CONTENT_DISPOSITION,
	 * "attachment;filename=new_image." +
	 * ImageProcessorDataStore.getUploadedImageType()) .body(res);
	 * 
	 * }
	 */
}
