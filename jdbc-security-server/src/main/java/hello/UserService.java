package hello;

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
     
}