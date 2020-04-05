package com.wldemo.demo.dto;

import com.wldemo.demo.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;//发出通知的人的id
    private String notifyName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
