package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.service.ChatMessagesService;
import io.ethertale.findadicethymeleaf.chat.service.ChatRoomService;
import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String chatRoom(@PathVariable String roomCode, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, Model model) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());
        ChatRoom chatRoom = chatRoomService.getChatRoomIfAuthorized(roomCode, loggedUser.getHero());

        ModelAndView modelAndView = new ModelAndView("chatRoomView");
        modelAndView.addObject("chatRoom", chatRoom);
        modelAndView.addObject("messages", chatRoom.getMessages());
        modelAndView.addObject("loggedUser", loggedUser);

        if (chatRoom == null) {
            return "redirect:/";
        }

        return modelAndView.toString();
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
