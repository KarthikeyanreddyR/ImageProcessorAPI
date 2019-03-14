package rokkamkarthi.ImageProcessor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import rokkamkarthi.ImageProcessor.models.ResponseTemplate;
import rokkamkarthi.ImageProcessor.models.Utils;

@ControllerAdvice
@RestController
public class ImageProcessorExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ResponseTemplate> handleAll(Exception ex, WebRequest request) {
		ResponseTemplate res = Utils.getErrorResponse("INTERNAL_ERROR", "Internal server error..");
		return new ResponseEntity<ResponseTemplate>(res, new HttpHeaders(), HttpStatus.valueOf(res.getStatus()));
	}

}
