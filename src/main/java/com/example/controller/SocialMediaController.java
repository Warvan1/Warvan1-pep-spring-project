package com.example.controller;

import java.util.List;

import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.exception.ConflictException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) throws ClientErrorException, ConflictException{
        accountService.register(account);
        return ResponseEntity.ok().body(account);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) throws AuthenticationException{
        return ResponseEntity.ok().body(accountService.login(account));
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws ClientErrorException{
        return ResponseEntity.ok().body(messageService.createMessage(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok().body(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        return ResponseEntity.ok().body(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        int result = messageService.deleteMessageById(messageId);
        if(result == 0){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody Message message) throws ClientErrorException{
        messageService.updateMessageById(messageId, message);
        return ResponseEntity.ok().body(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByPostedBy(@PathVariable int accountId){
        return ResponseEntity.ok().body(messageService.getMessagesByPostedBy(accountId));
    }

    @ExceptionHandler(ClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String clientException(ClientErrorException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String conflictException(ConflictException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authenticationException(AuthenticationException ex){
        return ex.getMessage();
    }
}
