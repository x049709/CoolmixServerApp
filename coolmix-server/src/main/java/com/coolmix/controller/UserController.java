package com.coolmix.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.coolmix.service.UserService;
import com.coolmix.model.User;

@RestController
@RequestMapping("/v1.0/userApi")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;  //Service which will do all data retrieval/manipulation work

	//-------------------Echo Input for Testing--------------------------------------------------------
	@RequestMapping(value = "/echo/{in}", method = RequestMethod.GET)
	public String echo(@PathVariable(value="in") final String in, @RequestHeader HttpHeaders headers) {
		Map<String, String> map = headers.toSingleValueMap();
		String authTag = map.get("authorization");
		String credentials =  null;
		String[] credValues =  null;
		if (authTag != null && authTag.startsWith("Basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authTag.substring("Basic".length()).trim();
			credentials = new String(Base64.getDecoder().decode(base64Credentials),
					Charset.forName("UTF-8"));
			// credentials = username:password
			credValues = credentials.split(":",2);
		}
		return "You said: " + in + " credentials: " + credValues[0] + ":" + credValues[1];
	}

	//-------------------Retrieve All Users--------------------------------------------------------
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if(users.isEmpty()){
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	//-------------------Retrieve Single User--------------------------------------------------------
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {
	    LOG.info("userApi.getUser: UserId={}", id);
        System.out.println("Fetching User with id " + id);
		User user = userService.findById(id);
		if (user == null) {
		    LOG.info("userApi.getUser: UserId not found={}", id);
			//return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<User>(HttpStatus.OK);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	//-------------------Create a User--------------------------------------------------------
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
	    LOG.info("userApi.createUser: UserName={}", user.getName());
        System.out.println("Creating User " + user.getName());

		if (userService.doesUserExist(user)) {
			System.out.println("A User with name " + user.getName() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	//-------------------Get Image--------------------------------------------------------
	@RequestMapping(value = "/getImage/{imageName}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getImage(@PathVariable("imageName") String imageName) {
	    LOG.info("userApi.getImage: UserId={}", imageName);
	    String fileLocation = "/srv/www/coolmix/file/images/" + imageName;
        System.out.println("Fetching " + fileLocation);
        FileSystemResource fileSystemResource = new FileSystemResource(fileLocation);
	    return fileSystemResource;
	}


	//-------------------Upload an Image--------------------------------------------------------
	@RequestMapping(value = "/uploadImage/", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload( 
            @RequestParam("file") MultipartFile file){
            String name = "test11";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = "/srv/www/coolmix/file/images/" + name + "-uploaded.jpg";
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
	//------------------- Update a User --------------------------------------------------------
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
	    LOG.info("userApi.updateUser: UserId={}", id);
        System.out.println("Updating User " + id);

		User currentUser = userService.findById(id);

		if (currentUser==null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setEmail(user.getEmail());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	//------------------- Delete a User --------------------------------------------------------
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
	    LOG.info("userApi.deleteUser: UserId={}", id);
        System.out.println("Fetching & Deleting User with id " + id);
	
		User user = userService.findById(id);
		if (user == null) {
			System.out.println("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	//------------------- Delete All Users --------------------------------------------------------
	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
	    LOG.info("userApi.deleteAllUsers");
        System.out.println("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

}