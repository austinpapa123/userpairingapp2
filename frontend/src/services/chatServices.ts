//@ts-ignore
import SockJS from 'sockjs-client/dist/sockjs.min.js'
//@ts-ignore
import Stomp from 'stompjs'

let stompClient: any = null;
let subscription: any = null;

const connect = (onMessageReceived: any, senderId: number | undefined, avatarUrl1: string | undefined, chatroomID: string) => {
    console.log("connected");
    const socket = new SockJS('http://localhost:8080/signal/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame: any) => {
        subscription = stompClient.subscribe('/topic/private/' + chatroomID, (message: any) => {
            onMessageReceived(JSON.parse(message.body));
        });
        stompClient.send('/private/' + chatroomID,
            {},
            JSON.stringify({senderId: senderId, avatarUrl: avatarUrl1, messageType: 'JOIN', content: 'user has just joined'}));
    });
};

const sendMessage = (message: string | undefined, senderId: number, avatarUrl: string, chatroomID: string) => {
    if (message && stompClient) {
        const chatMessage = {
            content: message,
            messageType: "CHAT",
            senderId: senderId,
            avatarUrl: avatarUrl,
        };
        stompClient.send('/chat/private/' + chatroomID, {}, JSON.stringify(chatMessage));
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