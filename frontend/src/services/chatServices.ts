//@ts-ignore
import SockJS from 'sockjs-client/dist/sockjs.min.js'
//@ts-ignore
import Stomp from 'stompjs'
import {IContact, IMessage, MockUser} from "@src/types";

let stompClient: any = null;
let subscription: any = null;

const connect = (onMessageReceived: any, theSender: IContact | undefined, chatroomID: string) => {
    const socket = new SockJS('http://localhost:8080/signal/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame: any) => {
        subscription = stompClient.subscribe('/topic/private/' + chatroomID, (message: any) => {
            onMessageReceived(JSON.parse(message.body));
        });
        stompClient.send('/private/' + chatroomID,
            {},
            JSON.stringify({
                id: -2,
                messageType: 'JOIN',
                content: 'user has just joined',
                sender: theSender,
                timestamp: new Date().toISOString(),
            }));
    });
};


const sendMessage = (message: IMessage) => {
    if (message && stompClient) {
        stompClient.send('/chat/private/' + message.roomName, {}, JSON.stringify(message));
    }
};


const disconnect = () => {
    if(subscription != null) {
        console.log("Unsubscribe");
        subscription.unsubscribe(); //Unsubscribe before disconnecting
    }
    if(stompClient != null) {
        stompClient.disconnect(() => {
            console.log("Disconnected");
        });
    }
};

export const chatServices = {
    connect,
    sendMessage,
    disconnect,
};