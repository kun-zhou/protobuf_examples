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
    // the field `entity.others.owner.id` is updated to null

    //****************************************************************//
     //*** 0. Preliminaries **//
    //****************************************************************//
    public static void main(String[] args) {
        String entityId = "b116768c-dac0-4ef1-8024-7d3056c6b186";
        String ownerId = "7d304ef1-8c-dac002b1167684-56c6b1a1sx";
        Entity entity = Entity
            .newBuilder()
            .setId(StringValue.newBuilder().setValue(entityId).build())
            .setOthers(
                Others
                    .newBuilder()
                    .setOwner(
                        Owner
                        .newBuilder()
                        .setId(StringValue.newBuilder().setValue(ownerId).build())
                        .build()
                    )
                    .build()
            )
            .build();

        EntityT entityT = EntityT
            .newBuilder()
            .setId(NullableString.newBuilder().setValue(entityId).build())
            .setOthers(
                OthersT
                    .newBuilder()
                    .setOwner(
                        OwnerT
                        .newBuilder()
                        .setId(NullableString.newBuilder().setValue(ownerId).build())
                        .build()
                    )
                    .build()
            )
            .build();
        FieldMaskUtil.MergeOptions mergeOptions = new FieldMaskUtil.MergeOptions();
        mergeOptions.setReplaceMessageFields(true); 

      //****************************************************************//
     //*** 1. Use fieldmask for presence and wrapper for nullability **//
    //****************************************************************//
        System.out.println("1. Use fieldmask for presence and wrapper for nullability");

        String field_to_update = "others.owner.id";
        String fieldToSetAsPresent = field_to_update;
         System.out.println("Creating a patch that update "+field_to_update+" to null");
        // 1.1 CLIENT side setting a field as not present
            //just don't include in field mask
        // 1.2 CLIENT side setting a field as present
            // just include in fieldmask
        FieldMask fieldMask = FieldMask
            .newBuilder()
            .addPaths(fieldToSetAsPresent)  // set "others.owner.id" as present
            .build();

        // 1.3 CLIENT side setting a field as NULL
        String fieldToSetAsNull = fieldToSetAsPresent;
        Entity patch = Entity.newBuilder()
            .setOthers(
                Others
                    .newBuilder()
                    .setOwner(
                        Owner.newBuilder().build()
                        // DO NOT .setId(), which is fieldToSetAsNull
                    )
                    .build()
            )
            .build();

        PatchRequest patchRequest = PatchRequest
            .newBuilder()
            .setEntity(patch)
            .setMask(fieldMask)
            .build();
        //-------------------------------------------------------------
        patch = patchRequest.getEntity();
        String field_to_check = field_to_update;
        // 1.4 SERVER side checking a field is present
        // 1.5 SERVER side check field as null
        // 1.6 SERVER side get non-null
        System.out.print("In the patch, ");
        if (!fieldMask.getPathsList().contains(field_to_check)) {
            System.out.println(field_to_check + " is not present");
        } else if (!patch.getOthers().getOwner().hasId()) {
            System.out.println(field_to_check + " is null");
        } else {
             System.out.println(field_to_check + " is " + patch.getOthers().getOwner().getId().getValue());
        }

        // 1.7 SERVER side MERGING
        Entity.Builder modifiedBuilder = entity.toBuilder(); 
        FieldMaskUtil.merge(
            patchRequest.getMask(),
            patchRequest.getEntity(),
            modifiedBuilder,
            mergeOptions
        );
        Entity modified = modifiedBuilder.build();
        System.out.print("Patch applied, then is " + field_to_check + " null in the patched entity?   ");
        System.out.println(!patch.getOthers().getOwner().hasId());


    //****************************************************************//
     //*** 2. Use wrapper for presence and oneof for nullability **//
    //****************************************************************//
        System.out.println("2. Use wrapper for presence and oneof for nullability");
        System.out.println("Creating a patch to set `id` to null and `others.owner.id` to 'CHANGED'");
        // 2.1 CLIENT side setting a field as not present
            // just don't set it
        // 2.2 CLIENT side setting a field as present
        // 2.3 CLIENT side setting a field as present and NULL
        EntityT patchT = EntityT
            .newBuilder()
            .setId(NullableString.newBuilder().setValue("CHANGED").build()) // set non null & present
            .setOthers(
                OthersT
                    .newBuilder()
                    .setOwner(
                        OwnerT
                        .newBuilder()
                            // setting `id` to present null (i.e., don't set the values)                         
                        .setId(NullableString.newBuilder().build())
                        .build()
                    )
                    .build()
            )
            .build();
        //-------------------------------------------------------------
        // 2.4 SERVER side checking a field is present
        // 2.5 Server side check field as null
        // 2.6 SERVER side get non-null
        // say you want to check if others.owner.id's presence/null_or_non-null/value
        System.out.print("In the patch, ");
        if (!patchT.getOthers().getOwner().hasId()) {
            System.out.println(field_to_check + " is not present");
        } else if (patchT.getOthers().getOwner().getId().getStringCase() 
                    == NullableString.StringCase.STRING_NOT_SET) {
            System.out.println(field_to_check + " is null");
        } else {
             System.out.println(field_to_check + " is " + patchT.getOthers().getOwner().getId().getValue());
        }

        // 2.7 SERVER side merging patch
        // NEED TO LOOP THRU NESTED LOOPS, **UGLY**
    }
}
