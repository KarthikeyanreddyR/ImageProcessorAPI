package rokkamkarthi.ImageProcessor.service;

import rokkamkarthi.ImageProcessor.models.ReqTransform;
import rokkamkarthi.ImageProcessor.models.ResponseTemplate;

public interface ImageProcessorService {
	
	public ResponseTemplate transform(ReqTransform reqTransform);

}
