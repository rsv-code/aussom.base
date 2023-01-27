# file: reflect.aus

## class: reflect
[20:21] `static` (extern: com.aussom.stdlib.AReflect) **extends: object** 
#### Methods
- **evalStr** (`string CodeStr, string Name = "evalStr"`)

- **evalFile** (`string FileName`)

- **includeModule** (`string ModuleName`)

- **loadedModules** ()

- **loadedClasses** ()

- **isModuleLoaded** (`string ModuleName`)

- **classExists** (`string ClassName`)

- **getClassDef** (`string ClassName`)

- **instantiate** (`string ClassName`)

- **invoke** (`object Object, string MethodName, ...`)

- **_evalStr** (`string CodeStr, string Name = "evalStr"`)

- **_evalFile** (`string FileName`)

- **_includeModule** (`string ModuleName`)




## class: rclass
[41:14] (extern: com.aussom.stdlib.AClass) **extends: object** 
#### Methods
- **getName** ()

- **isStatic** ()

- **isExtern** ()

- **getExternClassName** ()

- **getMembers** ()

- **getMethods** ()




