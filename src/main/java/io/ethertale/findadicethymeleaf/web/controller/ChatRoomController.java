package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.service.ChatMessagesService;
import io.ethertale.findadicethymeleaf.chat.service.ChatRoomService;
import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.ChatRoomMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessagesService chatMessagesService;
    private final HeroService heroService;
    private final UserService userService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, ChatMessagesService chatMessagesService, HeroService heroService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.chatMessagesService = chatMessagesService;
        this.heroService = heroService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView chatRoomsAll(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView("chatRooms");
        modelAndView.addObject("chats", chatRoomService.getAllChatRoomsSortedByLastActivity(loggedUser));

        return modelAndView;
    }

    @GetMapping("/{roomCode}")
    public String chatRoom(@PathVariable String roomCode,
                           @AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                           Model model) {

        User loggedUser = userService.getUserById(authenticationDetails.getId());
        ChatRoom chatRoom = chatRoomService.getChatRoomIfAuthorized(roomCode, loggedUser.getHero());

        if (chatRoom == null) {
            return "redirect:/";
        }

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("messages", chatRoom.getMessages().reversed());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("messageDTO", new ChatRoomMessageDTO());

        return "chatRoomView";
    }

    @PostMapping("/{roomCode}/create-message")
    public String sendMessage(@ModelAttribute ChatRoomMessageDTO chatRoomMessageDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable String roomCode, Model model) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        chatMessagesService.sendMessage(roomCode, loggedUser, chatRoomMessageDTO);
        return "redirect:/chat-room/" + roomCode;
    }

    @PostMapping("/create-chatroom/{id}")
    public String createChatRoom(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, Model model) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());
        User targetUser = userService.getUserById(id);

        if (id.equals(loggedUser.getId())) {
            return "redirect:/chat-room";
        }

        chatRoomService.createChatRoom(loggedUser.getHero(), targetUser.getHero());

        return "redirect:/chat-room";
    }
}
