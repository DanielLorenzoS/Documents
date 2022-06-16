package com.uploadownload.uploadownload.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.uploadownload.uploadownload.entities.FileDB;
import com.uploadownload.uploadownload.message.ResponseFile;
import com.uploadownload.uploadownload.message.ResponseMessage;
import com.uploadownload.uploadownload.services.FileStorageService;

@RestController
public class FileController {

	@Autowired
	private FileStorageService storageService;

	@PostMapping("/files")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
		String message = "";
		try {
			storageService.store(multipartFile);
			message = "EL archivo se ha subido correctamente: " + multipartFile.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "No se pudo subir el archivo: " + multipartFile.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			var idd = dbFile.getId();
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(idd)
					.toUriString();
			return new ResponseFile(dbFile.getId(), dbFile.getName(), fileDownloadUri, dbFile.getType(),
					dbFile.getData().length);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		FileDB fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}

}
