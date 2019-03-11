package rokkamkarthi.ImageProcessor.store;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class ImageProcessorDataStore {

	private static byte[] _uploadedImage = null;
	private static String _imageType = "png";
	private static byte[] _transformedImage = null;

	public static void uploadImage(byte[] bytes) {
		_uploadedImage = bytes;
		_transformedImage = bytes;
	}

	public static void clearImage() {
		_uploadedImage = null;
		_transformedImage = null;
	}

	public static boolean isFileUploaded() {
		return _uploadedImage != null;
	}

	public static void updateTransformedImage(byte[] newImageByteArray) {
		_transformedImage = newImageByteArray;
	}

	public static byte[] getTransformedImage() {
		return _transformedImage;
	}

	public static String getUploadedImageType() {
		return _imageType;
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

	// Supported file types are BMP, GIF, JPG, PNG
	public static boolean validateFileTypes() { // throws some custom exception

		if (!isFileUploaded())
			return false;

		try {
			// check for BMP, GIF, JPG and PNG types.
			ByteArrayInputStream bis = new ByteArrayInputStream(_uploadedImage);
			ImageIO.read(bis).toString();
			String contentType = URLConnection.guessContentTypeFromStream(bis);
			if (contentType != null) {
				_imageType = contentType.substring(contentType.lastIndexOf("/") + 1);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
