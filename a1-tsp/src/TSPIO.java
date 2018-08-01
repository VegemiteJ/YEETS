import java.io.File;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSPIO
{
	TSPIO()
	{
		;
	}

	/**
	 * <pre>
	 * NAME : eil51
	 * COMMENT : 51-city problem (Christofides/Eilon)
	 * TYPE : TSP
	 * DIMENSION : 51
	 * EDGE_WEIGHT_TYPE : EUC_2D
	 * NODE_COORD_SECTION
	 * 1 37 52
	 * ...
	 * EOF
	 * </pre>
	 * 
	 * @param file
	 *            the TSP problem file to read
	 * @return a TSPProblem object with the weights initialised
	 * @throws IOException
	 */
	public TSPProblem read(Reader r) throws IOException, NullPointerException
	{
		// Read each line of the reader into a string
		try (BufferedReader br = new BufferedReader(r))
		{
			boolean header = true; // 1 for header, 0 for body
			String line = new String();
			String name = new String();
			String comment = new String();
			String type = new String();
			String edgeWeightType = new String();
			int dimension = 0;
			Map<Integer, Point> points = new HashMap<>();
			List<List<Double>> weights = new ArrayList<>();
			for (int i = 1; (line = br.readLine()) != null
					|| line.trim().equals("EOF"); i++)
			{
				// Read header
				if (header)
				{
					String[] split = line.trim().split(" : ");
					switch (split[0])
					{
					case "NAME":
						if (split.length < 2)
						{
							throw new IOException("Bad " + split[0]
									+ " format on line " + i + ": " + line);
						}
						name = split[1];
						break;
					case "COMMENT":
						if (split.length < 2)
						{
							throw new IOException("Bad " + split[0]
									+ " format on line " + i + ": " + line);
						}
						comment = split[1];
						break;
					case "TYPE":
						if (split.length < 2)
						{
							throw new IOException("Bad " + split[0]
									+ " format on line " + i + ": " + line);
						}
						type = split[1];
						break;
					case "DIMENSION":
						if (split.length < 2)
						{
							throw new IOException("Bad " + split[0]
									+ " format on line " + i + ": " + line);
						}
						dimension = Integer.parseInt(split[1]);
						//						points = new ArrayList<>(dimension);
						break;
					case "EDGE_WEIGHT_TYPE":
						if (split.length < 2)
						{
							throw new IOException("Bad " + split[0]
									+ " format on line " + i + ": " + line);
						}
						edgeWeightType = split[1];
						break;
					case "NODE_COORD_SECTION":
						header = false;
						break;
					case "EOF":
						break;
					default:
						break;
					}
				}
				else // read body
				{
					if (line.equals("EOF"))
					{
						break;
					}

					String[] split = line.trim().split(" "); // should be {n, x, y}
					if (split.length < 3)
					{
						throw new IOException(
								"Bad body format on line " + i + ": " + line);
					}
					points.put(Integer.parseInt(split[0]),
							new Point(Integer.parseInt(split[1]),
									Integer.parseInt(split[2])));
				}
			}

			// Finished reading lines, construct weights based on points
			for (int i = 1; i <= dimension; i++)
			{
				List<Double> temp = new ArrayList<>();
				for (int j = 1; j <= dimension; j++)
				{
					temp.add(euclideanDistance2D(points.get(i), points.get(j)));
				}
				weights.add(temp);
			}
			return new TSPProblem(name, comment, type, edgeWeightType, weights);
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return the 2D euclidean distance between a and b
	 */
	private double euclideanDistance2D(Point a, Point b)
	{
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2)
				+ Math.pow(a.getY() - b.getY(), 2));
	}

	// TODO : Fill in after we have Instance
	// TODO : Figure out whether we even need this function
	/**
	 * @param instance
	 *            the instance object to write
	 */
	public void write()
	{
		System.out.println("Writing instance");
	}
}
