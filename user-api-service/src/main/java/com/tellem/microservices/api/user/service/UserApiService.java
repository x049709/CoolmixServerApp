package com.tellem.microservices.api.user.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.tellem.microservices.api.user.model.AxonUser;
import com.tellem.microservices.api.user.model.User;


/**
 * Created by magnus on 04/03/15.
 */
@RestController
@RefreshScope
//@PropertySource(value={"classpath:cassandra.properties"})

public class UserApiService {

    private static final Logger LOG = LoggerFactory.getLogger(UserApiService.class);

    private RestTemplate restTemplate = new RestTemplate();
	private static Cluster cluster;
	private static Session session;

    @Value("${spring.cloud.config.uri}")
    private String configUri;

    @Value("${configuration.projectName}")
    String projectName;

    @Value("${cassandra.contactpoint}")
    private String cassandraContactPoint;
    
    @Value("${cassandra.keyspace}")
    private String cassandraKeySpace;
    

    @Autowired
    private LoadBalancerClient loadBalancer;
    
    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "defaultUser")
    public List<AxonUser> getAxonUser(
        @PathVariable int userId,
        //@RequestParam(value = "user_id",  required = true) int userId,
        @RequestHeader(value="Authorization") String authorizationHeader,
        Principal currentUser) {
        LOG.info("UserApiService.getAxonUser: called with userId={}, User={}, Auth={}", userId, currentUser.getName(), authorizationHeader);
      	List<AxonUser> userList = new ArrayList<AxonUser>();     
       
		try {
			cluster = Cluster.builder().addContactPoint(cassandraContactPoint).build();
			session = cluster.connect(cassandraKeySpace);
			CassandraOperations cassandraOps = new CassandraTemplate(session);
			final String[] columns = new String[] { "user_id", "fname", "lname", "email","dob","ssn","emplid" };
			Select select = QueryBuilder.select(columns).from("axonuser");
			select.where(QueryBuilder.eq("user_id", userId));
			userList = cassandraOps.select(select, AxonUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cluster.close();
		}
        
		return userList;

    }
    
    @RequestMapping("/axonuserlist")
    @HystrixCommand(fallbackMethod = "defaultUserList")
    public List<AxonUser> getAllAxonUsers(
        @RequestHeader(value="Authorization") String authorizationHeader,
        Principal currentUser) {
        LOG.info("UserApiService.getAllAxonUsers: User={}, Auth={}", currentUser.getName(), authorizationHeader);
        //LOG.info("UserApiService.getAllAxonUsers: configUri={}, Auth={}", configUri);
       	List<AxonUser> userList = new ArrayList<AxonUser>();     
		try {
			cluster = Cluster.builder().addContactPoint(cassandraContactPoint).build();
			session = cluster.connect(cassandraKeySpace);
			CassandraOperations cassandraOps = new CassandraTemplate(session);
			final String[] columns = new String[] { "user_id", "fname", "lname", "email","dob","ssn","emplid" };
			Select select = QueryBuilder.select(columns).from("axonuser");
			userList = cassandraOps.select(select, AxonUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cluster.close();
		}
		
		return userList;

    }

    @RequestMapping(value="/axonusersave", method=RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "defaultUserSave")
    public ResponseEntity<AxonUser> saveAxonUser(
        @RequestBody AxonUser axonUser,
        Principal currentUser) {
        LOG.info("UserApiService.saveAxonUser: User={}, UserData={}", currentUser.getName(), axonUser);
		try {
			cluster = Cluster.builder().addContactPoint(cassandraContactPoint).build();
			session = cluster.connect(cassandraKeySpace);
			CassandraOperations cassandraOps = new CassandraTemplate(session);
			final String[] columns = new String[] { "user_id", "fname", "lname", "email","dob","ssn","emplid" };
			Select select = QueryBuilder.select(columns).from("axonuser");
			List<AxonUser> userList = cassandraOps.select(select, AxonUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cluster.close();
		}
		
		return new ResponseEntity<AxonUser>(axonUser, HttpStatus.OK);

    }

    
    @RequestMapping(value="/axonuseradd", method=RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "defaultUserAdd")
    public ResponseEntity<String> addAxonUser(
        @RequestBody String axonUserString,
        Principal currentUser) {
        LOG.info("UserApiService.addAxonUser: User={}, UserData={}", currentUser.getName(), axonUserString);
        ObjectMapper mapper = new ObjectMapper();
		try {
	        AxonUser user = mapper.readValue(axonUserString, AxonUser.class);
			cluster = Cluster.builder().addContactPoint(cassandraContactPoint).build();
			session = cluster.connect(cassandraKeySpace);
			CassandraOperations cassandraOps = new CassandraTemplate(session);

/*			Insert insertStmt = QueryBuilder.insertInto("axonuser")
				.value("user_id", user.getUser_id())
				.value("fname", user.getFname())
				.value("lname", user.getLname())
				.value("email", user.getEmail())
				.value("dob", user.getDob())
				.value("ssn",user.getSsn())
				.value("emplid", user.getEmplid());
			ResultSet results = session.execute(insertStmt);
*/			
			AxonUser aUser = cassandraOps.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cluster.close();
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);

    }

    /**
     * Fallback method for getAxonUser()
     *
     * @param NONE
     * @return
     */
    public List<AxonUser> defaultUser(
    	@PathVariable int userId,
        @RequestHeader(value="Authorization") String authorizationHeader,
        Principal currentUser) {
    	List<AxonUser> results = new ArrayList<AxonUser>();
        LOG.warn("Using fallback method for user-api-service.defaultUser: Called with userId={}User={}, Auth={}", userId, currentUser.getName(), authorizationHeader);
        return results;
    }

    /**
     * Fallback method for getAllAxonUsers()
     *
     * @param NONE
     * @return
     */
    public List<AxonUser> defaultUserList(
        @RequestHeader(value="Authorization") String authorizationHeader,
        Principal currentUser) {
    	List<AxonUser> results = new ArrayList<AxonUser>();
        LOG.warn("Using fallback method for user-api-service.defaultUserList: Called with User={}, Auth={}", currentUser.getName(), authorizationHeader);
        return results;
    }

    /**
     * Fallback method for saveAxonUser()
     *
     * @param NONE
     * @return
     */
    
    public ResponseEntity<AxonUser> defaultUserSave(
            @RequestBody AxonUser axonUser,
            Principal currentUser) {
        LOG.warn("Using fallback method for user-api-service.saveAxonUser: Called with User={}, UserData={}", currentUser.getName(), axonUser);
		return new ResponseEntity<AxonUser>(axonUser, HttpStatus.OK);
   }

    /**
     * Fallback method for addAxonUser()
     *
     * @param NONE
     * @return
     */
    
     public ResponseEntity<String> defaultUserAdd(
    		 @RequestBody String axonUser,
             Principal currentUser) {
        LOG.warn("Using fallback method for user-api-service.addAxonUser: Called with User={}, UserData={}", currentUser.getName(), axonUser);
		return new ResponseEntity<String>(HttpStatus.OK);
     }

}
