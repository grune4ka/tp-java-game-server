package tests;

import static org.junit.Assert.*;




import gameMechanics.Gamer;
import org.testng.annotations.Test;

public class GamerTest {
	

	@Test
	public void testGetId() {
		Gamer gamer = new Gamer(1);
		assertEquals(gamer.getId(), 1);
	}

	@Test
	public void testGetBoardPosition() {
		Gamer gamer = new Gamer(1);
		gamer.setBoardPosition(23);
		assertEquals(gamer.getBoardPosition(), 23);
	}

	@Test
	public void testGetPoints() {
		Gamer gamer = new Gamer(1);
		gamer.setPoints(42);
		assertEquals(gamer.getPoints(), 42);
	}

	@Test
	public void testSetId() {
		Gamer gamer = new Gamer(1);
		assertEquals(gamer.getId(), 1);
		gamer.setId(123);
		assertEquals(gamer.getId(), 123);
	}

	@Test
	public void testSetBoardPosition() {
		Gamer gamer = new Gamer(1);
		gamer.setBoardPosition(1234);
		assertEquals(gamer.getBoardPosition(), 1234);
	}

	@Test
	public void testSetPoints() {
		Gamer gamer = new Gamer(1);
		gamer.setPoints(1234);
		assertEquals(gamer.getPoints(), 1234);
	}

}
