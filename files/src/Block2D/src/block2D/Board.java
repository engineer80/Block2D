package block2D;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

import block2D.Shape;
import block2D.ShapeGenerator;

public class Board extends JPanel {
// Configuration params
	private ShapeGenerator shapeGenerator = null; 
	private int boardWidth;
  private int boardHeight;
	private int initialDelay;	
	private int levelTimeDelay;
	private int levelNumLines;			
	private boolean allowFlipFlag;	
	private boolean genFlippedFlag;
	private boolean showNextFlag;   
	private Callback callback = null;
// Gameplay non-configuration params
	private int messageDelay = 1000;
	private int speedLevel = 0;		
  private boolean isFallingFinished = false;
  private boolean isStarted = false;
	private boolean isNewGame = true;
  private boolean isPaused = false;
  private int numLinesRemoved = 0;		
  private int curX = 0;
  private int curY = 0;
  private Shape curShape = ShapeGenerator.NO_SHAPE;
	private Shape nextShape = ShapeGenerator.NO_SHAPE;
  private Shape[] board = null;	
// GUI params
	private int infoPanelMinHeight = 40;	
	private int infoPanelMinWeight = 40;	
	private int boardMarginMinWidth = 2;
	private int boardMarginMinHeight = 2;
// GUI elements	
  private JLabel statusLabel = null;
	private JLabel scoreLabel = null;
	private Timer timer = null;
	private Timer messageTimer = null;
	private StepListener stepListener = null;
	private MessageListener messageListener = null;
	private JPanel infoPanel = null;	
	private NextShapePanel nextShapePanel = null;
	private DrawPanel drawPanel = null;
// GUI action params		
	private enum BoardAction {START,STOP,PAUSE,MOVE_LEFT,MOVE_RIGHT,ROTATE_RIGHT,ROTATE_LEFT,FLIP_VERTICAL,FLIP_HORIZONTAL,DROP_DOWN,ONE_LINE_DOWN,CALL};		
	private Map<BoardAction,List<Integer>> boardKeyActionMap = new HashMap() {{
		put(BoardAction.START,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_ENTER));}});
		put(BoardAction.STOP,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_ESCAPE));}});				
		put(BoardAction.PAUSE,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_P));}});
		put(BoardAction.MOVE_LEFT,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_LEFT));}});
		put(BoardAction.MOVE_RIGHT,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_RIGHT));}});
		put(BoardAction.ROTATE_RIGHT,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_DOWN));}});
		put(BoardAction.ROTATE_LEFT,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_UP));}});		
		put(BoardAction.FLIP_VERTICAL,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_Z));}});		
		put(BoardAction.FLIP_HORIZONTAL,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_X));}});		
		put(BoardAction.DROP_DOWN,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_SPACE));}});		
		put(BoardAction.ONE_LINE_DOWN,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_D));}});		
		put(BoardAction.CALL,new ArrayList<Integer>() {{add(new Integer(KeyEvent.VK_S));}});				
	}};		
	private enum BoardMessage {START,GAME_OVER,NEW_GAME,CONFIG,PAUSE,EMPTY};
	private BoardMessage boardMessage = BoardMessage.EMPTY;
	private Map<BoardMessage,String> boardMessageMap = null;

  public Board() {	
		super(new BorderLayout());
		stepListener = new StepListener();
		timer = new Timer(initialDelay, stepListener);			
		messageListener = new MessageListener();
		messageTimer = new Timer(messageDelay,messageListener);		
  }			

	public void init(){
		board = new Shape[boardWidth * boardHeight + boardWidth*shapeGenerator.getMaxHeight()+shapeGenerator.getMaxWidth()];	 
		clearBoard();
		curShape = ShapeGenerator.NO_SHAPE;
		nextShape = ShapeGenerator.NO_SHAPE;
		messageTimer.stop();	
		boardMessageMap = new HashMap() {{
			put(BoardMessage.START," Press "+keys(BoardAction.START)+" to start!");
			put(BoardMessage.GAME_OVER," Game over!");
			put(BoardMessage.NEW_GAME," New game!");
		  put(BoardMessage.CONFIG," Press "+keys(BoardAction.CALL)+" to config!");
			put(BoardMessage.PAUSE,"  Paused!");
			put(BoardMessage.EMPTY,"");
		}};		
		isNewGame = true;	
	}	
	
	public void initUI() {
		{
			drawPanel = new DrawPanel();		
			drawPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));							
		}
		{
			infoPanel = new JPanel(new BorderLayout());
			infoPanel.setBorder(BorderFactory.createEtchedBorder());														
			infoPanel.setPreferredSize(new Dimension(infoPanelMinHeight,infoPanelMinWeight));
			JPanel messagePanel = new JPanel(new BorderLayout());
			JPanel scorePanel = new JPanel(new BorderLayout());					
			scoreLabel =  new JLabel();			
			scoreLabel.setText("");			
			scorePanel.add(scoreLabel,BorderLayout.CENTER);
			messagePanel.add(scorePanel,BorderLayout.NORTH);
			JPanel statusPanel = new JPanel(new BorderLayout());
			statusLabel = new JLabel();					
			statusLabel.setText(boardMessageMap.get(boardMessage));														
			statusPanel.add(statusLabel,BorderLayout.CENTER);
			messagePanel.add(statusPanel,BorderLayout.SOUTH);			
			infoPanel.add(messagePanel,BorderLayout.CENTER);
			nextShapePanel = new NextShapePanel();
			nextShapePanel.setBorder(BorderFactory.createEtchedBorder());
			nextShapePanel.setPreferredSize(new Dimension(infoPanelMinHeight,infoPanelMinWeight));			
			nextShapePanel.setActiveFlag(showNextFlag);
			if(showNextFlag) {
				infoPanel.add(nextShapePanel,BorderLayout.EAST);
			}
			nextShapePanel.repaint();
		}
		this.setBorder(BorderFactory.createEtchedBorder());														
		this.add(infoPanel,BorderLayout.NORTH);
		this.add(drawPanel,BorderLayout.CENTER);		
		genKeyMaps();
		boardMessage = BoardMessage.NEW_GAME;
		statusLabel.setText(boardMessageMap.get(boardMessage));		
		messageTimer.start();				
	}	
	
	public void refreshUI() {
		scoreLabel.setText("");
		messageTimer.stop();				
		boardMessage = BoardMessage.NEW_GAME;
		statusLabel.setText(boardMessageMap.get(boardMessage));			
		nextShapePanel.setActiveFlag(showNextFlag);																
		nextShapePanel.repaint();
		if(showNextFlag) {
			infoPanel.add(nextShapePanel,BorderLayout.EAST);
		} else {
			infoPanel.remove(nextShapePanel);
		}
		messageTimer.start();										
	}
	
  Shape shapeAt(int x, int y) { 
		return board[(y * boardWidth) + x]; 
	}	

  public void start() {
		if(isPaused) return;
		messageTimer.stop();
		boardMessage = BoardMessage.EMPTY;
		speedLevel = 0;		
		isFallingFinished = false;
		isStarted = false;
		isPaused = false;
		numLinesRemoved = 0;		
		curX = 0;
		curY = 0;				
		isStarted = true;
		isNewGame = false;
		timer.setDelay(initialDelay);								
		timer.start();	
		clearBoard();
		nextShape = shapeGenerator.getShape();
		statusLabel.setText(boardMessageMap.get(boardMessage));				
		scoreLabel.setText(" rows 0"+"  level 0");		
		newShape();			
  }
	
	public void stop() {
		if(isPaused) return;
		timer.stop();
    curShape = ShapeGenerator.NO_SHAPE;
    isStarted = false;
		scoreLabel.setText(" rows "+String.valueOf(numLinesRemoved)+"  level "+String.valueOf(speedLevel));
		boardMessage = BoardMessage.GAME_OVER;
		statusLabel.setText(boardMessageMap.get(boardMessage));		
		messageTimer.start();		
	}

  private void pause() {
    if (!isStarted)
        return;
    isPaused = !isPaused;
    if (isPaused) {
        timer.stop();
				boardMessage = BoardMessage.PAUSE;
				statusLabel.setText(boardMessageMap.get(boardMessage));		
				scoreLabel.setText(" rows "+String.valueOf(numLinesRemoved)+"  level "+String.valueOf(speedLevel));		
    } else {
        timer.start();
				boardMessage = BoardMessage.EMPTY;
				statusLabel.setText(boardMessageMap.get(boardMessage));		
				scoreLabel.setText(" rows "+String.valueOf(numLinesRemoved)+"  level "+String.valueOf(speedLevel));			
    }
    drawPanel.repaint();
  }

  private void dropDown() {
    int newY = curY;
    while (newY > 0) {
      if (!tryMove(curShape, curX, newY - 1))
          break;
      --newY;
    }
    ShapeDropped();
  }

  private void oneLineDown() {
    if(!tryMove(curShape, curX, curY - 1))
			ShapeDropped();
  }


  private void clearBoard() {
    for (int i = 0; i < board.length; ++i)
      board[i] = ShapeGenerator.NO_SHAPE;
  }

  private void ShapeDropped() {
    for (int i = 0; i < curShape.dimension; ++i) {
			int x = curX + curShape.x(i);
			int y = curY + curShape.y(i);
			board[(y * boardWidth) + x] = curShape;
    }
    removeFullLines();
    if (!isFallingFinished)
			newShape();
  }

  private void newShape() {
		curShape = nextShape;
	  curX = (boardWidth-(curShape.maxX()-curShape.minX()))/2 - curShape.minX();
		curY = boardHeight - curShape.minY();
	  if (!tryMove(curShape, curX, curY)) {
			stop();
	  }
		else {
			nextShape = shapeGenerator.getShape();	
			if(showNextFlag) {
				nextShapePanel.repaint();
			}
		}
  }

  private boolean tryMove(Shape newShape, int newX, int newY) {
		boolean overheight = true;
	  for (int i = 0; i < newShape.dimension; ++i) {
	    int x = newX + newShape.x(i);
	    int y = newY + newShape.y(i);
			if(y <= boardHeight) {
				overheight = false;
			}
			if (x < 0 || x >= boardWidth || y < 0) {
				return false;
			}
	    if (shapeAt(x, y) != ShapeGenerator.NO_SHAPE) {
	      return false;
			}
	  }
		if(overheight){
			return false;
		}
	  curShape = newShape;
	  curX = newX;
	  curY = newY;
	  drawPanel.repaint();
	  return true;
  }

  private void removeFullLines() {
		int numFullLines = 0;
		for(int i = boardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;
			for(int j = 0; j < boardWidth; ++j) {
				if(shapeAt(j, i) == ShapeGenerator.NO_SHAPE) {
						lineIsFull = false;
						break;
				}
			}
			if(lineIsFull) {
				++numFullLines;
				for (int k = i; k < boardHeight - 1; ++k) {
					for (int j = 0; j < boardWidth; ++j)
					board[(k * boardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}
		if(numFullLines > 0) {
			numLinesRemoved += numFullLines;
			isFallingFinished = true;
			curShape = ShapeGenerator.NO_SHAPE;
			int deltaSpeedLevel = numLinesRemoved/levelNumLines - speedLevel;
			int newDelay = timer.getDelay()-levelTimeDelay*deltaSpeedLevel;
			if(newDelay>0) {
				speedLevel+=deltaSpeedLevel;
				timer.setDelay(newDelay);
			}
			boardMessage = BoardMessage.EMPTY;
			statusLabel.setText(boardMessageMap.get(boardMessage));		
			scoreLabel.setText(" rows "+String.valueOf(numLinesRemoved)+"  level "+String.valueOf(speedLevel));			
			drawPanel.repaint();
		}
	}
	 
	public void setShapeGenerator(ShapeGenerator shapeGenerator){
		this.shapeGenerator = shapeGenerator;
	}
	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}
	public void setBoardHeight(int boardHeight) {
		this.boardHeight = boardHeight;
	}
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}
	public void setLevelTimeDelay(int levelTimeDelay) {
		this.levelTimeDelay = levelTimeDelay;
	}
	public void setLevelNumLines(int levelNumLines) {
		this.levelNumLines = levelNumLines;
	}
	public void setAllowFlipFlag(boolean allowFlipFlag) {
		this.allowFlipFlag = allowFlipFlag;
	}
	public void setGenFlippedFlag(boolean genFlippedFlag) {
		this.genFlippedFlag = genFlippedFlag;
	}
	public void setShowNextFlag(boolean showNextFlag) {
		this.showNextFlag = showNextFlag;
	}	 
	
	private void genKeyMaps() {
		this.setInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,this.getInputMap());
		for(BoardAction boardAction : BoardAction.values()){
			for(Integer keyCode : boardKeyActionMap.get(boardAction)) {
				KeyStroke key = KeyStroke.getKeyStroke(keyCode.intValue(),0);
				this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key,boardAction.toString());			
			}
		}
		this.getActionMap().put(BoardAction.START.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(!isPaused && !isStarted) {
					start();
				}
			}
    });
		this.getActionMap().put(BoardAction.STOP.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(!isPaused && isStarted) {
					stop();
				}
			}
    });						
		this.getActionMap().put(BoardAction.PAUSE.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted) {
					pause();
				}
			}
    });		
		this.getActionMap().put(BoardAction.MOVE_LEFT.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					tryMove(curShape, curX - 1, curY);
				}
			}
    });				
		this.getActionMap().put(BoardAction.MOVE_RIGHT.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					tryMove(curShape, curX + 1, curY);
				}
			}
    });				
		this.getActionMap().put(BoardAction.ROTATE_RIGHT.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					tryMove(shapeGenerator.getShape(curShape).rotateRight(), curX, curY);
				}
			}
    });		
		this.getActionMap().put(BoardAction.ROTATE_LEFT.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					tryMove(shapeGenerator.getShape(curShape).rotateLeft(), curX, curY);
				}
			}
    });		
		this.getActionMap().put(BoardAction.FLIP_VERTICAL.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(allowFlipFlag && isStarted && !isPaused) {
					tryMove(shapeGenerator.getShape(curShape).flipVertical(), curX, curY);
				}
			}
    });		
		this.getActionMap().put(BoardAction.FLIP_HORIZONTAL.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(allowFlipFlag && isStarted && !isPaused) {
					tryMove(shapeGenerator.getShape(curShape).flipHorizontal(), curX, curY);
				}
			}
    });	
		this.getActionMap().put(BoardAction.DROP_DOWN.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					dropDown();
				}
			}
    });	
		this.getActionMap().put(BoardAction.ONE_LINE_DOWN.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted && !isPaused) {
					oneLineDown();
				}
			}
    });									
		this.getActionMap().put(BoardAction.CALL.toString(),new AbstractAction(){         
			public void actionPerformed(ActionEvent arg0) {
				if(!isStarted) {
					callback.call();
				}
			}
    });						
	}
	
	private class StepListener implements ActionListener {
		@Override
    public void actionPerformed(ActionEvent e) {
      if (isFallingFinished) {
          isFallingFinished = false;
          newShape();
      } else {
				oneLineDown();
      }
    }			
	}
	
	private class MessageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {			
			if(boardMessage == BoardMessage.GAME_OVER || boardMessage == BoardMessage.NEW_GAME){
				boardMessage = BoardMessage.START;
			}	else if(boardMessage == BoardMessage.START) {
				boardMessage = BoardMessage.CONFIG;
			} else if(!isNewGame) {
				boardMessage = BoardMessage.GAME_OVER;
			} else {
				boardMessage = BoardMessage.NEW_GAME;
			}
			statusLabel.setText(boardMessageMap.get(boardMessage));
		}
	}

	private class DrawPanel extends JPanel {	
	
		int squareWidth() { 
			return ((int) getSize().getWidth() - 2*boardMarginMinWidth) / boardWidth; 
		}
		
		int squareHeight() { 
			return ((int) getSize().getHeight() - 2*boardMarginMinHeight) / boardHeight; 
		}	
	
		@Override
    public void paint(Graphics g) { 
      super.paint(g);
      Dimension size = getSize();			
			int dz = Math.min(squareWidth(),squareHeight());
			int x0 = ((int)size.getWidth()-boardWidth*dz)/2;
			int y0 = ((int) size.getHeight()-boardHeight*dz)/2;	
			drawGrid(g,x0,y0,boardWidth*dz,boardHeight*dz,dz,dz);			
      for(int i = 0; i < boardHeight; ++i) {
        for(int j = 0; j < boardWidth; ++j) {
          Shape shape = shapeAt(j, boardHeight - i - 1);
          if(shape != ShapeGenerator.NO_SHAPE) {
            drawSquare(g,x0+j*dz,y0+i*dz,shape.color);
					}
        }
      }
      if(curShape != ShapeGenerator.NO_SHAPE) {
				for (int i = 0; i < curShape.dimension; ++i) {
					int x = curX+curShape.x(i);
					int y = curY+curShape.y(i);
					drawSquare(g,x0+x*dz,y0+(boardHeight-y-1)*dz,curShape.color);
				}
      }
    }				
		
    protected void drawSquare(Graphics g, int x, int y, Color color) {
			int dz = Math.min(squareWidth(),squareHeight());
			g.setColor(color);
      g.fillRect(x+1,y+1,dz-2,dz-2);
      g.setColor(color.brighter());
      g.drawLine(x,y+dz-1,x,y);
      g.drawLine(x,y,x+dz-1,y);
      g.setColor(color.darker());
      g.drawLine(x+1,y+dz-1,x+dz-1,y+dz-1);
      g.drawLine(x+dz-1,y+dz-1,x+dz-1,y+1);
    }
		
		protected void drawGrid(Graphics g, int x0, int y0, int width, int height, int dx, int dy) {				
			int x = x0;
			int y = y0;	
			int x1 = x0 + width;
			int y1 = y0 + height;
			g.setColor(Color.GRAY);
			for(int ind = 0; ind <= boardHeight; ind++) {
				g.drawLine(x0,y,x1,y);
				y+=dy;
			}
			for(int j = 0; j <= boardWidth; ++j) {
				g.drawLine(x,y0,x,y1);
				x+=dx;
			}		 			
		}
		
	}
	
	private class NextShapePanel extends DrawPanel {	
		private boolean activeFlag = false;

		public void setActiveFlag(boolean activeFlag){
			this.activeFlag = activeFlag;
		}
	
		@Override
		int squareWidth() {
			return ((int)getSize().getWidth()-2*boardMarginMinWidth)/shapeGenerator.getMaxWidth();
		}
		
		@Override
		int squareHeight() {
			return ((int)getSize().getHeight()-2*boardMarginMinHeight)/shapeGenerator.getMaxHeight();
		}		
				
		@Override
    public void paint(Graphics g)
    { 
      super.paint(g);
			clear(g);
			if(activeFlag) {
				Dimension size = getSize();
				int shapeXMin = nextShape.minX();
				int shapeYMin = nextShape.minY();
				int gridWidth = shapeGenerator.getMaxWidth();
				int gridHeight = shapeGenerator.getMaxHeight();
				int shapeWidth = nextShape.maxX() - shapeXMin +1;
				int shapeHeight = nextShape.maxY() - shapeYMin +1;			
				int shapeX0 = shapeXMin - (gridWidth - shapeWidth)/2;			
				int shapeY0 = shapeYMin - (gridHeight - shapeHeight)/2;
				int dz = Math.min(squareWidth(),squareHeight());
				int x0 = ((int)size.getWidth()-gridWidth*dz)/2;
				int y0 = ((int)size.getHeight()-gridHeight*dz)/2;
				//drawGrid(g,x0,y0,gridWidth*dz,gridHeight*dz,dz,dz);			
				for(int ind=0; ind<nextShape.dimension; ind++){
					drawSquare(g,(nextShape.coordinates[ind][0]-shapeX0)*dz+x0,(nextShape.coordinates[ind][1]-shapeY0)*dz+y0,nextShape.color); 
				}			
			}
    }		
		
		private void clear(Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0,0,(int)getSize().getWidth(),(int)getSize().getHeight());
		}							
	}			 
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public static interface Callback {
		public void call();
	}
	
	private String keys(BoardAction ba) {
		String result = "";
		List<Integer> startKeyCodeList = boardKeyActionMap.get(ba);
		for(int ind=0;ind<startKeyCodeList.size();ind++) {
			result +="'"; 
			result += KeyEvent.getKeyText(startKeyCodeList.get(ind).intValue());
			result +="'";
			if(ind<startKeyCodeList.size()-1) result +=", ";
		}
		return result;		
	}
		
}
