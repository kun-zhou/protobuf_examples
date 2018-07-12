package me.kunzhou.protobuf_examples;

import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.FieldMaskUtil;
import com.google.protobuf.FieldMask;

import me.kunzhou.protobuf_examples.field_mask_messages.*;

// protobuf objects namespaced in the same package
// *.proto can be found in {project_root}/src/main/proto/

public class FieldMaskDemo {

    /* 
     ** SOME USEFUL LINKS **
     *
     * https://github.com/google/protobuf/blob/master/src/google/protobuf/field_mask.proto
     * https://developers.google.com/protocol-buffers/docs/reference/java/com/google/protobuf/util/FieldMaskUtil
     * https://developers.google.com/protocol-buffers/docs/reference/java/com/google/protobuf/FieldMask
     */

    public static void main(String[] args) {

        //*** 1. playaround with a FieldMask ***//
        System.out.println(">>> 1. FieldMask Playground");
        FieldMask fieldMask = FieldMask
            .newBuilder()
            .addPaths("id") // each call to .addPath add one Path
            .addPaths("detail.age")
            .build();
        System.out.println(fieldMask.getPaths(0));
        System.out.println(fieldMask.getPaths(1));

        //*** 2. Create an artificial patch request with FieldMask ***//
        System.out.println(">>> 2. Create an artificial patch request with FieldMask");
        PatchRequest request = PatchRequest
            .newBuilder()
            .setEntity(
                Entity
                    .newBuilder()
                    .setId("XYZ")
                    .setDetail(
                        Detail
                            .newBuilder()
                            .setAge(100)
                            .build()
                    )
                    .build()
            )
            .setMask(fieldMask)
            .build();

        //*** 3. Merge the patch to the original entity ***//
        System.out.println(">>> 3. Merge the patch");
        // first, estabilish the original entity. (obviously, you would create 
        // the patch after estiabilishing original in practice)
        Entity original = Entity
            .newBuilder()
            .setId("ABC")
            .setDetail(
                Detail
                    .newBuilder()
                    .setAge(10)
                    .setDob("960620")
                    .setAddress("350 Convention Way, Redwood City, CA")
                    .setCountry("USA")
                    .build()
            )
            .build();

        // second, we merge the patch from request to original by using
        // FieldMaskUtil.merge(FieldMask mask, Message source, Message.Builder destination)
        // `source` -> patch, and `destination` -> `builder with data from original`

        Entity.Builder modifiedEntityBuilder = original.toBuilder();
        FieldMaskUtil.merge(
            request.getMask(),
            request.getEntity(), 
            modifiedEntityBuilder
        );
        Entity modified = modifiedEntityBuilder.build();

        try {
            System.out.println("> Patch");
            System.out.println(JsonFormat.printer().print(request));
            System.out.println("> Original Entity Object");
            System.out.println(JsonFormat.printer().print(original));
            System.out.println("> Modified Entity Object");
            System.out.println(JsonFormat.printer().print(modified));
        } catch (Exception e) {
            System.err.println("Something went wrong :(");
        }
    }
}