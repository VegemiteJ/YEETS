import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to store a single trial/solution of the TSP problem.
 * <p>
 * As such, it should contain a Set of numbers, where each number is a city,
 * representing a permutation of all the cities (from 1-n)
 * 
 * @author Namdrib
 *
 */
public class Individual
{
	List<Integer> genotype; // the tour, elements should be 1-n

	public Individual()
	{
		genotype = new ArrayList<>();
		initialise(0);
	}

	/**
	 * Copy constructor for Individual
	 * 
	 * @param src
	 *            source Individual object to copy construct
	 */
	public Individual(Individual src)
	{
		this.genotype = new ArrayList<>(src.getGenotype());
	}

	/**
	 * 
	 * @param n
	 *            initialise to have a tour of n cities
	 */
	public Individual(int n)
	{
		initialise(n);
	}

	public Individual(List<Integer> genotype)
	{
		this.genotype = genotype;
	}

	/**
	 * Initialise as per Exercise 3, "constructs .. a solution [uniformly at
	 * random] in linear time"
	 * 
	 * @param n
	 *            the size of the genotype
	 */
	public void initialise(int n)
	{
		genotype = new ArrayList<>();
		for (int i = 1; i <= n; i++)
		{
			genotype.add(i);
		}
		Collections.shuffle(genotype);
	}

	public List<Integer> getGenotype()
	{
		return genotype;
	}

	// TODO : Figure out whether getting cost should be from the Individual or the TSPProblem. One way to do it would be Individual.getCost(TSPProblem). Or TSPProblem.getCost(Individual).
	public double getCost()
	{
		return 0.0;
	}

	@Override
	public String toString()
	{
		String out = new String();
		for (Iterator<Integer> it = genotype.iterator(); it.hasNext();)
		{
			out += String.valueOf(it.next()) + "\n";
		}
		out += "-1\n"; // terminates the tour
		return out;
	}
}
