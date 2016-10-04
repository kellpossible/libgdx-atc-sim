import DebugDataFeedServe_pb2 as NetworkInterfacePacket
import socket
import sys
import time
import math
from google.protobuf.internal import encoder
from google.protobuf.internal import decoder


def build_test_packet(dt):
    t = int(round(time.time() * 1000))
    system_state = NetworkInterfacePacket.SystemStateMessage()
    system_state.time = t
    # assert not system_state.HasField("time")

    aircraft_state = system_state.aircraftState.add()
    aircraft_state.aircraftID = "7C1468"
    aircraft_state.time = t
    aircraft_state.heading = 81.0

    speed = 0.01
    deg_to_rad = 0.0174533

    position = aircraft_state.position
    position.altitude = 2278.38
    position.latitude = math.radians(-37.7549 + speed*(dt/1000.0))
    position.longitude = math.radians(144.6835)

    velocity = aircraft_state.velocity
    velocity.dr = 0.0
    velocity.dtheta = 0.0
    velocity.dphi = math.radians(speed)

    return system_state

# I had to implement this because the tools in google.protobuf.internal.decoder
# read from a buffer, not from a file-like objcet


def readRawVarint32(stream):
    mask = 0x80  # (1 << 7)
    raw_varint32 = []
    while 1:
        b = stream.read(1)
        # eof
        if b == "":
            break
        raw_varint32.append(b)
        if not (ord(b) & mask):
            # we found a byte starting with a 0, which means it's the last byte
            # of this varint
            break
    return raw_varint32


# These methods are from here: http://stackoverflow.com/questions/2340730/are-t
# here-c-equivalents-for-the-protocol-buffers-delimited-i-o-functions-in-ja/3453
# 9706#34539706


def writeDelimitedTo(message, connection):
    message_str = message.SerializeToString()
    delimiter = encoder._VarintBytes(len(message_str))
    connection.send(delimiter + message_str)


def readDelimitedFrom(MessageType, stream):
    raw_varint32 = readRawVarint32(stream)
    message = None

    if raw_varint32:
        size, _ = decoder._DecodeVarint32(raw_varint32, 0)

        data = stream.read(size)
        if len(data) < size:
            raise Exception("Unexpected end of file")

        message = MessageType()
        message.ParseFromString(data)

    return message

# In place version that takes an already built protobuf object
# In my tests, this is around 20% faster than the other version
# of readDelimitedFrom()


def readDelimitedFrom_inplace(message, stream):
    raw_varint32 = readRawVarint32(stream)

    if raw_varint32:
        size, _ = decoder._DecodeVarint32(raw_varint32, 0)

        data = stream.read(size)
        if len(data) < size:
            raise Exception("Unexpected end of file")

        message.ParseFromString(data)

        return message
    else:
        return None


def start_server(address, port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("starting server on {} port {}".format(address, port))
    sock.bind((address, port))

    # listen for incoming connections
    sock.listen(1)


    while True:


        # wait for a connection
        connection, client_address = sock.accept()
        print("connection initiated from {}", client_address)

        start_time = int(round(time.time() * 1000))

        while True:
            current_time = int(round(time.time() * 1000))
            dt = current_time - start_time
            message = build_test_packet(dt)
            print(dt)

            writeDelimitedTo(message, connection)

            time.sleep(2)


if __name__ == "__main__":
    # print(build_test_packet(0))
    start_server('localhost', 6989)
