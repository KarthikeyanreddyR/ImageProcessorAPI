package rokkamkarthi.ImageProcessor.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import rokkamkarthi.ImageProcessor.models.Orientation;
import rokkamkarthi.ImageProcessor.models.Params;
import rokkamkarthi.ImageProcessor.models.ReqTransform;
import rokkamkarthi.ImageProcessor.models.ResponseTemplate;
import rokkamkarthi.ImageProcessor.models.Transformation;
import rokkamkarthi.ImageProcessor.models.Utils;
import rokkamkarthi.ImageProcessor.store.ImageProcessorDataStore;

@Service
public class ImageProcessorServiceImpl implements ImageProcessorService {

	@Override
	public ResponseTemplate transform(ReqTransform inputData) {
		// check if image string is empty
		if (!Utils.validateRequestData(inputData))
			return Utils.getErrorResponse("MISSING_DATE", "Please upload required data.");

		String[] splits = inputData.getImageAsBase64String().split(",");
		if (splits.length == 0)
			return Utils.getErrorResponse("NO_IMAGE", "Please upload Image.");

		String base64String = "";
		if (splits.length == 1) {
			base64String = splits[0];
		} else {
			base64String = splits[1];
		}

		byte[] image = null;
		try {
			image = Base64.getDecoder().decode(base64String);
		} catch (IllegalArgumentException e) {
			return Utils.getErrorResponse("INVALID_IMAGE", "Please upload valid image.");
		}

		// save image
		ImageProcessorDataStore.uploadImage(image);

		// validate if provided base64 string is image or not
		boolean isValid = ImageProcessorDataStore.validateFileTypes();
		if(!isValid) {
			ImageProcessorDataStore.clearImage();
			return Utils.getErrorResponse("INVALID_IMAGE", "Please upload valid image.");
		}

		// start transformations
		for (Transformation transform : inputData.getTransformations()) {
			Params params = transform.getParameters();
			switch (transform.getTransformType()) {
			case GRAYSCALE:
				// call grayscale function
				try {
					grayscale();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case FLIP:
				// check for orientation
				// if no orientation skip transformation
				try {
					Orientation type = params.getOrientation();
					if (type != Orientation.UNKNOWN) {
						if (type == Orientation.HORIZONTAL)
							flipHorizontally();
						if (type == Orientation.VERTICAL)
							flipVertically();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case ROTATE:
				// check for degrees
				// if no data
				// check for orientation
				// if no orientation skip transformation
				// else call rotate with orientation
				// else call rotate fun with degree
				try {
					if (params.getDegrees() != 1000) {
						rotate(params.getDegrees());
					} else {
						Orientation type = params.getOrientation();
						if (type != Orientation.UNKNOWN) {
							if (type == Orientation.RIGHT)
								rotateRight();
							if (type == Orientation.LEFT)
								rotateLeft();
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case RESIZE:
				// check for width and height
				// if no data - skip transformation
				// else call resize function
				System.out.println(params.getWidth());
				System.out.println(params.getHeight());
				try {
					if (params.getWidth() != -1 && params.getHeight() != -1) {
						resize(params.getWidth(), params.getHeight());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case THUMBNAIL:
				// call thumbnail function
				try {
					thumbnail();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				// skip transformation
				break;
			}
		}

		// fetch transformed image
		byte[] outputImage = ImageProcessorDataStore.getTransformedImage();
		return Utils.transformationSuccessResponse(outputImage);
	}

	private void grayscale() throws IOException {
		BufferedImage img = ImageProcessorDataStore.getBufferedImage();
		// get image width and height
		int width = img.getWidth();
		int height = img.getHeight();

		// convert to grayscale
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = outputImage.createGraphics();

		// Maintain Image Quality
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw image
		g2d.drawRenderedImage(img, null);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, ImageProcessorDataStore.getUploadedImageType(), bao);
		ImageProcessorDataStore.updateTransformedImage(bao.toByteArray());
	}

	private void resize(int width, int height) throws IOException {
		BufferedImage inputImage = ImageProcessorDataStore.getBufferedImage();

		// creates output image
		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();

		// Maintain Image Quality
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//g2d.drawRenderedImage(inputImage, null);
		g2d.drawImage(inputImage, 0, 0, width, height, null);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, ImageProcessorDataStore.getUploadedImageType(), bao);
		ImageProcessorDataStore.updateTransformedImage(bao.toByteArray());

	}

	private void thumbnail() throws IOException {

		BufferedImage inputImage = ImageProcessorDataStore.getBufferedImage();

		int imageWidth = inputImage.getWidth();
		int imageHeight = inputImage.getHeight();
		
		System.out.println(imageWidth + "::::" + imageHeight);

		int thumbnailWidth = Utils.thumbnailWidth, thumbnailHeight = Utils.thumbnailHeight;

		// if image width and height is less than default thumbnail width and height
		if (imageWidth < thumbnailWidth && imageHeight < thumbnailHeight) {
			thumbnailWidth = imageWidth;
			thumbnailHeight = imageHeight;
		} else if (imageWidth < thumbnailWidth)
			thumbnailWidth = imageWidth;
		else if (imageHeight < thumbnailHeight)
			thumbnailHeight = imageHeight;
		
		int original_width = imageWidth;
	    int original_height = imageHeight;
	    int bound_width = thumbnailWidth;
	    int bound_height = thumbnailHeight;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
		
		System.out.println(thumbnailWidth + "::::" + thumbnailHeight);
		System.out.println(new_width + "::::" + new_height);

		resize(new_width, new_height);

	}

	private void flipHorizontally() throws IOException {

		BufferedImage inputImage = ImageProcessorDataStore.getBufferedImage();

		AffineTransform tx = AffineTransform.getScaleInstance(1.0, -1.0); // scaling
		tx.translate(0, -inputImage.getHeight()); // translating
		AffineTransformOp tr = new AffineTransformOp(tx, null); // transforming

		BufferedImage outputImage = tr.filter(inputImage, null); // filtering

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, ImageProcessorDataStore.getUploadedImageType(), bao);
		ImageProcessorDataStore.updateTransformedImage(bao.toByteArray());
	}

	private void flipVertically() throws IOException {

		BufferedImage inputImage = ImageProcessorDataStore.getBufferedImage();

		AffineTransform tx = AffineTransform.getScaleInstance(-1.0, 1.0); // scaling
		tx.translate(-inputImage.getWidth(), 0); // translating
		AffineTransformOp tr = new AffineTransformOp(tx, null); // transforming

		BufferedImage outputImage = tr.filter(inputImage, null); // filtering

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(outputImage, ImageProcessorDataStore.getUploadedImageType(), bao);
		ImageProcessorDataStore.updateTransformedImage(bao.toByteArray());
	}

	private void rotate(int degree) throws IOException {

		BufferedImage inputImage = ImageProcessorDataStore.getBufferedImage();

		double rads = Math.toRadians(degree);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = inputImage.getWidth();
		int h = inputImage.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, inputImage.getType());

		// Change image background color to white/transparent
		for (int x = 0; x < newWidth; x++) {
			for (int y = 0; y < newHeight; y++) {
				rotated.setRGB(x, y, 00000000);
			}
		}

		Graphics2D g2d = rotated.createGraphics();

		// Maintain Image Quality
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate((newWidth - w) / 2, (newHeight - h) / 2);
		g2d.rotate(rads, w / 2, h / 2);
		g2d.drawRenderedImage(inputImage, null);
		g2d.dispose();

		// Create a byte array output stream.
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(rotated, ImageProcessorDataStore.getUploadedImageType(), bao);
		ImageProcessorDataStore.updateTransformedImage(bao.toByteArray());
	}

	private void rotateRight() throws IOException {
		rotate(90);
	}

	private void rotateLeft() throws IOException {
		rotate(-90);
	}

}
