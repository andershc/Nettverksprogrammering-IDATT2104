const net = require('net');
const crypto = require('crypto');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    WebSocket test page
    
    <input type="text" id="inputField">
    <input type="submit" id="submit">
    
    <div id="chatBox"></div>
    <script>
      let ws = new WebSocket('ws://localhost:3001');
      let submitBtn = document.getElementById("submit")
      const inputField = document.getElementById("inputField")
      let chatBox = document.getElementById("chatBox")
      
      submitBtn.addEventListener("click", function (){
          ws.send(inputField.value)
      })
      
      ws.onmessage = event => chatBox.innerHTML += event.data + "</br>";
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});

let clients = [];
let userName;
const wsServer = net.createServer((connection) => {
    console.log("Client connected");

    connection.on("data", (data) => {
        let key;
        if ((key = getKey(data)) != null) {
            let acceptString = getAcceptString(key)

            connection.write("HTTP/1.1 101 Switching Protocols\r\n");
            connection.write("Upgrade: websocket\r\n");
            connection.write("Connection: Upgrade\r\n");
            connection.write("Sec-WebSocket-Accept: " + acceptString + "\r\n\r\n");
            clients.push(connection);
            userName = "User" + clients.length
            console.log(userName)
            console.log("websocket connected");
        } else {
            let msg = decodeMessage(data);
            console.log("Data recieved from client: " + msg);

            let response = createBufferFromMessage(msg);
            sendToClients(response, connection);
            console.log(response);
        }
    });

    connection.on('end', () => {
        console.log('Client disconnected');
    });
});
wsServer.on('error', (error) => {
    console.error('Error: ', error);
});
wsServer.listen(3001, () => {
    console.log('WebSocket server listening on port 3001');
});

function getAcceptString(key){
    return base64encode(sha1(key))
}
function sha1(key){
    const rfc6455 = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    return crypto.createHash("sha1").update(key + rfc6455, "binary")
}
function base64encode(hashedKey){
    return hashedKey.digest("base64")
}
function decodeMessage(data) {
    let bytes = Buffer.from(data);

    //bytes[0] is the type of message
    let length = bytes[1] & 127; //bytes[1] is the length of the message
    let maskStart = 2; //next four bytes are the mask starting at index 2
    let dataStart = maskStart + 4;
    let msg = "";
    for (let i = dataStart; i < dataStart + length; i++) {
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)];
        msg += String.fromCharCode(byte);
    }
    return msg;
}
function getKey(data){
    const array = data.toString().split("\n")
    for(let i = 0; i < array.length; i++){
        if(array[i].includes("Sec-WebSocket-Key")){
            return array[i].split(" ")[1].trim()
        }
    }
}
function createBufferFromMessage(message) {
    let sub1 = Buffer.from([0x81, message.length]);
    let sub2 = Buffer.from(message, "utf-8");
    return Buffer.concat([sub1, sub2]);
}

function sendToClients(buffer, connection) {
    for (let i = 0; i < clients.length; i++)
        if (connection != clients[i]) clients[i].write(buffer); //Send the message to all other clients, not the current connection.
}