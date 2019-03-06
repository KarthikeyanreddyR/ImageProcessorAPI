package rokkamkarthi.ImageProcessor.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import rokkamkarthi.ImageProcessor.models.ResponseTemplate;
import rokkamkarthi.ImageProcessor.models.Utils;

/**
 * @author rokkamkarthi
 *
 */

@Service
public class ImageProcessorService {

	private static byte[] _uploadedImage = null;
	public static String _imageName = null;
	public static String _imageType = null;
	private static byte[] _transformedImage = null;

	public void setUploadFile(MultipartFile file) throws IOException {
		_uploadedImage = file.getBytes();
		_imageName = file.getResource().getFilename();
		_imageType = _imageName.substring(_imageName.lastIndexOf(".") + 1);
		_transformedImage = null;
	}

	public void clearUploadFile() {
		_uploadedImage = null;
		_imageName = null;
		_imageType = null;
		_transformedImage = null;
	}

	private boolean isFileUploaded() {
		return _uploadedImage != null;
	}

	// Supported file types are BMP, GIF, JPG, PNG
	public boolean validateFileTypes() { // throws some custom exception

		if (!isFileUploaded())
			return false;

		try {
			// check for BMP, GIF, JPG and PNG types.
			ImageIO.read(new ByteArrayInputStream(_uploadedImage)).toString();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private BufferedImage getBufferedImage() throws IOException {
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

	public ResponseTemplate landingPage() {
		return Utils.landingPageResponse();
	}

	public ResponseTemplate grayscale() throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage img = getBufferedImage();
		// get image width and height
		int width = img.getWidth();
		int height = img.getHeight();

		// convert to grayscale
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);

				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;

				// calculate average
				int avg = (r + g + b) / 3;

				// replace RGB value with avg
				p = (a << 24) | (avg << 16) | (avg << 8) | avg;

				img.setRGB(x, y, p);
			}
		}
		
		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(img, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}

	public ResponseTemplate resize(int width, int height) throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage inputImage = getBufferedImage();

		// creates output image
		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(inputImage, 0, 0, width, height, null);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());

	}

	public ResponseTemplate thumbnail(int thumbWidth, int thumbHeight) throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage inputImage = getBufferedImage();

		// adjust width & height
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = inputImage.getWidth(null);
		int imageHeight = inputImage.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		// if image width and height is less than provided thumbnail width and height
		if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
			thumbWidth = imageWidth;
			thumbHeight = imageHeight;
		} else if (imageWidth < thumbWidth)
			thumbWidth = imageWidth;
		else if (imageHeight < thumbHeight)
			thumbHeight = imageHeight;

		// creates output image
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = thumbImage.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, thumbWidth, thumbHeight);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(inputImage, 0, 0, thumbWidth, thumbHeight, null);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(thumbImage, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}

	public ResponseTemplate flipHorizontally() throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage inputImage = getBufferedImage();

		AffineTransform tx = AffineTransform.getScaleInstance(1.0, -1.0); // scaling
		tx.translate(0, -inputImage.getHeight()); // translating
		AffineTransformOp tr = new AffineTransformOp(tx, null); // transforming

		BufferedImage outputImage = tr.filter(inputImage, null); // filtering

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}

	public ResponseTemplate flipVertically() throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage inputImage = getBufferedImage();

		AffineTransform tx = AffineTransform.getScaleInstance(-1.0, 1.0); // scaling
		tx.translate(-inputImage.getWidth(), 0); // translating
		AffineTransformOp tr = new AffineTransformOp(tx, null); // transforming

		BufferedImage outputImage = tr.filter(inputImage, null); // filtering

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}

	public ResponseTemplate rotate(int degree) throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage inputImage = getBufferedImage();

		double rads = Math.toRadians(degree);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = inputImage.getWidth();
		int h = inputImage.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, inputImage.getType());
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		at.rotate(rads, w / 2, h / 2);
		g2d.setTransform(at);
		g2d.drawImage(inputImage, 0, 0, null);
		g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(rotated, _imageType, bao);
		_transformedImage = bao.toByteArray();
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}

	public ResponseTemplate downloadImage() throws IOException {
		if (!isFileUploaded())
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image..");

		BufferedImage img = getBufferedImage();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(img, _imageType, bao);
		return Utils.transformationSuccessResponse(bao.toByteArray());
	}
}
