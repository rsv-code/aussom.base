# file: lang.aus

## class: exception
[346:14] (extern: com.aussom.types.AussomException) **extends: object** 
#### Methods
- **getLineNumber** ()

- **getExceptionType** ()

- **getId** ()

- **getText** ()

- **getDetails** ()

- **getStackTrace** ()

- **printStackTrace** ()

- **toString** ()




## class: date
[390:14] (extern: com.aussom.stdlib.ADate) **extends: object** 
#### Methods
- **date** (`int Mills = null`)

- **newDate** (`int Mills = null`)

- **getHours** ()

- **getMinutes** ()

- **getSeconds** ()

- **getTime** ()

- **setHours** (`int Hours`)

- **setMinutes** (`int Minutes`)

- **setSeconds** (`int Seconds`)

- **setTime** (`int TimeMills`)

- **toString** ()

- **parse** (`string DateString, string DateFormat`)

- **format** (`string DateFormat = "yyyy-MM-dd HH:mm:ss.SSS Z"`)

- **isEpoch** ()

- **_setHours** (`int Hours`)

- **_setMinutes** (`int Minutes`)

- **_setSeconds** (`int Seconds`)

- **_setTime** (`int TimeMills`)




## class: charset
[424:6] `static` (extern: com.aussom.types.AussomObject) **extends: object** 
#### Members
- **us_ascii**
- **iso_8859_1**
- **utf_8**
- **utf_16be**
- **utf_16le**
- **utf_16**



## class: string
[240:14] (extern: com.aussom.types.AussomString) **extends: object** 
#### Methods
- **charAt** (`int Index`)

- **compare** (`string Str`)

- **compareICase** (`string Str`)

- **concat** (`string Str`)

- **contains** (`string Needle`)

- **endsWith** (`string Suffix`)

- **equals** (`string Str`)

- **equalsICase** (`string Str`)

- **indexOf** (`string Needle`)

- **indexOfStart** (`string Needle, int StartIndex`)

- **isEmpty** ()

- **lastIndexOf** (`string Needle`)

- **lastIndexOfStart** (`string Needle, int StartIndex`)

- **length** ()

- **matches** (`string Regex`)

- **replace** (`string Find, string Replace`)

- **replaceFirstRegex** (`string Regex, string Replace`)

- **replaceRegex** (`string Regex, string Replace`)

- **split** (`string Delim, bool AllowBlanks = false`)

- **startsWith** (`string Prefix`)

- **substr** (`int Index, int EndIndex = null`)

- **toLower** ()

- **toUpper** ()

- **trim** ()

- **toJson** ()

- **pack** ()




## class: bool
[21:14] (extern: com.aussom.types.AussomBool) **extends: object** 
#### Methods
- **toInt** ()
	> Converts the bool value to int.
	- **@r** `An` int with 1 if true and 0 if false.
- **toDouble** ()
	> Converts the bool value to double.
	- **@r** `A` double with 1.0 if true and 0.0 if false.
- **toString** ()
	> Converts the boolean value to string.
	- **@r** `A` string with 'true' or 'false'.
- **compare** (`bool Val`)
	> Compares the value to the provided value.
	- **@r** `An` int value with the value 0 if this == Val, a value less than 0 if !this && Val, and a value greater than 0 if this && !Val.
- **parse** (`string Val`)
	> Parses the provided string value and sets the bool value.
	- **@p** `Val` is a string with 'true' or 'false'.
	- **@r** `This` object.
- **toJson** ()
	> Converts the value to a JSON encoded string.
	- **@r** `A` JSON encoded string.
- **pack** ()
	> Serializes the data into a structure.
	- **@r** `A` packed string.



## class: cnull
[337:14] (extern: com.aussom.types.AussomNull) **extends: object** 
#### Methods
- **toJson** ()

- **pack** ()




## class: Int
[187:21] `static` (extern: com.aussom.stdlib.SInt) **extends: object** 
#### Methods
- **maxVal** ()
- **minVal** ()
- **parse** (`string Str, int Radix = null`)



## class: secman
[561:21] `static` (extern: com.aussom.stdlib.ASecMan) **extends: object** 
#### Methods
- **getProp** (`string PropName`)

- **keySet** ()

- **getMap** ()

- **setProp** (`string PropName, Value`)

- **setMap** (`map PropsToSet`)




## class: Bool
[71:21] `static` (extern: com.aussom.stdlib.SBool) **extends: object** 
#### Methods
- **parse** (`string Val`)
	> Parses the provided string and returns the bool value.
	- **@p** `Val` is a string with the bool value.
	- **@r** `A` bool value.



## class: json
[552:21] `static` (extern: com.aussom.stdlib.AJson) **extends: object** 
#### Methods
- **parse** (`string JsonString`)

- **unpack** (`string JsonString`)




## class: buffer
[446:14] (extern: com.aussom.stdlib.ABuffer) **extends: object** 
#### Methods
- **buffer** (`int Size = 1024`)

- **newBuffer** (`int Size = 1024`)

- **size** ()

- **clear** ()

- **writeSeek** (`int Index`)

- **readSeek** (`int Index`)

- **addString** (`string Str, string Charset = "utf_8"`)

- **addByte** (`int Byte`)

- **addUByte** (`int Byte`)

- **addShort** (`int Short, string ByteOrder = "big"`)

- **addUShort** (`int Short, string ByteOrder = "big"`)

- **addInt** (`int Int, string ByteOrder = "big"`)

- **addUInt** (`int Int, string ByteOrder = "big"`)

- **addLong** (`int Long, string ByteOrder = "big"`)

- **addFloat** (`double Float, string ByteOrder = "big"`)

- **addDouble** (`double Double, string ByteOrder = "big"`)

- **getWriteCursor** ()

- **getReadCursor** ()

- **getString** (`string Charset = "utf_8"`)

- **getStringAt** (`int Length, int Index = -1, string Charset = "utf_8"`)

- **getByte** (`int Index = -1`)

- **getUByte** (`int Index = -1`)

- **getShort** (`int Index = -1, string ByteOrder = "big"`)

- **getUShort** (`int Inex = -1, string ByteOrder = "big"`)

- **getInt** (`int Index = -1, string ByteOrder = "big"`)

- **getUInt** (`int Index = -1, string ByteOrder = "big"`)

- **getLong** (`int Index = -1, string ByteOrder = "big"`)

- **getFloat** (`int Index = -1, string ByteOrder = "big"`)

- **getDouble** (`int Index = -1, string ByteOrder = "big"`)

- **setString** (`string Str, string Charset = "utf_8"`)

- **setStringAt** (`int Index, string Str, string Charset = "utf_8"`)

- **setByte** (`int Index, int Byte, string ByteOrder = "big"`)

- **setUByte** (`int Index, int Byte, string ByteOrder = "big"`)

- **setShort** (`int Index, int Short, string ByteOrder = "big"`)

- **setUShort** (`int Index, int Short, string ByteOrder = "big"`)

- **setInt** (`int Index, int Int, string ByteOrder = "big"`)

- **setUInt** (`int Index, int Int, string ByteOrder = "big"`)

- **setLong** (`int Index, int Long, string ByteOrder = "big"`)

- **setFloat** (`int Index, double Float, string ByteOrder = "big"`)

- **setDouble** (`int Index, double Float, string ByteOrder = "big"`)

- **copyFrom** (`int DestIndex, object Buffer, int SrcIndex = -1, int Length = -1`)

- **copyTo** (`int SrcIndex, object Buffer, int DestIndex = -1, int Length = -1`)

- **byteToBinary** (`int Index`)

- **shortToBinary** (`int Index, string ByteOrder = "big"`)

- **intToBinary** (`int Index, string ByteOrder = "big"`)

- **longToBinary** (`int Index, string ByteOrder = "big"`)

- **floatToBinary** (`int Index, string ByteOrder = "big"`)

- **doubleToBinary** (`int Index, string ByteOrder = "big"`)

- **_clear** ()

- **_writeSeek** (`int Index`)

- **_readSeek** (`int Index`)

- **_addString** (`string Str, string Charset = "utf_8"`)

- **_addByte** (`int Byte`)

- **_addUByte** (`int Byte`)

- **_addShort** (`int Short, string ByteOrder = "big"`)

- **_addUShort** (`int Short, string ByteOrder = "big"`)

- **_addInt** (`int Int, string ByteOrder = "big"`)

- **_addUInt** (`int Int, string ByteOrder = "big"`)

- **_addLong** (`int Long, string ByteOrder = "big"`)

- **_addFloat** (`double Float, string ByteOrder = "big"`)

- **_addDouble** (`double Double, string ByteOrder = "big"`)

- **_setByte** (`int Index, int Byte, string ByteOrder = "big"`)

- **_setUByte** (`int Index, int Byte, string ByteOrder = "big"`)

- **_setShort** (`int Index, int Short, string ByteOrder = "big"`)

- **_setUShort** (`int Index, int Short, string ByteOrder = "big"`)

- **_setInt** (`int Index, int Int, string ByteOrder = "big"`)

- **_setUInt** (`int Index, int Int, string ByteOrder = "big"`)

- **_setLong** (`int Index, int Long, string ByteOrder = "big"`)

- **_setFloat** (`int Index, double Float, string ByteOrder = "big"`)

- **_setDouble** (`int Index, double Float, string ByteOrder = "big"`)

- **_copyFrom** (`int DestIndex, object Buffer, int SrcIndex = -1, int Length = -1`)

- **_copyTo** (`int SrcIndex, object Buffer, int DestIndex = -1, int Length = -1`)




## class: lang
[543:21] `static` (extern: com.aussom.stdlib.ALang) **extends: object** 
#### Methods
- **type** (`DataType`)

- **getClassAussomdoc** (`string ClassName`)




## class: map
[301:14] (extern: com.aussom.types.AussomMap) **extends: object** 
#### Methods
- **clear** ()

- **containsKey** (`string Key`)

- **containsVal** (`Val`)

- **get** (`string Key`)

- **isEmpty** ()

- **keySet** ()

- **put** (`string Key, Val`)

- **putAll** (`map ToAdd`)

- **putIfAbsent** (`string Key, Val`)

- **remove** (`string Key`)

- **size** ()

- **values** ()

- **toJson** ()

- **pack** ()




## class: c
[368:21] `static` (extern: com.aussom.stdlib.console) **extends: object** 
#### Methods
- **log** (`Content`)

- **info** (`Content`)

- **warn** (`Content`)

- **err** (`Content`)

- **print** (`Content`)

- **println** (`Content`)

- **_log** (`Content`)

- **_info** (`Content`)

- **_warn** (`Content`)

- **_err** (`Content`)

- **_print** (`Content`)

- **_println** (`Content`)




## class: double
[207:14] (extern: com.aussom.types.AussomDouble) **extends: object** 
#### Methods
- **toInt** ()

- **toBool** ()

- **toString** ()

- **compare** (`double Val2`)

- **isInfinite** ()

- **isNan** ()

- **parse** (`string Val`)

- **toHex** ()

- **toJson** ()

- **pack** ()




## class: list
[272:14] (extern: com.aussom.types.AussomList) **extends: object** 
#### Methods
- **add** (`ItemToAdd`)

- **addAll** (`list ListToAdd`)

- **addAllAt** (`list ListToAdd, int Index`)

- **clear** ()

- **clone** ()

- **contains** (`Item`)

- **containsObjRef** (`Item`)

- **get** (`int Index`)

- **indexOf** (`Item`)

- **isEmpty** ()

- **remove** (`Item`)

- **removeAt** (`int Index`)

- **removeAll** (`list ListToRemove`)

- **retainAll** (`list ListToRetain`)

- **set** (`int Index, Item`)

- **size** ()

- **subList** (`int StartIndex, int EndIndex`)

- **sort** ()

- **sortAsc** ()

- **join** (`string Glue`)

- **sortCustom** (`callback OnCompare`)

- **toJson** ()

- **pack** ()




## class: Double
[224:21] `static` (extern: com.aussom.stdlib.SDouble) **extends: object** 
#### Methods
- **maxExp** ()

- **maxVal** ()

- **minExp** ()

- **minNormal** ()

- **minVal** ()

- **nanVal** ()

- **negInfinity** ()

- **posInfinity** ()

- **size** ()

- **parse** (`string Val`)




## class: int
[83:14] (extern: com.aussom.types.AussomInt) **extends: object** 
#### Methods
- **toDouble** ()
	> Converts the int to double.
	- **@r** `A` double with the value.
- **toBool** ()
	> Converts the int to a bool.
	- **@r** `A` bool with true if non-zero and false if zero.
- **toString** ()
	> Converts the int to it's string representation.
	- **@r** `A` string with the value.
- **compare** (`int Val`)
	> Compares the value to the provided value.
	- **@r** `An` int with the value 0 if this == Val. A value less than 0 if this < Val, and a value greater than 0 if this > Val.
- **numLeadingZeros** ()
	> Returns the number of zero bits preceding the highest-order one-bit in the two's complement binary representation of the value.
	- **@r** `An` integer with number of zeros.
- **numTrailingZeros** ()
	> Returns the number of zero bits following the lowest-order one-bit in the two's complement binary representation of the value.
	- **@r** `An` integer with the number of zeros.
- **reverse** ()
	> Returns the value by reversing the bits of the value.
	- **@r** `An` int with the reversed bit order.
- **reverseBytes** ()
	> Returns the value by reversing the bytes of the value.
	- **@r** `An` int with the reversed byte order.
- **rotateLeft** (`int Distance`)
	> Rotates the value the number of bits provided to the left.
	- **@r** `An` int with the rotated bits.
- **rotateRight** (`int Distance`)
	> Rotates the value the number of bits provided to the right.
	- **@r** `An` int with the rotated bits.
- **signum** ()
- **toBinary** ()
- **toHex** ()
- **toOctal** ()
- **parse** (`string Str, int Radix = null`)
- **toJson** ()
- **pack** ()



## class: securitymanager
[574:21] `static` (extern: com.aussom.stdlib.ASecurityManager) **extends: object** 
#### Methods
- **getProp** (`string PropName`)

- **keySet** ()

- **getMap** ()

- **setProp** (`string PropName, Value`)

- **setMap** (`map PropsToSet`)




## class: callback
[329:14] (extern: com.aussom.types.AussomCallback) **extends: object** 
#### Methods
- **call** (`...`)

- **_call** (`list args`)




## class: byteOrder
[437:6] `static` (extern: com.aussom.types.AussomObject) **extends: object** 
#### Members
- **big**
- **little**



## class: object
[321:14] (extern: com.aussom.types.AussomObject) 
#### Methods
- **toJson** ()

- **pack** ()




