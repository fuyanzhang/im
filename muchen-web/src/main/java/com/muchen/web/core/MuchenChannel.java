package com.muchen.web.core;

import lombok.Data;

import java.nio.channels.SocketChannel;

/**
 * ClassName MuchenChannel
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/18 15:07
 **/
@Data
public class MuchenChannel {
    /**
     * 通道
     */
    private SocketChannel channel;

    /**
     * 通道类型
     */
    private MuchenChannelType type;
}
