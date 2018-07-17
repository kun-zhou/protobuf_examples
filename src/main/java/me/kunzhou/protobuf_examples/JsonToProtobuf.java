package me.kunzhou.protobuf_examples;

import me.kunzhou.protobuf_examples.json_to_protobuf.*;

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
 * Java Beans style of accessing protobuf messages. It also showcases how validation 
 * could be done, and how we can leverage the wrappers to map JSON to protobuf
 * protobuf message back and forth with ObjectMapper
 * 
 * Ideally, these helper classes will be generated from the
 * protobuf definitions.
 */

public class JsonToProtobuf {

    public static void main(String[] args) throws Exception {

    	ObjectMapper mapper = new ObjectMapper();

    	SecretProxy secret = mapper.readValue(
    		JsonToProtobuf.class.getResourceAsStream("/secret.json"), 
            SecretProxy.class
    	);

    	// test whether JSON is mapped
    	System.out.println(secret.getUsername());
    	System.out.println(secret.getDetail().getWebsite());

    	// test whether setting nested field to bull works
    	secret.getDetail().setWebsite(null);
    	System.out.println(secret.toProto().getDetail().hasWebsite());

        // test mapping to JSON again
        System.out.println(
            mapper.writeValueAsString(secret)
        );
    }
}

// Below classes should be generated
class SecretProxy {

    	private Secret.Builder builder;

    	@JsonCreator
    	SecretProxy(@JsonProperty("username") String username, @JsonProperty("detail") DetailProxy detail) {
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
    		if (detail == null) {
    			builder.clearDetail();
    		} else {
    			builder.setDetail(detail._getBuilder());
    		}
    	}
        @JsonGetter
    	DetailProxy getDetail() {
    		// always use getMessageTypeBuilder when getMessageType
    		// so the source of truth is the builder's sub-builder
			return builder.hasDetail() ? new DetailProxy(builder.getDetailBuilder()) : null;
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
    		return builder;
    	}

    	Secret toProto() {
    		return builder.build();
    	}
    }

class DetailProxy {

    	private Detail.Builder builder;

    	@JsonCreator
    	DetailProxy(@JsonProperty("website")String url) {
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

    	Detail toProto() {
    		return builder.build();
    	}
    }
