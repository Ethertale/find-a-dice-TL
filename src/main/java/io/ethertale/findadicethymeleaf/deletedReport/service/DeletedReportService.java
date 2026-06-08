package io.ethertale.findadicethymeleaf.deletedReport.service;

import io.ethertale.findadicethymeleaf.campaign.model.CampaignMessage;
import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReport;
import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReportType;
import io.ethertale.findadicethymeleaf.deletedReport.repo.DeletedReportRepo;
import io.ethertale.findadicethymeleaf.event.model.Event;
import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeletedReportService {

    private final DeletedReportRepo deletedReportRepo;

    @Autowired
    public DeletedReportService(DeletedReportRepo deletedReportRepo) {
        this.deletedReportRepo = deletedReportRepo;
    }

    public List<DeletedReport> getAllDeletedReportsFromType(DeletedReportType deletedReportType) {
        return deletedReportRepo.getAllByType(deletedReportType);
    };
//    public List<GroupPost> getAllDeletedGroupPosts(){
//        return (List<GroupPost>) deletedReportRepo.getAllByType(DeletedReportType.GROUP_POST);
//    };
//    public List<Event> getAllDeletedEvents(){
//        return (List<Event>) deletedReportRepo.getAllByType(DeletedReportType.EVENT);
//    };
//    public List<ChatMessages> getAllDeletedChatMessages(){
//        return (List<ChatMessages>) deletedReportRepo.getAllByType(DeletedReportType.CHAT_MESSAGE);
//    };
//    public List<CampaignMessage>  getAllDeletedCampaignMessages(){
//        return deletedReportRepo.getAllByType(DeletedReportType.CAMPAIGN_CHAT_MESSAGE);
//    };
}
