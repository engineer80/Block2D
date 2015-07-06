package block2D;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.text.ParseException;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;
import javax.swing.JComponent;


class Configurator extends JPanel {
	public static enum FieldID {BOARD_WIDTH, BOARD_HEIGHT, INITIAL_DELAY, LEVEL_NUM_LINES, LEVEL_TIME_DELAY, ALLOW_FLIP_FLAG, GEN_FLIPPED_FLAG, SHOW_NEXT_FLAG};
	private Map<FieldID,Integer> intFieldMap = null;
	private Map<FieldID,Boolean> boolFieldMap = null;
	private ShapeGenerator shapeGenerator;
	private List<Shape> shapes = null;
	private int[][] shapeGroupsDistributionData = null;
	private String confFileName = null;	
	private String shapesFileName = null;
	
	ModifySpinner colsSpinner = null;
	ModifySpinner rowsSpinner = null; 	
	ModifySpinner initialDelaySpinner = null;
	ModifySpinner levelTimeDelaySpinner = null;
	ModifySpinner levelNumLinesSpinner = null;
	
	ModifyCheckBox allowFlipCheckBox = null;
	ModifyCheckBox genFlippedCheckBox = null;	
	ModifyCheckBox showNextCheckBox = null;	

	ModifyTable shapeGroupsDistributionTable = null;

	Callback callback = null;

	public Configurator(){
		genDefaultValue();
		initUI();
	}
	
	public Integer getBoardWidth() {
		return intFieldMap.get(FieldID.BOARD_WIDTH);
	}
	public Integer getBoardHeight() {
		return intFieldMap.get(FieldID.BOARD_HEIGHT);
	}
	public Integer getInitialDelay() {
		return intFieldMap.get(FieldID.INITIAL_DELAY);
	}
	public Integer getLevelNumLines() {
		return intFieldMap.get(FieldID.LEVEL_NUM_LINES);
	}	
	public Integer getLevelTimeDelay() {
		return intFieldMap.get(FieldID.LEVEL_TIME_DELAY);
	}	
	public Boolean getAllowFlipFlag() {
		return boolFieldMap.get(FieldID.ALLOW_FLIP_FLAG);
	}
	public Boolean getGenFlippedFlag() {
		return boolFieldMap.get(FieldID.GEN_FLIPPED_FLAG);
	}	
	public Boolean getShowNextFlag() {
		return boolFieldMap.get(FieldID.SHOW_NEXT_FLAG);
	}
	public ShapeGenerator getShapeGenerator() {
		shapeGenerator = new ShapeGenerator(shapes,boolFieldMap.get(FieldID.GEN_FLIPPED_FLAG).booleanValue());	
		refresh();
		return shapeGenerator;
	}
	
	public void genDefaultValue() {
		intFieldMap = new HashMap<FieldID,Integer>(){{
			put(FieldID.BOARD_WIDTH,new Integer(12));
			put(FieldID.BOARD_HEIGHT,new Integer(22));
			put(FieldID.INITIAL_DELAY,new Integer(400));
			put(FieldID.LEVEL_NUM_LINES,new Integer(5));
			put(FieldID.LEVEL_TIME_DELAY,new Integer(25));
		}};	
		boolFieldMap = new HashMap<FieldID,Boolean>(){{
			put(FieldID.ALLOW_FLIP_FLAG,new Boolean(true));
			put(FieldID.GEN_FLIPPED_FLAG,new Boolean(false));
			put(FieldID.SHOW_NEXT_FLAG,new Boolean(true));
		}};
		shapes = new ArrayList<Shape>() {{
			add(new Shape("I_1", 1, new Color(0,0,0), new int[][] {{0,0}}));
			add(new Shape("I_2", 2, new Color(0,102,204), new int[][] {{0,0},{1,0}}));
			add(new Shape("I_3", 3, new Color(0,102,204), new int[][] {{-1,0},{0,0},{1,0}}));
			add(new Shape("L_3", 3, new Color(255,128,0), new int[][] {{0,0},{0,1},{1,0}}));
			add(new Shape("O_4", 4, new Color(255,255,0), new int[][] {{0,0},{0,1},{1,0},{1,1}}));		
			add(new Shape("I_4", 4, new Color(0,102,204), new int[][] {{-1,0},{0,0},{1,0},{2,0}}));
			add(new Shape("S_4", 4, new Color(204,0,0), new int[][] {{-1,0},{0,0},{0,1},{1,1}}));
			add(new Shape("L_4", 4, new Color(255,128,0), new int[][] {{-1,0},{0,0},{1,0},{1,1}}));
			add(new Shape("T_4", 4, new Color(102,0,51), new int[][] {{0,-1},{-1,0},{0,0},{1,0}}));
			add(new Shape("X_5", 5, new Color(255,0,0), new int[][] {{0,-1},{-1,0},{0,0},{1,0},{0,1}}));
			add(new Shape("N_5", 5, new Color(0,0,255), new int[][] {{0,-2},{0,-1},{0,0},{1,0},{1,1}}));			
			add(new Shape("F_5", 5, new Color(255,255,0), new int[][] {{0,-1},{0,0},{-1,0},{-1,1},{1,0}}));
			add(new Shape("T_5", 5, new Color(0,255,255), new int[][] {{0,-1},{0,0},{-1,1},{0,1},{1,1}}));
			add(new Shape("P_5", 5, new Color(0,255,0), new int[][] {{0,-1},{0,0},{0,1},{1,0},{1,1}}));
			add(new Shape("L_5", 5, new Color(255,128,0), new int[][] {{-2,0},{-1,0},{0,0},{1,0},{1,1}}));
			add(new Shape("I_5", 5, new Color(204,204,255), new int[][] {{-2,0},{-1,0},{0,0},{1,0},{2,0}}));
			add(new Shape("U_5", 5, new Color(255,153,255), new int[][] {{-1,0},{0,0},{1,0},{-1,1},{1,1}}));
			add(new Shape("V_5", 5, new Color(0,153,153), new int[][] {{0,0},{1,0},{2,0},{0,1},{0,2}}));
			add(new Shape("W_5", 5, new Color(0,204,0), new int[][] {{0,-1},{1,-1},{0,0},{-1,0},{-1,1}}));
			add(new Shape("Y_5", 5, new Color(153,0,153), new int[][] {{0,-2},{0,-1},{0,0},{-1,0},{0,1}}));
			add(new Shape("Z_5", 5, new Color(204,204,0), new int[][] {{0,-1},{1,-1},{0,0},{0,1},{-1,1}}));	
		}};  			
		shapeGenerator = new ShapeGenerator(shapes,true);
		genShapeGroupsDistributionData();
	}

	public void setConfFileName(String confFileName) {
		this.confFileName = confFileName;		
	}
	
	public void setShapesFileName(String shapesFileName) {
		this.shapesFileName = shapesFileName;		
	}	

	public void load() {
		if(loadShapes()) {
			shapeGenerator = new ShapeGenerator(shapes,false);
		}
		if(!loadSettings() || !checkShapeGroupsDistributionData()) {
			commit();		
			genShapeGroupsDistributionData();				
		}
		refresh();		
	}
	
	public void save() {
		commit();
		shapeGenerator = new ShapeGenerator(shapes,boolFieldMap.get(FieldID.GEN_FLIPPED_FLAG).booleanValue());			
		refresh();
		saveShapes();	
		saveSettings();
	}
	
	public boolean loadSettings() {
		BufferedReader br=null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(confFileName)));
			String line;
			Pattern parametersPattern = Pattern.compile("[ ]*([^\\# ][^\\= ]*)[ ]*[\\=][ ]*([\\d]+|true|false)[ ]*");
			Pattern shapeGroupsDistributionPattern = Pattern.compile("[ ]*([\\d]+)[ ]*[\\=][ ]*([\\d]+)[\\%][ ]*");
			Set<FieldID> intFieldKeySet = intFieldMap.keySet();
			Set<FieldID> boolFieldlKeySet = boolFieldMap.keySet();			
			Map<Integer,Integer> shapeGroupsDistributionMap = new HashMap<Integer,Integer>();
			while((line=br.readLine())!=null){
				Matcher parametersMatcher = parametersPattern.matcher(line);		
				if(parametersMatcher.matches()){
					String name, value;
					if((name=parametersMatcher.group(1))!=null && (value=parametersMatcher.group(2))!=null && !name.isEmpty() && !value.isEmpty()){
						FieldID fid = FieldID.valueOf(name);
						if(intFieldKeySet.contains(fid)){
							intFieldMap.put(fid,Integer.parseInt(value));
						} 
						else if(boolFieldlKeySet.contains(fid)){
							boolFieldMap.put(fid,Boolean.parseBoolean(value));
						}
					}													
				} 
				else {		
					Matcher shapeGroupsDistributionMatcher = shapeGroupsDistributionPattern.matcher(line);
					if(shapeGroupsDistributionMatcher.matches()){
						String name, value;
						if((name=shapeGroupsDistributionMatcher.group(1))!=null && (value=shapeGroupsDistributionMatcher.group(2))!=null && !name.isEmpty() && !value.isEmpty()){	
							shapeGroupsDistributionMap.put(new Integer(name),new Integer(value));							
						}						
					}
				}				
			}				
			if(shapeGroupsDistributionMap.size()!=0){
				shapeGroupsDistributionData = new int[2][shapeGroupsDistributionMap.size()];
				int ind=0;
				for(Map.Entry<Integer,Integer> item : shapeGroupsDistributionMap.entrySet()){
					shapeGroupsDistributionData[0][ind]=item.getKey().intValue();
					shapeGroupsDistributionData[1][ind]=item.getValue().intValue();					
					ind++;
				}
			}
			return true;
		}
		catch(Exception ex){
			//System.out.println(ex.getMessage());
			return false;
		}
		finally {
			try {
				br.close();
			}
			catch(Exception ex) {
				//System.out.println(ex.getMessage());
			}
		}		
	}	
	
	public void saveSettings(){
		BufferedWriter bw = null;
		try {
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(confFileName)));
			bw.write("# Parameters\n");
			bw.write("# Name = Value\n\n");
			for(FieldID fieldID : intFieldMap.keySet()){
				bw.write(fieldID.name()+" = "+intFieldMap.get(fieldID)+"\n");
			}
			for(FieldID fieldID : boolFieldMap.keySet()){
				bw.write(fieldID.name()+" = "+boolFieldMap.get(fieldID)+"\n");
			}			
			bw.write("\n# Shape Groups Distribution\n");
			bw.write("# GroupID = P%\n\n");
			for(int ind=0; ind<shapeGroupsDistributionData[0].length; ind++){
				bw.write(shapeGroupsDistributionData[0][ind]+" = "+shapeGroupsDistributionData[1][ind]+"%\n");
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		finally {
			try {
				bw.close();
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}		
	}	
	
	private void saveShapes() {		
		BufferedWriter bw = null;
		try{
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(shapesFileName)));
			bw.write("# \"name\", groupID, {red, green, blue}, {{X1,Y1}, ..., {Xn,Yn}}\n\n");
			for(int ind=0; ind<shapes.size(); ind++){
				if(ind<shapes.size()-1)
					bw.write(shapes.get(ind).toString()+"\n");			
				else 
					bw.write(shapes.get(ind).toString());			
			}					
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}		
		finally {
			try{
				bw.close();
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
	}	
	
	private boolean loadShapes() {
		BufferedReader br = null;
		try {			
			br=new BufferedReader(new InputStreamReader(new FileInputStream(shapesFileName)));
			this.shapes = new ArrayList<Shape>();
			String line;
			while((line = br.readLine()) != null){
				Shape shape = Shape.genShape(line);
				if(shape!=null) this.shapes.add(shape);
			}
			return true;
		}
		catch(Exception ex) {
			//System.out.println(ex.getMessage());
			return false;
		}
		finally {
			try {
				br.close();
			}
			catch(Exception ex) {
				//System.out.println(ex.getMessage());
			}
		}
	}
	
	private void genShapeGroupsDistributionData() {				
		int[] shapeGroupsDistribution = shapeGenerator.getShapeGroupsDistribution();		
		Integer[] shapeGroupIDs = shapeGenerator.getShapeGroupIDs();		
		shapeGroupsDistributionData = new int[2][shapeGroupsDistribution.length];
		int previous = 0;
		int[] p = new int[shapeGroupsDistribution.length];
		for(int ind=0; ind<p.length; ind++) {
			p[ind]=shapeGroupsDistribution[ind]-previous;
			previous=shapeGroupsDistribution[ind];
		}
		p=normalization(p,100);
		for(int ind=0; ind<p.length; ind++){
			shapeGroupsDistributionData[0][ind]=shapeGroupIDs[ind].intValue();
			shapeGroupsDistributionData[1][ind]=p[ind];
		}
	}
		
	private void updateShapeGroupsDistribution() {
		int[] p = new int[shapeGroupsDistributionData[1].length];
		for(int ind=0;ind<p.length;ind++){
			p[ind]=shapeGroupsDistributionData[1][ind];
		}
		int[] q=normalization(p,100);
		if(q==null){			
			for(int ind=0;ind<p.length; ind++) p[ind]=1;
			q=normalization(p,100);
		}
		int sum=0;
		for(int ind=0; ind<p.length; ind++) {
			sum+=q[ind];
			p[ind]=sum;			
		}
		shapeGroupsDistributionData[1] = q;
		shapeGenerator.setShapeGroupsDistribution(p);		
	}
	
	private boolean checkShapeGroupsDistributionData() {
		boolean result = false;		
		Integer[] shapeGroupIDs = shapeGenerator.getShapeGroupIDs();
		if(shapeGroupsDistributionData != null && shapeGroupIDs != null && (shapeGroupsDistributionData[0].length == shapeGroupIDs.length)) {
			result=true;
			for(int ind=0; ind<shapeGroupIDs.length; ind++) {
				if(shapeGroupsDistributionData[0][ind]!=shapeGroupIDs[ind].intValue()) {
					result=false;
					break;
				}			
			}
		}
		return result;
	}
	
	private int[] normalization(int[] x, int newNorm) {		
		int oldNorm=0;
		for(int ind=0; ind<x.length; ind++) oldNorm+=x[ind];
		if(oldNorm<=0 || newNorm<=0) return null;
		int[][] remainder = new int[x.length][2];
		int[] result = new int[x.length];
		int sum=0;
		for(int ind=0; ind<x.length; ind++) {
			result[ind]=(x[ind]*newNorm)/oldNorm;
			sum+=result[ind];						
			int rem = (x[ind]*newNorm)%oldNorm;
			for(int ind1=0; ind1<x.length; ind1++) {
				if(rem>remainder[ind1][0]){
					for(int ind2=x.length-1; ind2>ind1; ind2--){
						remainder[ind2][0]=remainder[ind2-1][0];
						remainder[ind2][1]=remainder[ind2-1][1];						
					}
					remainder[ind1][0]=rem;
					remainder[ind1][1]=ind;
					break;
				}				
			}
		}
		sum=newNorm-sum;
		for(int ind=0; ind<remainder.length; ind++) {
			if(sum<=0) break;
			result[remainder[ind][1]]++;
			sum--;
		}
		return result;
	}
	
	private void sortGroupsDistributionData() {
		int[] sortIndex = new int[shapeGroupsDistributionData[0].length];
		for(int ind=0; ind<sortIndex.length; ind++) sortIndex[ind]=ind;
		boolean shiftFlag = true;
		while(shiftFlag) {
			shiftFlag = false;
			for(int ind=1; ind<sortIndex.length; ind++) {
				if(shapeGroupsDistributionData[0][sortIndex[ind-1]]>shapeGroupsDistributionData[0][sortIndex[ind]]) {
					int x = sortIndex[ind];
					sortIndex[ind]=sortIndex[ind-1];
					sortIndex[ind-1]=x;
					shiftFlag=true;
				}
			}
		}
		int[][] newShapeGroupsDistributionData = new int[2][sortIndex.length];
		for(int ind=0; ind<sortIndex.length; ind++) {
			newShapeGroupsDistributionData[0][ind]=shapeGroupsDistributionData[0][sortIndex[ind]];
			newShapeGroupsDistributionData[1][ind]=shapeGroupsDistributionData[1][sortIndex[ind]];
		}
		shapeGroupsDistributionData	= newShapeGroupsDistributionData;
	}	
		
	private void initUI() {
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));			
		BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(bl);				
		{
			JPanel boardSizePanel = new JPanel();
			GroupLayout gl = new GroupLayout(boardSizePanel);
			boardSizePanel.setLayout(gl);		
			boardSizePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Board size"),
				BorderFactory.createEmptyBorder(0,0,0,0)));		
			JLabel colsLabel = new JLabel("Cols ");		
			JLabel rowsLabel = new JLabel("Rows ");		
			colsSpinner = new ModifySpinner(FieldID.BOARD_WIDTH,1,100,1);
			colsSpinner.setToolTipText("<html>Columns<br>of board</html>");
			rowsSpinner = new ModifySpinner(FieldID.BOARD_HEIGHT,1,100,1);
			rowsSpinner.setToolTipText("<html>Rows<br>of board</html>");						
			gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(colsLabel).addComponent(rowsLabel)).				
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(colsSpinner).addComponent(rowsSpinner)));				
			gl.setVerticalGroup(gl.createSequentialGroup().
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(colsLabel).addComponent(colsSpinner)).
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(rowsLabel).addComponent(rowsSpinner)));					
			this.add(boardSizePanel);															
		}		
		{
			JPanel levelParamsPanel = new JPanel();
			GroupLayout gl = new GroupLayout(levelParamsPanel);
			levelParamsPanel.setLayout(gl);		
			levelParamsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Level params"),
				BorderFactory.createEmptyBorder(0,0,0,0)));		
			JLabel initialDelayLabel = new JLabel("Init delay ");					
			JLabel levelDelayLabel = new JLabel("Level delay ");		
			JLabel linesLabel = new JLabel("Lines ");		
			
			initialDelaySpinner = new ModifySpinner(FieldID.INITIAL_DELAY,1,10000,1);
			initialDelaySpinner.setToolTipText("<html>Init next shape<br>delay (ms)</html>");					
			levelTimeDelaySpinner = new ModifySpinner(FieldID.LEVEL_TIME_DELAY,0,10000,1);					
			levelTimeDelaySpinner.setToolTipText("<html>Level delay<br>difference (ms)</html>");	
			levelNumLinesSpinner = new ModifySpinner(FieldID.LEVEL_NUM_LINES,1,intFieldMap.get(FieldID.BOARD_HEIGHT),1);
			levelNumLinesSpinner.setToolTipText("<html>Full lines<br>per level</html>");

			gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(initialDelayLabel).addComponent(levelDelayLabel).addComponent(linesLabel)).				
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(initialDelaySpinner).addComponent(levelTimeDelaySpinner).addComponent(levelNumLinesSpinner)));				
					
			gl.setVerticalGroup(gl.createSequentialGroup().
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(initialDelayLabel).addComponent(initialDelaySpinner)).
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(levelDelayLabel).addComponent(levelTimeDelaySpinner)).
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(linesLabel).addComponent(levelNumLinesSpinner)));			
			this.add(levelParamsPanel);				
		}		
		{
			JPanel gameOptionPanel = new JPanel();
			GroupLayout gl = new GroupLayout(gameOptionPanel);
			gameOptionPanel.setLayout(gl);		
			gameOptionPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Game options"),
				BorderFactory.createEmptyBorder(0,0,0,0)));
				
			allowFlipCheckBox = new ModifyCheckBox(FieldID.ALLOW_FLIP_FLAG,"Allow flip");
			allowFlipCheckBox.setToolTipText("<html>Allow flip shape<br>(\'z\' \'x\')</br>");
			genFlippedCheckBox = new ModifyCheckBox(FieldID.GEN_FLIPPED_FLAG,"Gen flipped");
			genFlippedCheckBox.setToolTipText("<html>Flipped shape<br>autogeneration</html>");
			showNextCheckBox = new ModifyCheckBox(FieldID.SHOW_NEXT_FLAG,"Show next");
			showNextCheckBox.setToolTipText("<html>Show<br>next shape</html>");
						
			gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(allowFlipCheckBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(genFlippedCheckBox)).
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).										
					addComponent(showNextCheckBox)));									
			gl.setVerticalGroup(gl.createSequentialGroup().
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(allowFlipCheckBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(showNextCheckBox)).
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(genFlippedCheckBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));						
			this.add(gameOptionPanel);															
		}		
		{
			JPanel shapeGroupsDistributionPanel = new JPanel();
			GroupLayout gl = new GroupLayout(shapeGroupsDistributionPanel);
			shapeGroupsDistributionPanel.setLayout(gl);		
			shapeGroupsDistributionPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Shape distribution"),
				BorderFactory.createEmptyBorder(0,0,0,0)));		
			shapeGroupsDistributionTable = new ModifyTable(new String[]{"GroupID","P(%)"});
			shapeGroupsDistributionTable.setToolTipText("<html>Probability distribution<br>of shape groups</html>");
			JScrollPane sp = new JScrollPane(shapeGroupsDistributionTable);
			shapeGroupsDistributionTable.setPreferredScrollableViewportSize(new Dimension(250, 100));			
			shapeGroupsDistributionPanel.add(sp);			
			
			shapeGroupsDistributionTable.setCellSelectionEnabled(true);
			shapeGroupsDistributionTable.setEditingColumn(1);
			shapeGroupsDistributionTable.setEditingRow(2); 
			gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(sp)));				
			gl.setVerticalGroup(gl.createSequentialGroup().
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(sp)));					
			this.add(shapeGroupsDistributionPanel);															
		}			
		{
			JPanel buttonPanel = new JPanel();
			GroupLayout gl = new GroupLayout(buttonPanel);
			buttonPanel.setLayout(gl);		
			buttonPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(0,0,0,0)));		
			ActionListener saveActionListener = new SaveActionListener();							
			JButton saveButton = new JButton("Save");
			saveButton.setMargin(new Insets(2,2,2,2));
			saveButton.addActionListener(saveActionListener);
			ActionListener loadActionListener = new LoadActionListener();
			JButton loadButton = new JButton("Load");
			loadButton.setMargin(new Insets(2,2,2,2));
			loadButton.addActionListener(loadActionListener);
			DefaultActionListener defaultActionListener = new DefaultActionListener();
			JButton defaultButton = new JButton("Default");			
			defaultButton.setMargin(new Insets(2,2,2,2));
			defaultButton.addActionListener(defaultActionListener);			
			OKActionListener oKActionListener = new OKActionListener();	
			JButton oKButton = new JButton("Ok");
			oKButton.setMargin(new Insets(2,2,2,2));
			oKButton.addActionListener(oKActionListener);							
		
			gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(saveButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).				
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(loadButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(defaultButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).
				addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).
					addComponent(oKButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));				
					
			gl.setVerticalGroup(gl.createSequentialGroup().
			addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
				addComponent(saveButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
				addComponent(loadButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
				addComponent(defaultButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
				addComponent(oKButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));					
																				
			this.add(buttonPanel);															
		}					
	}

	private class ModifySpinner extends JSpinner {
		FieldID fieldID;
		ModifySpinner(FieldID fieldID, int min, int max, int step) {			
			super(new SpinnerNumberModel(intFieldMap.get(fieldID).intValue(),min,max,step));
			this.fieldID = fieldID; 
		}
		void commit() {
			try {
				this.commitEdit();
				intFieldMap.put(fieldID,(Integer)this.getValue());	
			}
			catch (ParseException pe) {
				JComponent editor = this.getEditor();
				if (editor instanceof DefaultEditor) {
					((DefaultEditor)editor).getTextField().setValue(this.getValue());
				}
			}
		}
		void refresh() {
			this.setValue(intFieldMap.get(fieldID).intValue());
		}								
	} 
	
	private class ModifyCheckBox extends JCheckBox {
		FieldID fieldID;
		ModifyCheckBox(FieldID fieldID, String label)	{
			super(label,boolFieldMap.get(fieldID).booleanValue());
			this.fieldID = fieldID;
		}
		void commit() {
			boolFieldMap.put(fieldID,new Boolean(this.isSelected()));
		}
		void refresh() {
			this.setSelected(boolFieldMap.get(fieldID).booleanValue());
		}
	}
	
	private class ModifyTable extends JTable {
		String[] columnNames;
		private class ModifyTableModel extends AbstractTableModel {
			@Override 
			public int getColumnCount() {
				return 2;
			}
			@Override
			public int getRowCount() {
				return shapeGroupsDistributionData[0].length;
			}
			@Override
			public Object getValueAt(int row, int column) {
				return shapeGroupsDistributionData[column][row];
			}
			@Override
			public void setValueAt(Object value, int row, int column) {
				shapeGroupsDistributionData[column][row] = Integer.parseInt((String)value);
			}			
			@Override
			public String getColumnName(int column) {
				return columnNames[column];
			}								
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column == 0) {
					return false;
				}
				return true;
			}				
		};		
		ModifyTable(String[] columnNames) {
			super();
			this.columnNames = columnNames;			
			this.setModel(new ModifyTableModel());
		}		
		void commit(){		
		}
		void refresh(){
			this.resizeAndRepaint();
		}				
	}	

	private void commit(){
		colsSpinner.commit();
		rowsSpinner.commit();
		initialDelaySpinner.commit();
		levelTimeDelaySpinner.commit();
		levelNumLinesSpinner.commit();
		allowFlipCheckBox.commit();
		genFlippedCheckBox.commit();
		showNextCheckBox.commit();		
		shapeGroupsDistributionTable.commit();
	} 

	private void refresh(){
		colsSpinner.refresh();
		rowsSpinner.refresh();
		initialDelaySpinner.refresh();
		levelTimeDelaySpinner.refresh();
		levelNumLinesSpinner.refresh();
		allowFlipCheckBox.refresh();
		genFlippedCheckBox.refresh();
		showNextCheckBox.refresh();		
		updateShapeGroupsDistribution();
		shapeGroupsDistributionTable.refresh();		
	}

	private class SaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			save();
		}
	}	
	
	private class LoadActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			load();
		}
	}		

	private class DefaultActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			genDefaultValue();
			refresh();
		}
	}	
	
	private class OKActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {			
			commit();
			shapeGenerator = new ShapeGenerator(shapes,boolFieldMap.get(FieldID.GEN_FLIPPED_FLAG).booleanValue());			
			refresh();	
			callback.call();
		}
	}
	
	public static interface Callback {	
		public void call();
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	/*public static void main(String args[]){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Configurator configurator = new Configurator();
				configurator.setConfFileName("conf.ser");
				configurator.setShapesFileName("shapes.txt");				
				//configurator.pack();
				configurator.setVisible(true);
			}
    });
	}*/
}
