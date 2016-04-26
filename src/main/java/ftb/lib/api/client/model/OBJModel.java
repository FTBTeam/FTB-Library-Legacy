package ftb.lib.api.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.client.GlStateManager;
import latmod.lib.LMStringUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by LatvianModder
 */
@SideOnly(Side.CLIENT)
public class OBJModel
{
	public final List<Face> totalFaces;
	public final List<Vector3f> vertices;
	public final List<Vector3f> vertexNormals;
	public final List<Vector3f> texVertices;
	public final Map<String, Group> groups;
	
	private Group current = null;
	public double sizeV = 0D;
	
	protected OBJModel()
	{
		totalFaces = new ArrayList<>();
		vertices = new ArrayList<>();
		vertexNormals = new ArrayList<>();
		texVertices = new ArrayList<>();
		groups = new HashMap<>();
	}
	
	public static OBJModel load(ResourceLocation rl) throws Exception
	{
		return OBJModel.load(FTBLibClient.mc.getResourceManager().getResource(rl).getInputStream());
	}
	
	public static OBJModel load(InputStream is) throws Exception
	{
		OBJModel m = new OBJModel();
		
		double minSizeV = Double.POSITIVE_INFINITY;
		double maxSizeV = Double.NEGATIVE_INFINITY;
		
		for(String s : LMStringUtils.readStringList(is))
		{
			if(!s.isEmpty() && s.charAt(0) != '#')
			{
				String[] s3 = s.split(" ");
				
				switch(s3[0])
				{
					case "o":
					{
						Group g = new Group(m, s3[1]);
						if(m.current != null)
						{
							m.groups.put(m.current.getID(), m.current);
						}
						m.current = g;
						break;
					}
					case "g":
						break;
					case "s":
						break;
					case "mtllib":
						break;
					case "usemtl":
						break;
					case "v":
					{
						float x = Float.parseFloat(s3[1]);
						float y = Float.parseFloat(s3[2]);
						float z = Float.parseFloat(s3[3]);
						m.vertices.add(new Vector3f(x, y, z));
						
						if(y < minSizeV) minSizeV = y;
						if(y > maxSizeV) maxSizeV = y;
						break;
					}
					case "vn":
					{
						float x = Float.parseFloat(s3[1]);
						float y = Float.parseFloat(s3[2]);
						float z = Float.parseFloat(s3[3]);
						m.vertexNormals.add(new Vector3f(x, y, z));
						break;
					}
					case "vt":
					{
						if(s3.length == 3)
						{
							float x = Float.parseFloat(s3[1]);
							float y = Float.parseFloat(s3[2]);
							m.texVertices.add(new Vector3f(x, 1F - y, -1F));
						}
						else if(s3.length == 4)
						{
							float x = Float.parseFloat(s3[1]);
							float y = Float.parseFloat(s3[2]);
							float z = Float.parseFloat(s3[3]);
							m.texVertices.add(new Vector3f(x, 1F - y, z));
						}
						
						break;
					}
					case "f":
					{
						if(m.current == null) m.current = new Group(m, "Default");
						
						Face f = Face.parseFace(m, s, s3);
						if(f != null)
						{
							m.current.faces.add(f);
							m.totalFaces.add(f);
						}
						break;
					}
				}
			}
		}
		
		//if(!m.groups.contains(m.current));
		m.groups.put(m.current.getID(), m.current);
		m.sizeV = maxSizeV - minSizeV;
		
		if(m != null && m.texVertices != null)
		{
			GlStateManager.enableTexture2D();
		}
		else
		{
			GlStateManager.disableTexture2D();
		}
		
		m.renderAll();
		
		return m;
	}
	
	public void renderAll()
	{
		for(Group g : groups.values())
		{
			g.render();
		}
	}
	
	public void render(String s)
	{
		Group g = getGroup(s);
		if(g != null)
		{
			g.render();
		}
	}
	
	public Group getGroup(String s)
	{ return groups.get(s); }
}