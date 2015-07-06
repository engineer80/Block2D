package block2D;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.awt.Color;
import java.util.Collections;

public class ShapeGenerator {
  public static final Shape NO_SHAPE = new Shape("No_Shape", 0, new Color(0,0,0), new int[0][],0,0,0,0,0,false,false,false);  
  private Map<Integer,List<Shape>> shapeGroups;
  private Integer[] shapeGroupIDs;
	private int[] shapeGroupsDistribution;
	private Random random;
	private Integer maxHeight;
	private Integer maxWidth;

	public ShapeGenerator(){
		shapeGroups = new HashMap<Integer,List<Shape>>();
		random = new Random();
	}

	public ShapeGenerator(List<Shape> shapes, boolean addFlipShapesFlag) {
		this();
		if(addFlipShapesFlag) addFlipShapes(shapes);
		deployShapes(shapes);
		setShapeGroupsDistribution(null);	
	}

	public void addFlipShapes(List<Shape> shapes) {
		List<Shape> flipShapes = new ArrayList<Shape>();
		for(Shape shape : shapes){
			Shape flipShape = getShape(shape).flipVertical();
			if(!(flipShape.verticalSymmetryFlag || flipShape.horizontalSymmetryFlag)) {
				flipShape.name += "_FLIP";
				flipShape.color = flipShape.color.darker();
			}
			flipShapes.add(flipShape);	
		}
		shapes.addAll(flipShapes);
	}

	public void deployShapes(List<Shape> shapes) {		
		List<Integer> shapeGroupIDList = new ArrayList<Integer>();
		for(Shape shape : shapes) {
			if(shapeGroups.containsKey(shape.groupID)){
				shapeGroups.get(shape.groupID).add(shape);
			}
			else {
				List<Shape> shapeGroup = new ArrayList<Shape>();
				shapeGroup.add(shape);
				shapeGroups.put(shape.groupID,shapeGroup);
				shapeGroupIDList.add(shape.groupID);				
			}
    }
		Collections.sort(shapeGroupIDList);
		shapeGroupIDs = shapeGroupIDList.toArray(new Integer[shapeGroupIDList.size()]);
		genMaxHeight();
		genMaxWidth();
	}

	public void setShapeGroupsDistribution(int[] shapeGroupsDistribution) {
		if(shapeGroupsDistribution!=null) {
			this.shapeGroupsDistribution=shapeGroupsDistribution;
		}
		else {
			List<Integer> shapeGroupsDistributionList = new ArrayList<Integer>();
			int sum = 0;	
			for(Integer groupID : shapeGroupIDs) {
				sum += shapeGroups.get(groupID).size();
				shapeGroupsDistributionList.add(new Integer(sum));
			}
			this.shapeGroupsDistribution = new int[shapeGroupsDistributionList.size()];
			for(int ind=0; ind<this.shapeGroupsDistribution.length; ind++) {
				this.shapeGroupsDistribution[ind] = shapeGroupsDistributionList.get(ind).intValue();
			}
		}
	}
	
	public int[] getShapeGroupsDistribution() {
		return shapeGroupsDistribution;
	}

  public Shape getShape(Integer shapeGroupID, int index) {
		return shapeGroups.get(shapeGroupID).get(index);	
  }

  public Shape getShape(Integer shapeGroupID) {
		int size = shapeGroups.get(shapeGroupID).size();
		int index = Math.abs(random.nextInt())%size;
		return getShape(shapeGroupID,index);			
	}	

  public Shape getShape() {
		int size = shapeGroupsDistribution.length;
		int choice = Math.abs(random.nextInt()%shapeGroupsDistribution[size-1]);
		int index;
		for(index=0; index<size; index++) {
			if(choice<shapeGroupsDistribution[index]) break;
		}
		Integer shapeGroupID = shapeGroupIDs[index];
		return getShape(shapeGroupID);
	}

	public static Shape getShape(Shape shape) {
		Shape result = null;
		try{
			result = shape.clone();
		}
		catch(Exception ex){}
		return result;		
	}		
	
	public Integer[] getShapeGroupIDs(){
		return shapeGroupIDs;
	}
	
	public int getMaxHeight() {
		if(maxHeight==null) {		 
			genMaxHeight();						
		}
		return maxHeight.intValue();
	}	
	
	public int genMaxHeight() {
		int max = 0;
		for(List<Shape> shapeGroup : shapeGroups.values()) {
			for(Shape shape : shapeGroup) {
				max = Math.max(max,shape.maxY()-shape.minY());
			}			
		}	
		maxHeight=new Integer(++max);		
		return max;
	}	
	
	public int getMaxWidth() {
		if(maxWidth==null) {		 
			genMaxWidth();						
		}
		return maxWidth.intValue();
	}				
	
	public int genMaxWidth() {
		int max = 0;
		for(List<Shape> shapeGroup : shapeGroups.values()) {
			for(Shape shape : shapeGroup) {
				max = Math.max(max,shape.maxX()-shape.minX());
			}			
		}	
		maxWidth=new Integer(++max);		
		return max;
	}	
		
}
