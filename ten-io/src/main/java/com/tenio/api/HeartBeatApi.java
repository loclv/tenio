/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.api;

import com.tenio.engine.heartbeat.AbstractHeartBeat;
import com.tenio.engine.heartbeat.IHeartBeatManager;
import com.tenio.entities.element.TObject;
import com.tenio.logger.AbstractLogger;

/**
 * This class provides you a necessary interface for managing heart beats.
 * 
 * See {@link IHeartBeatManager}
 * 
 * @author kong
 * 
 */
public final class HeartBeatApi extends AbstractLogger {

	/**
	 * @see IHeartBeatManager
	 */
	private final IHeartBeatManager __heartBeatManager;

	public HeartBeatApi(IHeartBeatManager heartBeatManager) {
		__heartBeatManager = heartBeatManager;
	}
	
	public void initialize(int maxHeartbeat) {
		__heartBeatManager.initialize(maxHeartbeat);
	}

	public void create(final String id, final AbstractHeartBeat heartbeat) {
		__heartBeatManager.create(id, heartbeat);
	}

	public void dispose(final String id) {
		__heartBeatManager.dispose(id);
	}
	
	public void sendMessage(final String id, final TObject message, final double delayTime) {
		__heartBeatManager.sendMessage(id, message, delayTime);
	}

	public void sendMessage(final String id, final TObject message) {
		__heartBeatManager.sendMessage(id, message);
	}

}
