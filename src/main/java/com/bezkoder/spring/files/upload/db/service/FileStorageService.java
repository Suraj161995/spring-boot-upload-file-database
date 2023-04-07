package com.bezkoder.spring.files.upload.db.service;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.stream.Stream;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.files.upload.db.model.FileDB;
import com.bezkoder.spring.files.upload.db.model.StorageServiceRequest;
import com.bezkoder.spring.files.upload.db.model.StorageServiceResponse;
import com.bezkoder.spring.files.upload.db.repository.FileDBRepository;

@Service
public class FileStorageService {

	@Autowired
	private FileDBRepository fileDBRepository;


	String storageURL = "https://8e7pc1dhyi.execute-api.us-east-1.amazonaws.com/dev/storeimage";

	public ResponseEntity<StorageServiceResponse> store(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		File img = addTextWatermark("APPROVED", "png", file, null);
		FileDB FileDB = new FileDB(fileName, new MimetypesFileTypeMap().getContentType(img),
				Files.readAllBytes(img.toPath()));
		String b64 = Base64.getEncoder().encodeToString(Files.readAllBytes(img.toPath()));

		// Harsh api to convert png to pdf format.
		fileDBRepository.save(FileDB);
		return storageServiceCall(fileName, b64);
		 
	}

	private ResponseEntity<StorageServiceResponse> storageServiceCall(String fileName, String b64) {
		StorageServiceRequest storageServiceRequest = new StorageServiceRequest();
		storageServiceRequest.setImage_data(b64);
		String[] arrOfStr = fileName.split("[.]");
		storageServiceRequest.setImageName(arrOfStr[0]);
		storageServiceRequest.setBucketName("bucketfortest010695");
		storageServiceRequest.setServiceType("nonqueue");
		storageServiceRequest.setExtension(".jpg");
		RestTemplate rs = new RestTemplate();
		HttpEntity<StorageServiceRequest> httpEntity = new HttpEntity<>(storageServiceRequest);
		ResponseEntity<StorageServiceResponse> responseEntity = rs.exchange(storageURL, HttpMethod.POST, httpEntity, StorageServiceResponse.class);
		return responseEntity;
	}

	private File addTextWatermark(String text, String type, MultipartFile source, MultipartFile destination)
			throws IOException {
		File s_file = new File(source.getOriginalFilename());
		s_file.createNewFile();
		FileOutputStream fos = new FileOutputStream(s_file);
		fos.write(source.getBytes());
		fos.close();
		BufferedImage image = ImageIO.read(s_file);

		// determine image type and handle correct transparency
		int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
		BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

		// initializes necessary graphic properties
		Graphics2D w = (Graphics2D) watermarked.getGraphics();
		w.drawImage(image, 0, 0, null);
		AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
		w.setComposite(alphaChannel);
		w.setColor(Color.GREEN);
		w.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 50));
		FontMetrics fontMetrics = w.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds(text, w);

		// calculate center of the image
		int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
		int centerY = image.getHeight() / 2;

		// add text overlay to the image
		w.drawString(text, centerX, centerY);
		File d_file = new File(source.getOriginalFilename());
		d_file.createNewFile();
		FileOutputStream fs = new FileOutputStream(d_file);
		fs.write(source.getBytes());
		fs.close();
		ImageIO.write(watermarked, type, d_file);
		w.dispose();
		return d_file;
	}

	public FileDB getFile(String id) {
		return fileDBRepository.findById(id).get();
	}

	public Stream<FileDB> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}

}
