package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.notification.message.NotificationMessageDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteServiceAbstract;
import com.quick.recording.gateway.service.notification.NotificationServiceNotificationMessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteNotificationMessageServiceImpl
        extends MainRemoteServiceAbstract<NotificationMessageDto, NotificationServiceNotificationMessageApi>
        implements RemoteNotificationMessageService {

    @Autowired
    public RemoteNotificationMessageServiceImpl(NotificationServiceNotificationMessageApi service) {
        super(service);
    }

    @Override
    public Class<NotificationMessageDto> getType() {
        return NotificationMessageDto.class;
    }

}
