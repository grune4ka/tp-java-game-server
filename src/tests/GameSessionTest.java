package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import gameMechanics.GameSession;
import gameMechanics.Gamer;

public class GameSessionTest {
	private GameSession session;
	private Gamer gamerA;
	private Gamer gamerB;

	public void setUp() {
		this.session = new GameSession();
	}

	@Test
	public void testGameSession() {
		this.setUp();
		assertTrue(this.session.haveFreeSlots());
	}

	@Test
	public void testAddGamer() {
		this.setUp();
		this.session.addGamer(1);
		assertTrue(this.session.haveUser(1));
		assertFalse(this.session.haveUser(2));
		
		
		this.session.addGamer(2);
		assertTrue(this.session.haveUser(2));
		assertFalse(this.session.haveUser(3));
		
	}

	@Test
	public void testHaveFreeSlots() {
		this.setUp();
		assertTrue(this.session.haveFreeSlots());
		this.session.addGamer(1);
		assertTrue(this.session.haveFreeSlots());
		this.session.addGamer(2);
		assertFalse(this.session.haveFreeSlots());
	}

	@Test
	public void testHaveUser() {
		this.setUp();
		this.session.addGamer(1);
		assertTrue(this.session.haveUser(1));
		assertFalse(this.session.haveUser(2));
		this.session.addGamer(2);
		assertTrue(this.session.haveUser(1));
		assertTrue(this.session.haveUser(2));
		assertFalse(this.session.haveUser(3));
	}

	@Test
	public void testSetBoardPositionById() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsGameEnd() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGameSessionSnapshot() {
		fail("Not yet implemented");
	}

	@Test
	public void testNextTick() {
		fail("Not yet implemented");
	}

}
