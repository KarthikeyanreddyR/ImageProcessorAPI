package rokkamkarthi.ImageProcessor.store;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import rokkamkarthi.ImageProcessor.models.ImageStates;

public class ImageProcessorDataStore {

	private static byte[] _uploadedImage = null;
	private static String _imageName = null;
	private static String _imageType = null;
	private static byte[] _transformedImage = null;
	
	public static ImageStates states = ImageStates.IMAGE_ABSENT;

	public static void setUploadFile(MultipartFile file) throws IOException {
		_uploadedImage = file.getBytes();
		_imageName = file.getResource().getFilename();
		_imageType = _imageName.substring(_imageName.lastIndexOf(".") + 1);
		_transformedImage = null;
		states = ImageStates.IMAGE_PRESENT;
	}

	public static void clearUploadFile() {
		_uploadedImage = null;
		_imageName = null;
		_imageType = null;
		_transformedImage = null;
		states = ImageStates.IMAGE_ABSENT;
	}

	public static boolean isFileUploaded() {
		return _uploadedImage != null;
	}

	public static BufferedImage getBufferedImage() throws IOException {
		BufferedImage originalImage = null;
		InputStream in = null;
		if (_transformedImage == null) {
			in = new ByteArrayInputStream(_uploadedImage);
			originalImage = ImageIO.read(in);
		} else {
			in = new ByteArrayInputStream(_transformedImage);
			originalImage = ImageIO.read(in);
		}
		return originalImage;
	}
	
	public static void updateTransformedImage(byte[] newImageByteArray) {
		_transformedImage = newImageByteArray;
	}
	
	public static String getUploadedImageType() {
		return _imageType;
	}

	// Supported file types are BMP, GIF, JPG, PNG
	public static boolean validateFileTypes() { // throws some custom exception

		if (!isFileUploaded())
			return false;

		try {
			// check for BMP, GIF, JPG and PNG types.
			ImageIO.read(new ByteArrayInputStream(_uploadedImage)).toString();
			return true;
		} catch (Exception e) {
			states = ImageStates.IMAGE_ABSENT;
			return false;
		}
	}

}
