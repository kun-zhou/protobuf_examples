package me.kunzhou.protobuf_examples.jackson_json_to_protobuf;

import com.google.protobuf.StringValue;
import com.google.protobuf.Int64Value;
import com.google.protobuf.DoubleValue;

import java.util.Map;

import com.google.protobuf.util.JsonFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/* ** README **
 * ------------
 * Using com.hubspot.jackson.datatype.protobuf.ProtobufModule to convert
 * between JSON (serialized format) and protobuf (deserialized format)
 * 
 * It support mapping Wrapper to Null neatly
 */

public class App {

    public static void main(String[] args) throws Exception {
        // 0. Initlialization

        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ProtobufModule())
            .setSerializationInclusion(Include.ALWAYS); // to preserve `null`

        // -- construct proto and example json first
        final Detail detail = Detail
            .newBuilder()
            //.setWebsite(StringValue.of("cnn.com"))
            .setNum(DoubleValue.of(97912412))
            .build();
        final String jsonString = "{\"num\":9123.1241}";

        //----------------------------------------------------------------//

        // 1. Serialize protobuf to JSON
        System.out.println(
            mapper.writeValueAsString(detail)
        );

        // 2. Deserialize JSON to protobuf
        final Detail d = mapper.readValue(jsonString, Detail.class);
        System.out.println(
            d.getNum().getValue() + " "+ d.hasWebsite()
        );
    }
}