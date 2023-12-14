# gRPC Messaging Application with Spring Boot

The Spring Boot gRPC Messaging Sample is a hands-on showcase of integrating gRPC communication into Spring Boot
applications. It demonstrates the seamless interoperability between server and client, leveraging the power of gRPC to
streamline communication.

The repository provides a comprehensive guide on setting up gRPC services using Spring Boot. It covers everything from
defining structures in the proto file to implementing synchronous, asynchronous, and bidirectional
communication methods.

## Key Features

1. **gRPC Integration:** Demonstrates seamless integration of gRPC communication within Spring Boot applications,
   highlighting its efficiency and ease of use.

2. **Proto File Definitions:** Utilizes the [`messaging.proto`](grpc-interface/src/main/proto/messaging.proto) file to
   define message structures, including requests, responses, and service definitions, ensuring a standardized
   communication protocol.

3. **Various Service Types:** Explores and implements different gRPC service types, such as synchronous unary calls,
   client-streaming, and bidirectional streaming methods, showcasing their diverse functionalities.

4. **Practical Start-Up Guide:** Provides a straightforward process to start the application, enabling developers to
   quickly set up and run the gRPC-powered Spring Boot project.

5. **Interoperability Illustration:** Illustrates how gRPC services, while efficient, might face limitations when
   accessed directly from browsers and suggests a simple solutions through
   the `ProtobufJsonFormatHttpMessageConverter` bean
   in [`ProtobufJsonConverterConfig.java`](server-service/src/main/java/dev/federicopellegatta/serverservice/config/ProtobufJsonConverterConfig.java)
   which encode and decode protobuf data into a more accessible JSON format.

## Getting Started

### Prerequisites

- Java 11
- Maven 3.6.3
- [gRPCurl](https://aristanetworks.github.io/openmgmt/examples/gnoi/grpcurl/) or a gRPC client (such
  as [Postman](https://www.postman.com/) or [BloomRPC](https://appimage.github.io/BloomRPC/))

### Running the Application

1. Clone the repository
2. Navigate to the root directory
3. Run `mvn clean install`
4. Start the server-service with
   ```shell
   java -jar server-service/target/server-service-0.0.1-SNAPSHOT.jar
   ``` 
   By default, the
   server-service will start
    - a gRPC server on port 9090;
    - a server for API request on port 9091.
5. Start the client-service with
   ```shell
   java -jar client-service/target/client-service-0.0.1-SNAPSHOT.jar
   ```
   By default, the client-service will run on port 8080.