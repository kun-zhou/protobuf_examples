package me.kunzhou.protobuf_examples;

import me.kunzhou.protobuf_examples.nullability_and_presense_demo.*;

import com.google.protobuf.NullValue;
import com.google.protobuf.StringValue;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Descriptors;

import com.google.protobuf.util.FieldMaskUtil;
import com.google.protobuf.util.JsonFormat;

import java.util.Map;

public class NullabilityAndPresenseDemo {

    // an example of a REST PATCH request but in protobuf
    // the field `entity.id` is updated to null

    public static void main(String[] args) {
    // 
    //*** 1. Use fieldmask for presence and wrapper for nullability ***//
    //
        System.out.println(">>> 1. Use fieldmask for presence and wrapper for nullability");
        FieldMaskUtil.MergeOptions mergeOptions = new FieldMaskUtil.MergeOptions();
        
        // this makes sure if a patch has a message field that is UNSET, then
        // applying the patch agaist the complete message causes that field
        // to become unset in the complete message, too. This is important 
        // as we map set/unset to not-null/null.
        mergeOptions.setReplaceMessageFields(true); 
        
        // the below option not useful with proto3
        // mergeOptions.setReplacePrimitiveFields(true); 

        // construct an original entity
        Entity original = Entity
            .newBuilder()
            .setId(StringValue.newBuilder().setValue("ABC").build())
            .setOthers("Hello")
            .build();

        // CLIENT SIDE CODE
        /// construct a patch
        PatchRequest request = PatchRequest
            .newBuilder()
            .setMask(
                FieldMask
                    .newBuilder()
                    .addPaths("id")  // notice `id` is added as mask
                    .addPaths("others")
                    .build()  
            )
            .setEntity(
                Entity
                    .newBuilder()
                    .setOthers("placeholder")
                    .build() // notice that we do NOT SET `id`
            )
            .build();

        // END CLIENT SIDE CODE

        // SERVER SIDE CODE
        // Apply patch, original should be feteched from datastore.
        Entity.Builder modifiedBuilder = original.toBuilder(); 
        FieldMaskUtil.merge(
            request.getMask(),
            request.getEntity(),
            modifiedBuilder,
            mergeOptions
        );
        Entity modified = modifiedBuilder.build();
        // END SERVER SIDE CODE

        // try { System.out.println(JsonFormat.printer().print(modified)); } catch (Exception e){};
        System.out.println(modified.hasId());
    //
    //*** 2. Use oneof for nullability and wrapper for existence ***//
    //
        // construct an original entity
        EntityT originalT = EntityT
            .newBuilder()
            .setId(NullableString.newBuilder().setStringValue("ABC").build())
            .setOthers("Hello")
            .build();

        // CLIENT SIDE CODE
        /// construct a patch
        PatchRequestT requestT = PatchRequestT
            .newBuilder()
            .setEntity(
                EntityT
                    .newBuilder()
                    .setOthers("placeholder")
                    .setId(NullableString.newBuilder().build())
                    // notice how we omit setting specific values for the 
                    // oneof but set the NullableString field
                    .build()
            )
            .build();
        // END CLIENT SIDE CODE

        // SERVER SIDE CODE
        // Since the FieldMaskUtil only supports fieldmask, we have to
        // work hard ourselves to merge changes.
        EntityT.Builder modifiedBuilderT = originalT.toBuilder();

        // Heavy use of reflection
        for ( Map.Entry<Descriptors.FieldDescriptor, Object> entry : 
            requestT.getEntity().getAllFields().entrySet()) {
                modifiedBuilderT.setField(entry.getKey(), entry.getValue());
        }
        EntityT modifiedT = modifiedBuilderT.build();
        
        System.out.println(modifiedT.getId().getStringCase() != NullableString.StringCase.STRING_NOT_SET);
    //
    //*** 3. Use two wrappers for nullability and existence ***//
    //
        // this is basically method 2, actually somewhat simpler in code
        // because you just need 
        // modified.getId().hasNullableString()
        // to check for nullability, and use
        // modified.getId().getNullableString().hasValue()
        // to check for existence, we do it similar to 2
    }
}

