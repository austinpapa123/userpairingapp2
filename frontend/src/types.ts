export interface IContact {
  id: number;
  username: string;
  avatar: string;
  status: string;   //online or offline
  role: string; //admin, staff, or student
}

export interface IMessage {
  room_name: string,
  content?: string;
  sender: IContact;
}

export interface IConversation {
  id: number;
  type: string;
  name?: string;  //group chat could have a custom name
  displayName?: string;
  avatar?: string;  //group chat or public channel could have avatar
  admins?: number[];  //could be group chat
  contacts?: IContact[]; //could be group chat
  messages: IMessage[];
}

export interface IThread {
  id: number;
  author: IContact;
  title: string;
  content: string;
  //comments?: IComment[];
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