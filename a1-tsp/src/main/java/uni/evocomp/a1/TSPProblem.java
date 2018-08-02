package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

// TODO : See whether we need to have this actually handle the IO, or whether IO
// is separate and gets passed to this.
public class TSPProblem
{
	private String				name;
	private String				comment;
	private String				type;			// tour or TSP
	private String				edgeWeightType;	// For this assignment, will always be EUC_2D, but it can be EUC_3D
	private List<List<Double>>	weights;

	public TSPProblem()
	{
		;
	}

	/**
	 * Copy constructor for TSPProblem
	 * 
	 * @param src
	 *            source TSPProblem object to copy construct
	 */
	public TSPProblem(TSPProblem src)
	{
		this.name = new String(src.getName());
		this.comment = new String(src.getComment());
		this.type = new String(src.getType());
		this.edgeWeightType = new String(src.getEdgeWeightType());
		this.weights = new ArrayList<>(src.getWeights());
	}

	public TSPProblem(String name, String comment, String type,
			String edgeWeightType, List<List<Double>> weights)
	{
		this.name = name;
		this.comment = comment;
		this.type = type;
		this.edgeWeightType = edgeWeightType;
		this.weights = weights;
	}

	public String getName()
	{
		return name;
	}

	public String getComment()
	{
		return comment;
	}

	public String getType()
	{
		return type;
	}

	public String getEdgeWeightType()
	{
		return edgeWeightType;
	}

	public List<List<Double>> getWeights()
	{
		return weights;
	}

	/**
	 * @return the number of cities/towns/nodes in the problem
	 */
	public int getSize()
	{
		return weights.size();
	}
}
