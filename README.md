# client-server-feature-examples
 Client & Server - usecases like chat.
 
 The first step has been taken in this project.
 More features can be added later.
 The goal is to learn more by adding functionality like maybe a file server,
 broadcast audio, fetch information, use resources etc.
 
 Frequent refactoring and cleaning up before adding more code. 
 ________
 
 After first commit:
 
 Clients connects to a local server. The server creates one thread per client
 and broadcasts messages from clients to all connected clients.
 
 Json is used when sending data, according to a protocol.
 
 Some techniques and APIn used:
 TCP. Socket, ServerSocket, DataIn and OutPutStreams, 
 Multithreaded, synchronization when using many threads, 
 InputStreamReader, BufferedReader 
