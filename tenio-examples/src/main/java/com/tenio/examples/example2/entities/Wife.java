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
package com.tenio.examples.example2.entities;

import com.tenio.engine.fsm.MessageDispatcher;
import com.tenio.engine.fsm.entities.FSMComponent;
import com.tenio.engine.fsm.entities.Telegram;
import com.tenio.examples.example2.constants.EntityName;
import com.tenio.examples.example2.constants.Location;
import com.tenio.examples.example2.state.wife.DoHouseWork;
import com.tenio.examples.example2.state.wife.WifesGlobalState;

/**
 * The miner's wife entity.
 * 
 * @author kong
 *
 */
public class Wife extends BaseEntity {

	/**
	 * @see FSMComponent
	 */
	private FSMComponent<Wife> __state;
	/**
	 * @see Location
	 */
	private Location __location;
	/**
	 * Is she cooking?
	 */
	private boolean __cooking = false;

	public Wife(MessageDispatcher dispatcher, EntityName name) {
		super(name);
		__location = Location.SHACK;
		__state = new FSMComponent<Wife>(dispatcher, this);
		__state.setCurrentState(DoHouseWork.getInstance());
		__state.setGlobalState(WifesGlobalState.getInstance());
	}

	public FSMComponent<Wife> getFSM() {
		return __state;
	}

	public Location getLocation() {
		return __location;
	}

	public void changeLocation(Location location) {
		__location = location;
	}

	public boolean isCooking() {
		return __cooking;
	}

	public void setCooking(boolean cooking) {
		__cooking = cooking;
	}

	@Override
	public void update(float delta) {
		__state.update(delta);
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		return __state.handleMessage(msg);
	}

}
