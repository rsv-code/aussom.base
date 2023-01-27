# file: cunit.aus

## class: test
[22:7] **extends: object** 
#### Members
- **tests**
- **passed**
- **failed**
- **name**
- **silent**

#### Methods
- **test** ()
	> Public default constructor.
- **setName** (`string Name = ""`)
	> Sets the name of the unit test suite.
	- **@p** `Name` is a string with the test name to set.
	- **@r** `This` object.
- **getName** ()
	> Gets the name of the unit test suite.
	- **@r** `A` string with the test name.
- **setSilent** (`bool Silent`)
	> Sets the silent flag.
	- **@p** `Silent` is a bool with true for silent and false for not.
	- **@r** `This` object.
- **getSlient** ()
	> Gets the slient flag.
	- **@r** `A` bool with true for silent and false for not.
- **add** (`string TestName, callback ToCall, bool DieOnFail = false`)
	> Adds a test function to the test suite.
	- **@p** `TestName` is a string with the test name.
	- **@p** `ToCall` is a callback with the function to call for the test.
	- **@p** `DieOnFail` is a boolean that when set to true will make the unit test stop on failure. Default is false.
	- **@r** `This` object.
- **run** ()
	> Runs the test suite.
	- **@r** `This` object.
- **getResults** ()
	> Produces a map with the test results including name, total, number ran, number passed, and number failed.
	- **@r** `A` map with the results.
- **expect** (`Item, ToBe`)
	> Expect helper function compares two items for equality.
	- **@p** `Item` is the first item to compare.
	- **@p** `ToBe` is the second item to compare.
	- **@r** `A` bool with true if equal and false for not.
- **expectNotNull** (`Item`)
	> Expect helper function expects value to not be null.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if not null and false for not.
- **expectNull** (`Item`)
	> Expect helper function expects value to be null.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if null and false if not.
- **expectString** (`Item`)
	> Expect helper function expects the provided item to be a string.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a string and false if not.
- **expectBool** (`Item`)
	> Expect helper function expects the provided item to be a bool.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a bool and false if not.
- **expectInt** (`Item`)
	> Expect helper function expects the provided item to be an int.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a int and false if not.
- **expectDouble** (`Item`)
	> Expect helper function expects the provided item to be a double.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a double and false if not.
- **expectNumber** (`Item`)
	> Expect helper function expects the provided item to be a type of number.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a type of number and false if not.
- **expectList** (`Item`)
	> Expect helper function expects the provided item to be a list.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a list and false if not.
- **expectMap** (`Item`)
	> Expect helper function expects the provided item to be a map.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a map and false if not.
- **expectObject** (`Item, string ClassName`)
	> Expect helper function expects the provided item to be an object.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is an object and false if not.
- **expectCallback** (`Item`)
	> Expect helper function expects the provided item to be a callback.
	- **@p** `Item` is the item to check.
	- **@r** `A` bool with true if the item is a callback and false if not.
- **printResult** ()
	> Prints the test results to standard output.
	- **@r** `This` object.



