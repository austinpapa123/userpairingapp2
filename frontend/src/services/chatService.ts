import { io } from "socket.io-client";

const socket = io("http://localhost:8080");

export function socketSendMessage(message: {room_name: string | undefined, sender_name: string, content: string | undefined}){
    console.log('Sending message:', message);
    socket.emit('send', message);
}

export function socketJoinGobalUpdates() {
    console.log('joining global updates');
    socket.emit('join_global_updates');
}

export function socketJoinChannel(data: {name: string, avatar: string}) {
    console.log("emitting public channel created signal");
    socket.emit('join_channel', data);
}

export function socketJoinPrivate(data: {sender_name: string | undefined, receiver_name: string, avatar: string}) {
    console.log("emitting private chat created signal");
    socket.emit('join_private', data);
}

export function socketJoinConversation(data: {room_name: string | undefined}) {
    console.log("emitting join conversation ...");
    socket.emit('join', data);
}

export default socket;