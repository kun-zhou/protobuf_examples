This repository contains a collection of Java programs demonstrating the use of
some of the more obscure features of Protocol Buffer 3.

## structure

Proto definitions can be found in `src/main/proto`.

## how to use

To compile, execute `gradle installDist` from project root.

The following demos are available:

- FieldMaskDemo: run `build/install/protobuf_examples/bin/fieldmask-demo` to execute
- OneofDemo: run `build/install/protobuf_examples/bin/oneof-demo` to execute
- NullabilityAndPresenseDemo: run `build/install/protobuf_examples/bin/nullability-and-presense-demo` to execute
- JsonToProtobuf: run `build/install/protobuf_examples/bin/json-to-protobuf` to execute