This repository contains a collection of Java programs demonstrating the use of
some of the more obscure features of Protocol Buffer 3.

## structure

Proto definitions can be found in `src/main/proto`.

## how to use

To compile, execute `gradle installDist` from project root.

The following demos are available:

- FieldMaskDemo: run `build/install/protobuf-examples/bin/fieldmask` to execute
- OneofDemo: run `build/install/protobuf-examples/bin/oneof` to execute
- NullabilityAndPresenseDemo: run `build/install/protobuf-examples/bin/nullability-and-presense` to execute
- JsonToProtobuf: run `build/install/protobuf-examples/bin/json-to-protobuf` to execute