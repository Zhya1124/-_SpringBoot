package com.wldemo.demo.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.wldemo.demo.dto.NotificationDTO;
import com.wldemo.demo.dto.PaginationDTO;
import com.wldemo.demo.dto.QuestionDTO;
import com.wldemo.demo.enums.NotificationStatusEnum;
import com.wldemo.demo.enums.NotificationTypeEnum;
import com.wldemo.demo.exception.CustomizeErrorCode;
import com.wldemo.demo.exception.CustomizeException;
import com.wldemo.demo.mapper.NotificationMapper;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();//分页DTO
        Integer totalPage;
        NotificationExample notificationExample1 = new NotificationExample();
        notificationExample1.createCriteria().andReceiverEqualTo(userId);//要收到通知的人
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample1);//查询获取问题总数
        //该用户总页数(之前在paginationService里现在单独拿出来)
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //传入page越界处理
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);//计算好对应的标志和页数
        Integer offset = size * (page - 1);//计算offset
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));

        if(notifications.size()==0){
            return paginationDTO;//直接返回空的页面dto
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));//DTO的type是string类型用于显示到页面，notification的则是int

            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);//给DTO所需的查询量
        return paginationDTO;
    }
    //未读通知计数
    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }
    //读通知并更新状态，包含异常处理和数据库int型type和string型转换
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){//注意比较方式，是object之间的比较，因用
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);//修改status为已读1

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
