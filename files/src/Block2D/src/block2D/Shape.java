package block2D;
import java.awt.Color;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class Shape implements Cloneable {
	public String name;
	public int groupID;
	public Color color;
	public int[][] coordinates;
	public int dimension;
	public int minX;
	public int minY;
	public int maxX;
	public int maxY;
	public boolean rotationSymmetryFlag;
	public boolean verticalSymmetryFlag;
	public boolean horizontalSymmetryFlag;	
	
  public Shape(String name, int groupID, Color color, int[][] coordinates) {
		this.name = name;
		this.groupID = groupID;
		this.color = color;
		this.coordinates = coordinates;
		this.dimension = this.coordinates.length;		
		genMinMax();
		centering();		
		genRotationSymmetryFlag();
		genVerticalSymmetryFlag(); 
		genHorizontalSymmetryFlag(); 				
  }

  public Shape(String name, int groupID, Color color, int[][] coordinates, int dimension, int minX, int maxX, int minY, int maxY, boolean rotationSymmetryFlag, boolean verticalSymmetryFlag, boolean horizontalSymmetryFlag) {
		this.name = name;
		this.groupID = groupID;
		this.color = color;
		this.coordinates = coordinates;
		this.dimension = dimension;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.rotationSymmetryFlag = rotationSymmetryFlag;
		this.verticalSymmetryFlag = verticalSymmetryFlag;
		this.horizontalSymmetryFlag = horizontalSymmetryFlag; 
  }
	
	@Override
	public Shape clone() throws CloneNotSupportedException {
		Shape result = (Shape)super.clone();	
		result.coordinates = this.coordinates.clone();
		for(int ind=0; ind<this.coordinates.length; ind++){
			result.coordinates[ind] = this.coordinates[ind].clone();
		}		
		result.color = new Color(this.color.getRGB());		
		return result;		
	}			
	
  private void setX(int index, int x) { 
		coordinates[index][0] = x; 
	}

  private void setY(int index, int y) { 
		coordinates[index][1] = y; 
	}
 
 	public int x(int index) { 
		return coordinates[index][0]; 
	}

  public int y(int index) { 
		return coordinates[index][1]; 
	}

	public int minX() {
		return minX;
	}
	
	public int minY() {
		return minY;		
	}
	
	public int maxX(){
		return maxX;
	}
	
	public int maxY() {
		return maxY;
	}
	
	private void genMinMax() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
    for (int ind=0; ind < dimension; ind++) {
			minX = Math.min(minX,coordinates[ind][0]);
      minY = Math.min(minY, coordinates[ind][1]);
			maxX = Math.max(maxX,coordinates[ind][0]);
			maxY = Math.max(maxY,coordinates[ind][1]);
    }		
	}	
	
  public Shape rotateLeft() {
   	if(!rotationSymmetryFlag) {
			float x0 = (maxX + minX)/2.0F;
			float y0 = (maxY + minY)/2.0F;
			int dx = (x0 <= 0) ? Math.round(x0 + y0) : (int)(x0 + y0);
			int dy = (y0 <= 0) ? Math.round(y0 - x0) : (int)(y0 - x0);			
			for (int ind = 0; ind < dimension; ++ind) {
				int x = x(ind);
				int y = y(ind);
				setX(ind,-y + dx);
				setY(ind, x + dy);
			}
			int old0 = minY;
			minY = minX + dy;
			int old1 = maxX;
			maxX = -old0 +dx;
			old0 = maxY;
			maxY = old1 + dy;
			minX = -old0 + dx;
		}
	  return this;
  }

  public Shape rotateRight() {
  	if(!rotationSymmetryFlag) {
			float x0 = (maxX + minX)/2.0F;
			float y0 = (maxY + minY)/2.0F;
			int dx = (x0 <= 0) ? Math.round(x0 - y0) : (int)(x0 - y0);
			int dy = (y0 <= 0) ? Math.round(y0 + x0) : (int)(y0 + x0);	
			for (int ind = 0; ind < dimension; ++ind) {
				int x = x(ind);
				int y = y(ind);
				setX(ind,y + dx);
				setY(ind,-x + dy);
			}
			int old0 = maxY;
			maxY = -minX + dy;
			int old1 = maxX;
			maxX = old0 + dx;
			old0 = minY;
			minY = -old1 + dy;
			minX = old0 + dx;			
		}
	  return this;
	}

	public Shape flipVertical() {
		if(!verticalSymmetryFlag) {
			int y0 = (int)(maxY + minY);
			for (int ind = 0; ind < dimension; ++ind) {
				int x = x(ind);
				int y = y(ind);
				setX(ind,x);
				setY(ind,-y+y0);
			}	
			int old = minY;
			minY = -maxY+y0;
			maxY = -old+y0;				
		}		
		return this;
	}

	public Shape flipHorizontal() {
		if(!horizontalSymmetryFlag) {
			int x0 = (int)(maxX + minX);
			for (int ind = 0; ind < dimension; ++ind) {
				int x = x(ind);
				int y = y(ind);
				setX(ind,-x+x0);
				setY(ind,y);
			}	
			int old = minX;
			minX = -maxX+x0;
			maxX = -old+x0;								
		}		
		return this;
	}

	private void genRotationSymmetryFlag() {
		rotationSymmetryFlag = false;
		try {
			Shape rotatedShape = this.clone().rotateRight();			
			for(int ind0=0; ind0 < dimension; ind0++) {
				rotationSymmetryFlag = false;
				for(int ind1=0; ind1 <dimension; ind1++) {
					if((rotatedShape.x(ind0)==this.x(ind1))&&(rotatedShape.y(ind0)==this.y(ind1))) {
						rotationSymmetryFlag = true;
						break;
					}
				}
				if(!rotationSymmetryFlag) break;
			}
		}
		catch(Exception ex){}
	}

	private void genVerticalSymmetryFlag() {			
		verticalSymmetryFlag = false;
		try {
			Shape verticalFlippedShape = this.clone().flipVertical();
			for(int ind0=0; ind0 < dimension; ind0++){
				verticalSymmetryFlag = false;
				for(int ind1=0; ind1 <dimension; ind1++){
					if((verticalFlippedShape.x(ind0)==this.x(ind1))&&(verticalFlippedShape.y(ind0)==y(ind1))){
						verticalSymmetryFlag = true;
						break;
					}
				}
				if(!verticalSymmetryFlag) break;
			}
		}
		catch(Exception ex){}		
	}
	
	private void genHorizontalSymmetryFlag() {			
		horizontalSymmetryFlag = false;
		try{
			Shape horizontalFlippedShape = this.clone().flipHorizontal();
			for(int ind0=0; ind0 < dimension; ind0++){
				horizontalSymmetryFlag = false;
				for(int ind1=0; ind1 <dimension; ind1++){
					if((horizontalFlippedShape.x(ind0)==this.x(ind1))&&(horizontalFlippedShape.y(ind0)==this.y(ind1))){
						horizontalSymmetryFlag = true;
						break;
					}
				}
				if(!horizontalSymmetryFlag) break;
			}
		}
		catch(Exception ex){}
	}	
	
	@Override
	public String toString() {		
		String result="";
		result+="\""+name+"\"";
		result+=",";
		result+=new Integer(groupID).toString();
		result+=",";
		result+="{";
		result+=new Integer(color.getRed()).toString();
		result+=",";
		result+=new Integer(color.getGreen()).toString();
		result+=",";
		result+=new Integer(color.getBlue()).toString();
		result+="}";
		result+=",";
		result+="{";
		for(int row=0; row<coordinates.length; row++){
			result+="{";
			result+=coordinates[row][0];
			result+=",";
			result+=coordinates[row][1];
			result+="}";
			if(row<coordinates.length-1)
				result+=",";
		}
		result+="}";		
		return result;
	}

	public static Shape genShape(String str) {
		Scanner scanner = null; 
		try {
			scanner = new Scanner(str);
			String template = "[ ]*[\\\"]([^\\,\\\"]+)[\\\"][ ]*[\\,][ ]*([\\d]+)[^\\,]*[\\,][^\\{]*[\\{][ ]*([\\d]+)[^\\,]*[\\,][ ]*([\\d]+)[^\\,]*[\\,][ ]*([\\d]+)[^\\}]*[\\}]";
			if(scanner.findInLine(template) == null) return null;
			MatchResult mr = scanner.match();
			int ind = 0;
			String name = mr.group(++ind);
			int groupID = Integer.parseInt(mr.group(++ind));
			int red = Integer.parseInt(mr.group(++ind));
			int green = Integer.parseInt(mr.group(++ind));
			int blue = Integer.parseInt(mr.group(++ind));
			Color color = new Color(red,green,blue);			
			Pattern p = Pattern.compile("[\\{][ ]*([\\-]?\\d+)[ ]*,[ ]*([\\-]?\\d+)[ ]*[\\}]");
			List<Integer[]> coordinateList = new ArrayList<Integer[]>();
			while(scanner.findInLine(p) != null){
				mr = scanner.match();
				Integer[] c = new Integer[2];
				c[0] = Integer.parseInt(mr.group(1));
				c[1] = Integer.parseInt(mr.group(2));
				coordinateList.add(c);
			}
			int[][] coordinates = new int[coordinateList.size()][2];
			for(ind=0; ind<coordinates.length; ind++) {
				coordinates[ind][0]=coordinateList.get(ind)[0].intValue();
				coordinates[ind][1]=coordinateList.get(ind)[1].intValue();
			}			
			return new Shape(name,groupID,color,coordinates);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		finally {
			try {
				scanner.close();
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	private void centering(){
		int dx = (int)((maxX + minX)/2.0F);
		int dy = (int)((maxY + minY)/2.0F);				
		for(int ind=0; ind<coordinates.length; ind++) {
			coordinates[ind][0]-=dx;
			coordinates[ind][1]-=dy;
		}
		minX-=dx;
		minY-=dy;
		maxX-=dx;
		maxY-=dy;
	}
}	
