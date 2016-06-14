/*package com.coolmix;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class UserService {

    @RequestMapping(value = "/echo{in}",method=RequestMethod.GET)
    public String echo(@PathVariable(value="in") final String in, @RequestHeader HttpHeaders headers) {
    	long contentLength = headers.getContentLength();
    	return "Yo said: " + in + " length: " + contentLength;
    }
     
}*/

package com.coolmix;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class UserService {

    @RequestMapping(value = "/echo/{in}", method = RequestMethod.GET)
    public String echo(@PathVariable(value="in") final String in, @RequestHeader HttpHeaders headers) {
    	Map<String, String> map = headers.toSingleValueMap();
    	String authorization = map.get("authorization");
    	String credentials =  null;
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":",2);
        }
        return "Yo said: " + in + " cred: " + credentials;
    }
}