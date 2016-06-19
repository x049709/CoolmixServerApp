package com.coolmix.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.coolmix.service.GsrDao;
import com.coolmix.model.Gsr;
import com.coolmix.model.GsrWithImage;

@RestController
@RequestMapping("/v1.0/gsrApi")
public class GSRController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private GsrDao gsrDao;


	//-------------------Retrieve All GSRs plus images--------------------------------------------------------
	@RequestMapping(value = "/gsrWithImage/", method = RequestMethod.GET)
	public ResponseEntity<List<GsrWithImage>> listAllGsrs() {
		List<Gsr> gsrList = gsrDao.findAll();
		List<GsrWithImage> gsrWithImageList = new ArrayList<GsrWithImage>();
		try {
			for (Gsr gsr: gsrList) {
				String filePath = "/srv/www/coolmix/file/images/" + gsr.getImageFileName();
				InputStream imageStream = FileUtils.openInputStream(new File(filePath));
				FileSystemResource gsrImage = new FileSystemResource(filePath);
				GsrWithImage gsrWithImage = new GsrWithImage(gsr.getId(), gsr.getImageFilePath(), gsr.getImageFileName(), gsr.getDescription());
				gsrWithImage.setGsrImage(gsrImage);
				gsrWithImageList.add(gsrWithImage);
			}
			if(gsrWithImageList.isEmpty()){
				return new ResponseEntity<List<GsrWithImage>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (IOException e) {
			return new ResponseEntity<List<GsrWithImage>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}		
		return new ResponseEntity<List<GsrWithImage>>(gsrWithImageList, HttpStatus.OK);
	}


	//-------------------Retrieve All GSRs--------------------------------------------------------
	@RequestMapping(value = "/gsr/", method = RequestMethod.GET)
	public ResponseEntity<List<Gsr>> listAllGsrsWithImage() {
		List<Gsr> gsrList = gsrDao.findAll();
		if(gsrList.isEmpty()){
			return new ResponseEntity<List<Gsr>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Gsr>>(gsrList, HttpStatus.OK);
	}

}