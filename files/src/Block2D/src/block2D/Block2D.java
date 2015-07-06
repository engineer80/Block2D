package block2D;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

public class Block2D extends JFrame {		
	private Configurator configurator = null;
	private Board board = null;
		
    public Block2D(String confFileName, String shapesFileName) {
			configurator = new Configurator();
			configurator.setName("configurator");
			configurator.setConfFileName(confFileName);
			configurator.setShapesFileName(shapesFileName);
			configurator.load();
			configurator.setCallback(new ConfiguratorCallback());
      board = new Board();
			board.setName("board");
			board.setShapeGenerator(configurator.getShapeGenerator());
			board.setBoardWidth(configurator.getBoardWidth());
			board.setBoardHeight(configurator.getBoardHeight());
			board.setInitialDelay(configurator.getInitialDelay());
			board.setLevelTimeDelay(configurator.getLevelTimeDelay());
			board.setLevelNumLines(configurator.getLevelNumLines());
			board.setAllowFlipFlag(configurator.getAllowFlipFlag());
			board.setGenFlippedFlag(configurator.getGenFlippedFlag());
			board.setShowNextFlag(configurator.getShowNextFlag());
			board.init();
			board.initUI();
			board.setCallback(new BoardCallback());
			getContentPane().setLayout(new CardLayout());
			getContentPane().add(configurator,"configurator");
			getContentPane().add(board,"board");
			setSize(208, 420);
      setTitle("Block2D");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
			((CardLayout)(getContentPane().getLayout())).show(getContentPane(),"board");						
   }	 
	 
	 private class ConfiguratorCallback implements Configurator.Callback {
		@Override
		public void call() {
			board.setShapeGenerator(configurator.getShapeGenerator());
			board.setBoardWidth(configurator.getBoardWidth());
			board.setBoardHeight(configurator.getBoardHeight());
			board.setInitialDelay(configurator.getInitialDelay());
			board.setLevelTimeDelay(configurator.getLevelTimeDelay());
			board.setLevelNumLines(configurator.getLevelNumLines());
			board.setAllowFlipFlag(configurator.getAllowFlipFlag());
			board.setGenFlippedFlag(configurator.getGenFlippedFlag());
			board.setShowNextFlag(configurator.getShowNextFlag());
			board.init();
			board.refreshUI();		
			CardLayout cardLayout = (CardLayout)(getContentPane().getLayout());
			configurator.setFocusable(false);
			board.setFocusable(true);
			cardLayout.show(getContentPane(),"board");
		}
	 }
	 
	 private class BoardCallback implements Board.Callback {
		@Override
		public void call() {
			CardLayout cardLayout = (CardLayout)(getContentPane().getLayout());
			board.setFocusable(false);
			configurator.setFocusable(true);
			cardLayout.show(getContentPane(),"configurator");			
		}
	 }

	public static void main(String[] args) {
		String confFile=null;
		String shapesFile=null;
		if(args!=null && args.length>0){
			confFile=args[0];
			if(args.length>1){
				shapesFile=args[1];
			}
		}
		if(confFile==null){
			confFile="conf.txt";			
		}
		if(shapesFile==null){
			shapesFile="shapes.txt";
		}
		Block2D game = new Block2D(confFile,shapesFile);
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	} 
}
