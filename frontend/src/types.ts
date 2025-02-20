export interface IContact {
  id: number;
  username: string;
  avatar: string;
  role: string; //admin, regular
}

export interface MockUser { //backend user for now
  id: number;
  username: string;
  avatar: string;
  role: string;
}

export interface IMessage {
  id: number;
  roomName: string;
  messageType: string;
  content?: string;
  sender: IContact;
  timestamp?: any;
}


export interface IConversation {
  id: number;
  type: string;
  roomName?: string;  //group chat could have a custom name
  displayName?: string;
  avatar?: string;  //group chat or public channel could have avatar
  admins?: number[];  //could be group chat
  contacts?: IContact[]; //could be group chat
  messages: IMessage[];
}

export interface IThread {
  id: number;
  category: string;
}

//export interface IComment {}

export interface IContactGroup {
  letter: string; //for sorting purpose
  contacts: IContact[];
}

export interface IEmoji {
  n: string[];
  u: string;
  r?: string;
  v?: string[];
}