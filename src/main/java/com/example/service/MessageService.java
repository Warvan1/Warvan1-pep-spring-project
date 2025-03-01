package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) throws ClientErrorException{
        if(message.getMessageText().isEmpty() || message.getMessageText().length() > 255){
            throw new ClientErrorException("message text must be between 1 and 255 characters");
        }
        if(accountRepository.findById(message.getPostedBy()).isEmpty()){
            throw new ClientErrorException("postedBy does not refer to a real existing user");
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        return messageRepository.findById(id).orElse(null);
    }

    public int deleteMessageById(int id){
        if(messageRepository.findById(id).isEmpty()){
            return 0;
        }
        messageRepository.deleteById(id);
        return 1;
    }

    public void updateMessageById(int id, Message message) throws ClientErrorException{
        if(message.getMessageText().isEmpty() || message.getMessageText().length() > 255){
            throw new ClientErrorException("message text must be between 1 and 255 characters");
        }
        Message oldMessage = messageRepository.findById(id)
            .orElseThrow(() -> new ClientErrorException("messageId does not exist"));
        oldMessage.setMessageText(message.getMessageText());
        messageRepository.save(oldMessage);
    }

    public List<Message> getMessagesByPostedBy(int postedBy){
        return messageRepository.findByPostedBy(postedBy);
    }
}
