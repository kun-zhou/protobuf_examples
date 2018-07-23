package me.kunzhou.protobuf_examples.json_to_protobuf;

import com.google.protobuf.StringValue;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;


/* ** README **
 * ------------
 * Because protobuf's generated code is not user-friendly, and can easily
 * find its way into your business logic, helper classes are needed.
 *
 * This example present helper wrappers around the genereated code to allow 
 * Java Beans style of accessing protobuf messages. It also showcases how  
 * validation could be done, and how we can leverage the wrappers to map 
 * JSON to protobuf protobuf message back and forth with ObjectMapper.
 * 
 * Ideally, these helper classes will be generated from the
 * protobuf definitions.
 *
 */

public class App {

    public static void main(String[] args) throws Exception {

    	ObjectMapper mapper = new ObjectMapper();

    	SecretProxy secret = mapper.readValue(
    		App.class.getResourceAsStream("/secret.json"), 
            SecretProxy.class
    	);

    	// test whether JSON is mapped
    	System.out.println(secret.getUsername());
    	System.out.println(secret.getDetail().getWebsite());

    	// test whether setting nested field to bull works
    	secret.getDetail().setWebsite(null);
    	System.out.println(secret._build().getDetail().hasWebsite());

        // test whether mutating the child builder after it has been added
        //  mutates the parent builder
        DetailProxy detailProxy = new DetailProxy("www.163.com");
        secret.setDetail(
            detailProxy
        );
        detailProxy.setWebsite("www.google.com");
        System.out.println(secret._build().getDetail().getWebsite());
        //---------

        


        // System.out.println("---");
        // System.out.println(secret_hubspot);
        // System.out.println("---");
        //------
        // test mapping to JSON again
        System.out.println(
            mapper.writeValueAsString(secret)
        );
    }
}

// Below classes should be generated
class SecretProxy {

    	private final Secret.Builder builder;
        private DetailProxy detail;

    	@JsonCreator
    	SecretProxy(
            @JsonProperty("username") String username, 
            @JsonProperty("detail") DetailProxy detail
        ) {
    		builder = Secret.newBuilder();
    		setUsername(username);
    		setDetail(detail);
    	}

    	SecretProxy(Secret.Builder builder) {
    		this.builder = builder;
    	}

    	SecretProxy(Secret detail) {
    		this.builder = detail.toBuilder();
    	}

    	void setUsername(String username) {
    		if (username == null) {
    			builder.clearUsername();
    		} else {
    			builder.setUsername(StringValue.of(username));
    		}
    	}

        @JsonGetter
    	String getUsername() {
			return builder.hasUsername() ? builder.getUsername().getValue() : null;
    	}

    	void setDetail(DetailProxy detail) {
    		this.detail = detail;
    	}
        
        @JsonGetter
    	DetailProxy getDetail() {
    		// always use getMessageTypeBuilder when getMessageType
    		// so the source of truth is the builder's sub-builder
			return detail;
    	}

    	// Validation Generated Code
    	// boolean validate() {
    	// 	return _validateUsername() && _validateDetail();
    	// }

    	// boolean _validateUsername() {
    	// 	return something;
    	// }

    	// boolean _validateDetail() {
    	// 	return getDetail().validate();
    	// }

    	// Others
    	Secret.Builder _getBuilder() {
    		return builder.setDetail(detail._build());
    	}

    	Secret _build() {
    		return _getBuilder().build();
    	}
    }

class DetailProxy {

    	private Detail.Builder builder;

    	@JsonCreator
    	DetailProxy(@JsonProperty("website") String url) {
    		builder = Detail.newBuilder();
    		setWebsite(url);
    	}

    	DetailProxy(Detail.Builder builder) {
    		this.builder = builder;
    	}

    	DetailProxy(Detail detail) {
    		this.builder = detail.toBuilder();
    	}

    	void setWebsite(String url) {
    		if (url == null) {
    			builder.clearWebsite();
    		} else {
    			builder.setWebsite(StringValue.of(url));
    		}
    	}

        @JsonGetter
    	String getWebsite() {
    		return builder.hasWebsite() ? builder.getWebsite().getValue() : null;
    	}

    	Detail.Builder _getBuilder() {
    		return builder;
    	}

    	Detail _build() {
    		return builder.build();
    	}
    }
