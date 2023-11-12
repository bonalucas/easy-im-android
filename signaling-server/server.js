const nodeStatic = require('node-static');
const http = require('http');
const socketIO = require('socket.io');

const file = new(nodeStatic.Server)();
const app = http.createServer(function(req, res) {
  file.serve(req, res);
}).listen(9075, '0.0.0.0');
var io = socketIO(app);

io.sockets.on('connection', function(socket) {

  // convenience function to log server messages on the client
  function log() {
    var array = ['Message from server:'];
    array.push.apply(array, arguments);
    socket.emit('log', array);
    
    console.log('EasyIM', array);
  }

  socket.on('message', function(message) {
    // for a real app, would be room-only (not broadcast)
    // socket.broadcast.emit('message', message);

    var to = message['to'];
    log('from:' + socket.id + " to:" + to, message);
    io.sockets.sockets[to].emit('message', message);
  });

  socket.on('create or join', function(room) {
    log('Received request to create or join room ' + room);

    var clientsInRoom = io.sockets.adapter.rooms[room];
    var numClients = clientsInRoom ? Object.keys(clientsInRoom.sockets).length : 0;
    log('Room ' + room + ' now has ' + numClients + ' client(s)');

    if (numClients === 0) {
      socket.join(room);
      log('Client ID ' + socket.id + ' created room ' + room);
      socket.emit('created', room, socket.id);
    } else {
      log('Client ID ' + socket.id + ' joined room ' + room);
      io.sockets.in(room).emit('join', room, socket.id);
      socket.join(room);
      socket.emit('joined', room, socket.id);
      io.sockets.in(room).emit('ready');
    }
  });

  socket.on('ipaddr', function() {
    var ifaces = os.networkInterfaces();
    for (var dev in ifaces) {
      ifaces[dev].forEach(function(details) {
        if (details.family === 'IPv4' && details.address !== '127.0.0.1') {
          socket.emit('ipaddr', details.address);
        }
      });
    }
  });

  socket.on('bye', function(){
    console.log('received bye');
  });

});