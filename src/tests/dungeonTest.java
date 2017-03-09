package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import gameLogic.Coord;
import gameLogic.Direction;
import gameLogic.GameConfig;
import gameLogic.GameLogic;

public class dungeonTest {

	@Test
	public void testMoveHeroIntoToFreeCell(){
		
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.DOWN);
		assertEquals(new Coord(2,1), g.getHero().getPosition());
	}

	@Test
	public void testMoveHeroIntoToWall(){
		
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.UP);
		assertEquals(new Coord(1,1), g.getHero().getPosition());
	}
	
	@Test
	public void testHeroIsCapturedByGuard(){
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.RIGHT);
		assertFalse(g.isWon());
		
	}
	
	@Test
	public void testMoveHeroIntoClosedDoor(){
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.DOWN);
		g.updateGame(0,Direction.LEFT);
		assertEquals(new Coord(2,1), g.getHero().getPosition());
	}
	
	@Test
	public void testMoveHeroIntoLeverCell(){
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.DOWN);
		g.updateGame(0,Direction.DOWN);
		assertEquals('S', g.getBoard().getBoardAt(2,0));
	}
	
	@Test
	public void testMoveHeroIntoOpenDoor(){
		GameConfig game = new GameConfig(5,5);
		GameLogic g = new GameLogic(0, game);
		g.updateGame(0,Direction.DOWN);
		g.updateGame(0,Direction.DOWN);
		g.updateGame(0,Direction.LEFT);
		assertTrue(g.isWon());
	}
}


