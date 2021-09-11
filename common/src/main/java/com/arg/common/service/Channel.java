/*
 * arg license
 *
 */

package com.arg.common.service;

import com.arg.common.dto.Message;

public interface Channel {

    public Boolean sendMessage(Message message);

    public Boolean optIn(String mobileNumber);

}
