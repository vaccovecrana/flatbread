`flatbread` builds POJOs from flat string lists. That's it.

- Mark field names as `FdMask` to mask secret data.
- Cyclic object graphs are not supported.
- All nested objects (including the root object itself) must support empty constructors.
- Primitive values get hydrated with the wrapper type's `valueOf` method.

See test cases for examples.
