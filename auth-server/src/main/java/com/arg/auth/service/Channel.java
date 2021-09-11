/*
 * arg license
 *
 */

package com.arg.auth.service;

import com.arg.auth.dto.request.Message;

public interface Channel {

    public boolean sendMessage(Message message);
}
