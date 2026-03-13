package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.chat.service.ChatMessagesService;
import io.ethertale.findadicethymeleaf.config.Utils;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.ChatRoomMessageDTO;
import io.ethertale.findadicethymeleaf.web.dto.WSIncomingMessageDTO;
import io.ethertale.findadicethymeleaf.web.dto.WSOutgoingMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebsocketController {

    private final ChatMessagesService chatMessagesService;
    private final UserService userService;
    private final Utils utils;

    @Autowired
    public ChatWebsocketController(ChatMessagesService chatMessagesService, UserService userService, Utils utils) {
        this.chatMessagesService = chatMessagesService;
        this.userService = userService;
        this.utils = utils;
    }

    @MessageMapping("/chat/{roomCode}")
    @SendTo("/topic/chat/{roomCode}")
    public WSOutgoingMessageDTO handleChatMessage(
            @DestinationVariable String roomCode,
            WSIncomingMessageDTO incomingMessage,
            Principal principal) {

        // Unwrapping the Principal because with WebSockets there are no attached cookies, thus
        // Spring does not store @AuthenticationPrincipal, but it stores it in java.security.Principal
        // At the time of the handshake.
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        AuthenticationDetails authenticationDetailsDetails = (AuthenticationDetails) authToken.getPrincipal();
        User loggedUser = userService.getUserById(authenticationDetailsDetails.getId());

        chatMessagesService.sendMessage(roomCode, loggedUser, new ChatRoomMessageDTO(incomingMessage.getMessage()));

        return new WSOutgoingMessageDTO(
                loggedUser.getHero().getId().toString(),
                loggedUser.getHero().getName(),
                loggedUser.getHero().getImageUrl(),
                incomingMessage.getMessage(),
                utils.formattedTimeNow()
        );
    }
}
