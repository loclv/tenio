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
package com.tenio.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tenio.api.PlayerApi;
import com.tenio.api.RoomApi;
import com.tenio.configuration.constant.ErrorMsg;
import com.tenio.entities.manager.IPlayerManager;
import com.tenio.entities.manager.IRoomManager;
import com.tenio.entities.manager.PlayerManager;
import com.tenio.entities.manager.RoomManager;
import com.tenio.exception.DuplicatedPlayerException;
import com.tenio.exception.DuplicatedRoomException;
import com.tenio.exception.NullPlayerNameException;
import com.tenio.models.PlayerModel;
import com.tenio.models.RoomModel;

/**
 * @author kong
 */
public final class PlayerRoomTest {

	private IPlayerManager __playerManager;
	private IRoomManager __roomManager;
	private PlayerApi __playerApi;
	private RoomApi __roomApi;

	private String __testPlayerName;
	private String __testRoomId;

	@BeforeEach
	public void initialize() {
		__playerManager = new PlayerManager();
		__roomManager = new RoomManager();
		__playerApi = new PlayerApi(__playerManager, __roomManager);
		__roomApi = new RoomApi(__roomManager);
		__testPlayerName = "kong";
		__testRoomId = UUID.randomUUID().toString();
	}

	@AfterEach
	public void tearDown() {
		__playerManager.clear();
		__roomManager.clear();
	}

	@Test
	public void addNewPlayerShouldReturnSuccess() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		var result = __playerApi.get(__testPlayerName);
		
		assertEquals(player, result);
	}

	@Test
	public void addDupplicatedPlayerShouldCauseException() {
		assertThrows(DuplicatedPlayerException.class, () -> {
			var player = new PlayerModel(__testPlayerName);
			__playerApi.login(player);
			__playerApi.login(player);
		});
	}

	@Test
	public void addNullPlayerNameShouldCauseException() {
		assertThrows(NullPlayerNameException.class, () -> {
			var player = new PlayerModel(null);
			__playerApi.login(player, null);
		});
	}

	@Test
	public void checkContainPlayerShouldReturnSuccess() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		
		assertTrue(__playerApi.contain(__testPlayerName));
	}

	@Test
	public void countPlayersShouldReturnTrueValue() {
		for (int i = 0; i < 10; i++) {
			var player = new PlayerModel(UUID.randomUUID().toString());
			__playerApi.login(player);
		}
		
		assertEquals(10, __playerApi.count());
	}

	@Test
	public void countRealPlayersShouldReturnTrueValue() {
		for (int i = 0; i < 10; i++) {
			var player = new PlayerModel(UUID.randomUUID().toString());
			__playerApi.login(player);
		}
		
		assertEquals(0, __playerApi.countPlayers());
	}

	@Test
	public void removePlayerShouldReturnSuccess() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		__playerApi.logOut(__testPlayerName);
		
		assertEquals(0, __playerApi.count());
	}

	@Test
	public void createNewRoomShouldReturnSuccess() {
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		assertTrue(__roomApi.contain(__testRoomId));
	}

	@Test
	public void createDuplicatedRoomShouldCauseException() {
		assertThrows(DuplicatedRoomException.class, () -> {
			var room = new RoomModel(__testRoomId, "Test Room", 3);
			__roomApi.add(room);
			__roomApi.add(room);
		});
	}

	@Test
	public void playerJoinRoomShouldReturnSuccess() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		assertEquals(null, __playerApi.playerJoinRoom(__roomApi.get(__testRoomId), __playerApi.get(__testPlayerName)));
	}

	@Test
	public void addDuplicatedPlayerToRoomShouldReturnErrorMessage() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		__playerApi.playerJoinRoom(__roomApi.get(__testRoomId), __playerApi.get(__testPlayerName));
		
		assertEquals(ErrorMsg.PLAYER_WAS_IN_ROOM,
				__playerApi.playerJoinRoom(__roomApi.get(__testRoomId), __playerApi.get(__testPlayerName)));
	}

	@Test
	public void playerLeaveRoomShouldReturnSuccess() {
		var player = new PlayerModel(__testPlayerName);
		__playerApi.login(player);
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		__playerApi.playerJoinRoom(__roomApi.get(__testRoomId), __playerApi.get(__testPlayerName));
		__playerApi.playerLeaveRoom(__playerApi.get(__testPlayerName), true);
		
		assertAll("playerLeaveRoom", () -> assertFalse(__roomApi.get(__testRoomId).contain(__testPlayerName)),
				() -> assertEquals(null, __playerApi.get(__testPlayerName).getRoom()));
	}

	@Test
	public void addNumberPlayersExceedsRoomCapacityShouldReturnErrorMessage() {
		for (int i = 0; i < 10; i++) {
			var player = new PlayerModel(UUID.randomUUID().toString());
			__playerApi.login(player);
		}
		
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		int capacity = __roomApi.get(__testRoomId).getCapacity();
		PlayerModel[] players = new PlayerModel[capacity + 1];
		int counter = 0;
		for (var player : __playerApi.gets().values()) {
			if (counter > capacity) {
				break;
			}
			players[counter] = (PlayerModel) player;
			counter++;
		}
		
		assertAll("playerJoinRoom", () -> assertEquals(3, capacity),
				() -> assertEquals(null, __playerApi.playerJoinRoom(__roomApi.get(__testRoomId), players[0])),
				() -> assertEquals(null, __playerApi.playerJoinRoom(__roomApi.get(__testRoomId), players[1])),
				() -> assertEquals(null, __playerApi.playerJoinRoom(__roomApi.get(__testRoomId), players[2])),
				() -> assertEquals(ErrorMsg.ROOM_IS_FULL,
						__playerApi.playerJoinRoom(__roomApi.get(__testRoomId), players[3])));
	}

	@Test
	public void removeRoomShouldReturnSuccess() {
		var room = new RoomModel(__testRoomId, "Test Room", 3);
		__roomApi.add(room);
		
		for (int i = 0; i < 3; i++) {
			var player = new PlayerModel(UUID.randomUUID().toString());
			__playerApi.login(player);
			__playerApi.playerJoinRoom(__roomApi.get(__testRoomId), player);
		}
		
		__roomApi.remove(__roomApi.get(__testRoomId));
		boolean allRemoved = true;
		for (var player : __playerApi.gets().values()) {
			if (player.getRoom() != null) {
				allRemoved = false;
				break;
			}
		}
		final boolean removedResult = allRemoved;
		
		assertAll("removeRoom", () -> assertFalse(__roomApi.contain(__testRoomId)), () -> assertTrue(removedResult));
	}

}
