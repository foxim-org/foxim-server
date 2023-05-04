package com.hnzz.controller;

import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.result.Result;
import com.hnzz.dto.TimelineDto;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.Reply;
import com.hnzz.entity.Timeline;
import com.hnzz.form.Timelinefrom.DeleteReply;
import com.hnzz.form.Timelinefrom.ReplyFrom;
import com.hnzz.form.Timelinefrom.Timelinefrom;
import com.hnzz.form.userform.SendTimelineFrom;
import com.hnzz.service.ContactsService;
import com.hnzz.service.FileInfoService;
import com.hnzz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.controller
 * @ClassName:Timelines
 * @Author 冼大丰
 * @Date 2023/1/28 15:17
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/timelines")
@Slf4j
@Api(tags = "朋友圈接口")
public class TimelinesController {

    @Value("${app.uploadUrl}")
    private String uploadUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private FileInfoService fileInfoService;

    @GetMapping("/{friendsId}")
    @ApiOperation("查看某人的朋友圈")
    public ResponseEntity userTimelines(@RequestHeader("userId") String userId,@PathVariable("friendsId") String friendsId){
        if (friendsId==null){
            return ResultUtil.response(HttpStatus.FORBIDDEN,"好友Id不能为空！");
        }

        Contacts contacts = contactsService.selectByUserId(userId, friendsId);

        if (contacts==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不是我的好友！");
        }

        List<TimelineDto> timelineDtos = userService.findTimelineByUserId(friendsId);

        return ResultUtil.resultToResponse(Result.success(timelineDtos));
    }

    @PostMapping()
    @ApiOperation("发送朋友圈内容")
    public ResponseEntity timelines(@RequestHeader("userId") String userId, @RequestBody SendTimelineFrom sendTimelineFrom) {

        if (sendTimelineFrom == null) {
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR, "发送的朋友圈内容不能为空！");
        }

        Timeline timeline = userService.saveTimelines(sendTimelineFrom, userId);

        return ResultUtil.resultToResponse(Result.success(timeline));
    }

    @PostMapping("/remove/{_id}")
    @ApiOperation("删除朋友圈内容")
    public ResponseEntity removeTimelines(@RequestHeader("userId") String userId, @PathVariable("_id") String timelineId){

        userService.removeTimelinesById(userId,timelineId);
        return ResultUtil.resultToResponse(Result.success("删除成功"));
    }

    @GetMapping()
    @ApiOperation("查看朋友圈")
    public ResponseEntity timeline(@RequestHeader("userId") String userId){

        List<String> friendsInfo = contactsService.findFriendsInfo(userId);

        friendsInfo.add(userId);

        List<Timeline> timelinelist = userService.getTimeline(friendsInfo);
        log.info("朋友圈数据 - {}",timelinelist);

        List<UserDTO> usersById = userService.getUsersById(friendsInfo);
        log.info("好友信息 - {}",usersById);
        List<TimelineDto> timelineDtos =new ArrayList<>();


        for (int i = 0; i < timelinelist.size(); i++) {
            for (int j = 0; j < usersById.size(); j++) {
                if (timelinelist.get(i).getUserId().equals(usersById.get(j).getId())){
                    TimelineDto timelineDto=new TimelineDto();
                    timelineDto.setUserId(usersById.get(j).getId())
                            .setUsername(usersById.get(j).getUsername())
                            .setAvatarUrl(usersById.get(j).getAvatarUrl())
                            .setText(timelinelist.get(i).getText())
                            .setCreatedAt(timelinelist.get(i).getCreatedAt())
                            .setReplies(timelinelist.get(i).getReplies())
                            .setImageUrls(timelinelist.get(i).getImageUrls())
                            .setId(timelinelist.get(i).getId())
                            .setVideoUrls(timelinelist.get(i).getVideoUrls());
                    timelineDtos.add(timelineDto);
                }
            }
        }
        return ResultUtil.resultToResponse(Result.success(timelineDtos));
    }

    @PostMapping("/likeTimelines")
    @ApiOperation("点赞朋友圈")
    public ResponseEntity<String> likeTimelines(@RequestHeader("userId") String userId, @RequestBody ReplyFrom replyfrom){

        //查询朋友圈记录是否存在
        Timeline timeline = userService.getTimelineById(replyfrom.getTimelineId());
        //判断
        if (timeline==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该条朋友圈不存在或已删除!");
        }
        //点赞
        return userService.star(replyfrom,userId);
    }

    @PostMapping("/replyTimelines")
    @ApiOperation("回复朋友圈")
    public ResponseEntity replyTimelines(@RequestHeader("userId") String userId,@RequestBody ReplyFrom replyfrom){

        UserDTO userById = userService.findUserById(userId);
        //查询朋友圈记录是否存在
        Timeline timeline = userService.getTimelineById(replyfrom.getTimelineId());
        //判断
        if (timeline==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该条朋友圈不存在或已删除!");
        }
        List<Reply> replies = timeline.getReplies();

        //回复
        Reply Reply = new Reply()
                .setId(new ObjectId().toString())
                .setUserId(userId)
                .setAvatarUrl(userById.getAvatarUrl())
                .setReplyText(replyfrom.getText())
                .setUsername(replyfrom.getUsername());

        replies.add(Reply);

        Timeline timelines = timeline.setReplies(replies);

        userService.updateTimelines(timelines);

        return ResultUtil.resultToResponse(Result.success(Reply));
    }


    @PostMapping("/deleteReplyTimelines")
    @ApiOperation("删除回复")
    public ResponseEntity deleteReplyTimelines(@RequestHeader("userId") String userId,@RequestBody DeleteReply deleteReply){

        if (deleteReply==null){
            return ResultUtil.response(HttpStatus.FORBIDDEN,"replyId不能为空");
        }

        Timeline timelineById = userService.getTimelineById(deleteReply.getTimelineId());

        if (timelineById==null){
            return ResultUtil.response(HttpStatus.FORBIDDEN,"这条朋友圈不存在或已删除");
        }

        List<Reply> replies = timelineById.getReplies();

        for (int i = 0; i < replies.size(); i++) {
            if (replies.get(i).getId().equals(deleteReply.getReplyId())){
                replies.remove(i);
            }
        }

        Timeline timeline = timelineById.setReplies(replies);

        return ResultUtil.resultToResponse(Result.success(userService.updateTimelines(timeline)));
    }





    @PostMapping("/repostTimelines")
    @ApiOperation("转发朋友圈")
    public ResponseEntity repostTimelines(@RequestHeader("userId") String userId, @RequestBody Timelinefrom timelinefrom){
        //判断
        if (timelinefrom==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"转发的朋友圈不能为空！");
        }
        //获取当前用户数据
        UserDTO userById = userService.findUserById(userId);

        //生成数据
        Reply reply=new Reply()
                .setAuthorAvatarUrl(timelinefrom.getAvatarUrl())
                .setAuthorId(timelinefrom.getUserId())
                .setAuthorUsername(timelinefrom.getUsername())
                .setComment(timelinefrom.getComment())
                .setAuthorText(timelinefrom.getText())
                .setAvatarUrl(userById.getAvatarUrl())
                .setUserId(userById.getId())
                .setImageUrls(timelinefrom.getImageUrls());

        List<Reply> replyList=new ArrayList<>();

        replyList.add(reply);


        Timeline timeline=new Timeline()
                .setText(timelinefrom.getComment())
                .setReplies(replyList)
                .setUserId(userId)
                .setCreatedAt(new Date());


        Timeline timelines = userService.saveTimelines(timeline);


        return ResultUtil.resultToResponse(Result.success(timelines));
    }
}
