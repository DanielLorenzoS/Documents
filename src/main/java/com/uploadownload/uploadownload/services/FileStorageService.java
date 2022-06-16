package com.uploadownload.uploadownload.services;

import java.io.IOException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.uploadownload.uploadownload.entities.FileDB;
import com.uploadownload.uploadownload.exception.FileUploadExceptionAdvice;
import com.uploadownload.uploadownload.repositories.FileDBRepository;

@Service
public class FileStorageService {

	@Autowired
	FileDBRepository fileDBRepository;

	public FileDB store(MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Extrae el nombre del archivo
		FileDB FileDB = new FileDB(fileName, multipartFile.getContentType(), multipartFile.getBytes()); // Extrae la
																										// propiedad que
																										// indica el
																										// tipo de
																										// archivo y la
																										// cantidad de
																										// bytes
		return fileDBRepository.save(FileDB); // Almacena los datos del archivo en la base de datos con el repositorio
	}

	public FileDB getFile(String id) {
		return fileDBRepository.findById(id).get();
	}

	public Stream<FileDB> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}

}
