# Protocol Buffers

_Section author: Chris Coleman_

**More information from https://developers.google.com/protocol-buffers/**


## Defining One

For any data that is being transmitted between modules (DataFeed, Prediction and Display), we have decided to use ProtocolBuffers as the datatype.
Protocol Buffers can be thought of as a class that contains user-defined variables, but automates the creation of gets/sets, handles traversing through arrays and can be easily expanded for more functionality/data.

The 'class definition' is stored in a .proto file (/core/src/main/proto/ in our repo) and look like the following:
```
	syntax = "proto2"; //The language spec you want...look it up yourself

	option java_package = "some.address.to.package";
	option java_outer_classname = "className";

	message protocolBuffersName //The datatype's actual name
	{
 		required String name = 1;
		repeated double someArray = 2;
		optional int notReallyNeeded = 3
	}
```
From this example, you have the following:
- The class will be compiled into path/package "some.address.to.package"
- The class will be in a class called "className"
- the actual variable datatype is called protocolBuffersName
- and it contains
	- a String: name, this must be present whenever a protocolBuffersName is built (required)
	- a double[]: someArray, this is an array of doubles (repeated)
	- an int: notReallyNeeded, this can be present or not.. no one really cares (optional)



I'll skip over the compiling for now, they have already been compiled so will focus on actual implementation


## Building One

In standard code you
```
	int number = 3;
	someClass temp = new someClass();
```
A protocol buffer must be built. You do this through a protocolBuffersName.Builder, a side class that is able to accept all the Buffer's data information and returns a Protocol Buffer:
```
	protocolBuffersName.Builder tempBuilder = new protocolBuffersName.Builder();
	tempBuilder.addname("myName");
	tempBuilder.addsomeArray(10);
	tempBuilder.addsomeArray(100);
	tempBuilder.addnotReallyNeeded(1);
```
Calling this builder's .build() functionality you will be returned a ready to go Protocol Buffer!
```
	protocolBuffersName newProtoBuf = tempBuilder.build();
```

From there, you have a brand spanking new Protocol Buffer of you very own. Treat it like a normal class using the getName(), getsomeArray(1), etc methods or share it with your fiends!
