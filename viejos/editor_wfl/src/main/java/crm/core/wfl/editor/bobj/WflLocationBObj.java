package crm.core.wfl.editor.bobj;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CM
 */
public class WflLocationBObj extends WflBObj {

    private String id;
    private Point point;
    private List<Point> vertices = new ArrayList<Point>();
    
    /**
	 * Devuelve el/la vertices.
	 * @return el/la vertices.
	 */
	public List<Point> getVertices() {
		return vertices;
	}

	/**
	 * Setea el/la vertices.
	 * @param vertices El/la vertices a setear.
	 */
	public void setVertices(List<Point> vertices) {
		this.vertices = vertices;
	}

	public String getBObjId()
    {
        return getId();
    }
    
    public WflLocationBObj(String id, Point point)
    {
        this.id = id;
        this.point = point;
    }
    
    public WflLocationBObj(String id, Point point, List<Point> vertices)
    {
        this.id = id;
        this.point = point;
        this.vertices = vertices;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
}
