import type {
  IContact,
  IConversation,
  IMessage, MockUser,
} from "@src/types";
import {useStore, useMessageStore} from "@src/store/store";

/**
 * return the username, modified from firstName+lastName
 * @param contact
 * @returns username
 */
export const getFullName = (contact: IContact) => {
  return contact.username;
};

/**
 * get avatar
 * @param conversation
 * @returns A string representing the url to the avatar image
 */
export const getAvatar = (conversation: IConversation) => {
  return conversation?.avatar;
};

/**
 * get name
 * @param conversation
 * @returns String
 */
export const getName = (conversation: IConversation) => {
  return conversation?.displayName;
};

/**
 * trim a string when it reaches a certain length and adds three dots
 * at the end.
 * @param text
 * @param maxLength
 * @returns A string that is trimmed according the length provided
 */
export const shorten = (message: IMessage | string, maxLength: number = 23) => {
  let text: string | undefined;

  if (typeof message === "string") {
    text = message;
  } else {
    text = message?.content;
  }

  if (text && typeof text === "string") {
    let trimmedString = text;
    if (text.length > maxLength) {
      // trim the string to the maximum length.
      trimmedString = trimmedString.slice(0, maxLength);
      // add three dots to indicate that there is more to the message.
      trimmedString += "...";
    }
    return trimmedString;
  }

  return "";
};

/**
 * test if the message contains attachments
 * @param message
 * @returns A boolean indicating whether the message has attachments
 */
export const hasAttachments = (message: IMessage) => {
  return false;
};

/**
 * get index of the conversation inside the conversations array
 * @param conversationId
 * @returns A number indicating the index of the conversation.
 */
export const getConversationIndex = (
  conversationId: number
): number | undefined => {
  let conversationIndex;
  const store = useMessageStore();

  store.conversations.forEach((conversation, index) => {
    if (conversation.id === conversationId) {
      conversationIndex = index;
    }
  });

  return conversationIndex;
};

/**
 * Convert unicode to native emoji
 *
 * @param unicode - emoji unicode
 */
export const unicodeToEmoji = (unicode: string) => {
  return unicode
    .split("-")
    .map((hex) => parseInt(hex, 16))
    .map((hex) => String.fromCodePoint(hex))
    .join("");
};


/**
 * Helper function to convert IContact(frontend) to MockUser(Backend)
 * @param contact
 */
export const convertToMockUser = (contact: IContact): MockUser => ({
  id: contact.id,
  username: contact.username,
  avatar: contact.avatar,
  role: contact.role,
});


/**
 * Helper function to convert MockUser(Backend) to IContact(frontend)
 * @param mockUser
 */
export const convertToIContact = (mockUser: MockUser): IContact => ({
  id: mockUser.id,
  username: mockUser.username,
  avatar: mockUser.avatar,
  role: mockUser.role,
});


