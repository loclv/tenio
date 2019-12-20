/*
The MIT License

Copyright (c) 2016-2019 kong <congcoi123@gmail.com>

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
package com.tenio.engine.heartbeat;

import com.tenio.configuration.BaseConfiguration;

/**
 * The Java ExecutorService is a construct that allows you to pass a task to be
 * executed by a thread asynchronously. The executor service creates and
 * maintains a reusable pool of threads for executing submitted tasks. This
 * class helps you create and manage your HeartBeats @see
 * {@link AbstractHeartBeat}
 * 
 * @author kong
 *
 */
public interface IHeartBeatManager {

	/**
	 * The number of maximum heart-beats that the server can handle.
	 * 
	 * @param configuration @see {@link BaseConfiguration}
	 */
	void initialize(final BaseConfiguration configuration);

	/**
	 * @param maxHeartbeat The number of maximum heart-beats that the server can
	 *                     handle
	 */
	void initialize(final int maxHeartbeat);

	/**
	 * Create a new heart-beat.
	 * 
	 * @param id        the unique id
	 * @param heartbeat @see {@link AbstractHeartBeat}
	 */
	void create(final String id, final AbstractHeartBeat heartbeat);

	/**
	 * Dispose a heart-beat.
	 * 
	 * @param id the unique id
	 */
	void dispose(final String id);

	/**
	 * Check if a heart-beat is existed or not.
	 * 
	 * @param id the unique id
	 * @return Returns <code>true</code> if the corresponding heart-beat has existed
	 */
	boolean contains(final String id);

	/**
	 * Destroy all heart-beats and clear all references.
	 */
	void clear();

}