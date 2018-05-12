Prediction Transport Protocol
===========================================

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

Serialization
------------------

### Protocol Buffers
[Protocol Buffers](https://developers.google.com/protocol-buffers/)

There is a good comparison of the different popular choices here:
[Protocol Buffers vs Json vs XML](http://stackoverflow.com/questions/14028293/google-protocol-buffers-vs-json-vs-xml)

A couple of reasons why we may want to choose protocol buffers over XML or JSON for data serialization:

 - It's a lot more efficient than both XML and JSON (dense data)
 - Fast processing
 - A formal definition of the protocol is inherent in its use.
 (We have to formally specify the protocol in the process of using protobuf,
 so no extra documention really required)
 - Builtin support for forwards/backwards compatibility

A couple of reasons why we may not want to choose protocol buffers:

 - It's slightly more complicated, an extra step required to set it up.
 - When used in binary format, it's harder to debug using tools like wireshark, although there is a wireshark plugin which can help



Lower Level Transport
---------------------

TCP Sockets


Higher Level Transport
-------------------------

In order to handle connection loss, and the passing of messages to multiple clients easily, a higher level transport protocol such
as message queue is the industry norm.

https://en.wikipedia.org/wiki/Message_queue


### AMQP

AMQP has large scale industry support, and it's a fairly recent and well supported endevour.

[Advanced Message Queuing Protocol](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol)

Some implementations:

[QPID Proton](https://github.com/apache/qpid-proton)

[RabbitMQ](https://www.rabbitmq.com/)


Optimising Message Data
---------------------------

### Requests for information
I think AMQP might support something where all messages are passed to an amqp broker server, and then clients can select which messages they want to receive.
https://www.rabbitmq.com/tutorials/tutorial-three-java.html


### Prediction Curves
I've been thinking about how we can most efficiently transport the predictions. If it turns out that sending the predictions as points, is inefficient for
2000 aircraft, and we are required to do that, then a data reduction solution might be in order.

If the output of the prediction algorithm is a simple curvilinear curve, then, instead of reducing this to points, the curve specifications could be sent instead,
for long curves this would certainly result in a smaller transmition and an infinitely better resolution, but this does require more code on the client
to be written to generate points from the curve.

If the output of the prediction algorithm is a more complicated curve, or a series of points, then we could use something like taylor series (https://en.wikipedia.org/wiki/Taylor_series)
expansion to generate a spline or a bezier which is then sent to the client. This would have much better resolution, especially for longer predictions, but
again would require more complicated client code.

An example here on how to do this computing interpolating bezier curves or splines from points.

 - <http://www.antigrain.com/research/bezier_interpolation/>
 - <http://www.benknowscode.com/2012/09/path-interpolation-using-cubic-bezier_9742.html>
 - <https://en.wikipedia.org/wiki/Spline_interpolation>
 - [Curve Fitting: Spline Interpolation(VIDEO)](https://www.youtube.com/watch?v=dxvmafuP9Wk)
